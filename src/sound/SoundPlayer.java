package sound;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/** A missing audio device disables music without preventing gameplay. */
public final class SoundPlayer implements AutoCloseable {
    private Clip clip;
    private FloatControl gain;

    public SoundPlayer(String resource) {
        // Keep automated/headless runs silent and independent of an audio device.
        if (java.awt.GraphicsEnvironment.isHeadless()) return;
        try {
            URL url = SoundPlayer.class.getResource(resource);
            if (url == null) throw new IOException("Missing audio: " + resource);
            try (AudioInputStream input = AudioSystem.getAudioInputStream(url)) {
                clip = AudioSystem.getClip();
                clip.open(input);
            }
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | IllegalArgumentException e) {
            close();
            System.err.println("Music unavailable: " + e.getMessage());
        }
    }

    public void playLoop() { if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY); }

    public void setVolume(float volume) {
        if (gain == null) return;
        float value = Math.max(0, Math.min(1, volume));
        float db = value == 0 ? gain.getMinimum() : (float) (20 * Math.log10(value));
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), db)));
        if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
            ((BooleanControl) clip.getControl(BooleanControl.Type.MUTE)).setValue(value == 0);
        }
    }

    @Override public void close() {
        if (clip != null) { clip.stop(); clip.close(); clip = null; gain = null; }
    }
}

