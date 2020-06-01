
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.*;
import javax.swing.Timer;

public class SoundEffect {
    Long currentFrame;
    Clip clip;
    String status;
    AudioInputStream audioInputStream;
    ArrayList<Boolean> stopSoundEffects;

    // Constructor if we need to cancel soundeffects.
    public SoundEffect(String filePath, ArrayList<Boolean> stopSoundEffects)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        this.stopSoundEffects = stopSoundEffects;

        Timer stopSoundEffectTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (stopSoundEffects.contains(true)) {
                    clip.stop();
                    status = "stopped";
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        stopSoundEffectTimer.start();
    }

    public SoundEffect(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void play() {
        clip.start();
        status = "play";
    }

    public void stop() {
        clip.stop();
        status = "stopped";
    }
}