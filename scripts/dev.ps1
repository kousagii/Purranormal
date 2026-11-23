param(
    [ValidateSet('build', 'run', 'test', 'package', 'clean')]
    [string]$Action = 'run',
    [switch]$DesktopChecks
)
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
Push-Location -LiteralPath $projectRoot
try {
    function Remove-GeneratedDirectory([string]$Name) {
        $target = [IO.Path]::GetFullPath((Join-Path $projectRoot $Name))
        if ($target -notin @((Join-Path $projectRoot 'build'), (Join-Path $projectRoot 'dist'))) {
            throw "Refusing to remove unexpected directory: $target"
        }
        if (Test-Path -LiteralPath $target) { Remove-Item -LiteralPath $target -Recurse -Force }
    }
    if ($Action -eq 'clean') {
        Remove-GeneratedDirectory 'build'
        Remove-GeneratedDirectory 'dist'
        return
    }
    if ($Action -eq 'test' -and -not (Test-Path -LiteralPath 'test')) {
        throw 'The local regression suite is not included in the source distribution. Use build, run, or package.'
    }

    $jdkCandidates = @()
    if ($env:JAVA_HOME) { $jdkCandidates += $env:JAVA_HOME }
    $compiler = Get-Command javac -ErrorAction SilentlyContinue
    if ($compiler) { $jdkCandidates += Split-Path (Split-Path $compiler.Source -Parent) -Parent }
    # Common Windows JDK locations; no machine-specific path is committed.
    foreach ($folder in @('Eclipse Adoptium', 'Java', 'Microsoft')) {
        $vendorPath = Join-Path $env:ProgramFiles $folder
        if (Test-Path -LiteralPath $vendorPath) {
            $jdkCandidates += @(Get-ChildItem -LiteralPath $vendorPath -Directory |
                Where-Object Name -Match 'jdk' | Sort-Object Name -Descending | ForEach-Object FullName)
        }
    }
    $jdkRoot = $null
    foreach ($candidate in ($jdkCandidates | Select-Object -Unique)) {
        $javacPath = Join-Path $candidate 'bin\javac.exe'
        if (-not (Test-Path -LiteralPath $javacPath)) { continue }
        $versionText = (& $javacPath -version 2>&1 | Out-String).Trim()
        if ($LASTEXITCODE -eq 0 -and $versionText -match 'javac (\d+)' -and [int]$Matches[1] -ge 17) {
            $jdkRoot = $candidate
            break
        }
    }
    if (-not $jdkRoot) { throw 'JDK 17 or newer is required. Set JAVA_HOME to its installation folder, then restart the VS Code terminal.' }
    $javaPath = Join-Path $jdkRoot 'bin\java.exe'
    $javacPath = Join-Path $jdkRoot 'bin\javac.exe'
    $jarPath = Join-Path $jdkRoot 'bin\jar.exe'
    Write-Host "Using JDK: $jdkRoot"

    # Always compile cleanly so removed/renamed classes cannot survive.
    Remove-GeneratedDirectory 'build'
    New-Item -ItemType Directory -Path 'build\classes' -Force | Out-Null
    $sources = @(Get-ChildItem -LiteralPath src -Recurse -Filter *.java | ForEach-Object FullName)
    & $javacPath --release 17 -encoding UTF-8 '-Xlint:all,-serial' -Werror -d 'build\classes' $sources
    if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed.' }
    Write-Host 'Build passed.'

    if ($Action -eq 'test') {
        New-Item -ItemType Directory -Path 'build\test' -Force | Out-Null
        $tests = @(Get-ChildItem -LiteralPath test -Recurse -Filter *.java | ForEach-Object FullName)
        & $javacPath --release 17 -encoding UTF-8 '-Xlint:all,-serial' -Werror -cp 'build\classes' -d 'build\test' $tests
        if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed.' }
        & $javaPath '-Djava.awt.headless=true' -cp 'build\classes;res;build\test' main.ProjectChecks $projectRoot 'build\screenshots'
        if ($LASTEXITCODE -ne 0) { throw 'Regression checks failed.' }
    }
    if ($Action -eq 'package' -or $Action -eq 'test') {
        New-Item -ItemType Directory -Path dist -Force | Out-Null
        & $jarPath --create --file 'dist\Purranormal.jar' --main-class main.Main -C 'build\classes' . -C res .
        if ($LASTEXITCODE -ne 0) { throw 'Packaging failed.' }
        Write-Host 'Packaged dist\Purranormal.jar'
        if ($Action -eq 'test') {
            & $javaPath '-Djava.awt.headless=true' -cp 'dist\Purranormal.jar;build\test' main.ProjectChecks $projectRoot
            if ($LASTEXITCODE -ne 0) { throw 'Packaged JAR regression checks failed.' }
        }
    }
    if ($Action -eq 'run') {
        & $javaPath -cp 'build\classes;res' main.Main
        if ($LASTEXITCODE -ne 0) { throw 'Game exited with an error.' }
    }
    if ($Action -eq 'test' -and $DesktopChecks) {
        & $javaPath -cp 'build\classes;res;build\test' main.PuzzleChecks 'build\screenshots'
        if ($LASTEXITCODE -ne 0) { throw 'Desktop puzzle checks failed.' }
    }
} finally {
    Pop-Location
}

