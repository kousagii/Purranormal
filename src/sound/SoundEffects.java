package sound;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.*;

/** Short CC0 effects, loaded away from the Swing event thread. */
public final class SoundEffects implements AutoCloseable {
    public enum Cue { CLICK, INTERACT, PICKUP, PLACE, BOOK, PORTRAIT, DOOR, PUZZLE, ENDING }
    private final Map<Cue, Clip> clips = new EnumMap<>(Cue.class);
    private final ExecutorService audio = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "purranormal-sfx");
        thread.setDaemon(true);
        return thread;
    });
    private volatile boolean closed;

    public SoundEffects() {
        if (java.awt.GraphicsEnvironment.isHeadless()) return;
        audio.execute(() -> {
            for (Cue cue : Cue.values()) {
                try {
                    URL url = getClass().getResource("/sound/sfx/" + cue.name().toLowerCase(java.util.Locale.ROOT) + ".wav");
                    if (url == null) throw new IOException("Missing effect: " + cue);
                    Clip clip = AudioSystem.getClip();
                    try (AudioInputStream stream = AudioSystem.getAudioInputStream(url)) { clip.open(stream); }
                    clips.put(cue, clip);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | IllegalArgumentException e) {
                    System.err.println("Sound effect unavailable: " + cue + " (" + e.getMessage() + ")");
                }
            }
        });
    }

    public void play(Cue cue) {
        if (closed || java.awt.GraphicsEnvironment.isHeadless()) return;
        audio.execute(() -> {
            Clip clip = clips.get(cue);
            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
            }
        });
    }

    @Override public void close() {
        if (closed) return;
        closed = true;
        audio.execute(() -> {
            for (Clip clip : clips.values()) { clip.stop(); clip.close(); }
            clips.clear();
        });
        audio.shutdown();
    }
}

