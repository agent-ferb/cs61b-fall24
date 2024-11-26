package core;

import javax.sound.sampled.*; //@source From https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/sound/sampled/package-summary.html
import java.io.File;
import java.io.IOException;

public class MusicPlayer { //@source From https://stackoverflow.com/questions/20811728/adding-music-sound-to-java-programs
    private Clip clip;

    public void playMusic(String filePath) {
        try {
            // Open audio input stream
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Loop the audio indefinitely
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing music: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
