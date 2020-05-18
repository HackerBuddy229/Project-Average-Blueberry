package tech.beryllium.hangman_bluebarry.models;

public class ChoiceModel {
    private String choise;
    private int id;

    public ChoiceModel(String choise, int id) {
        this.choise = choise;
        this.id = id;
    }

    public String getChoise() {
        return choise;
    }

    public void setChoise(String choise) {
        this.choise = choise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
