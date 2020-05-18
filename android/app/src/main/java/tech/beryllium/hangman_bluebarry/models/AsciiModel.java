package tech.beryllium.hangman_bluebarry.models;

public class AsciiModel {
    private String asciiArt;
    private int progress;

    public String getAsciiArt() {
        return asciiArt;
    }

    public AsciiModel setAsciiArt(String asciiArt) {
        this.asciiArt = asciiArt;
        return null;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
