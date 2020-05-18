package tech.beryllium.hangman_bluebarry.models;

public class GameDataModel {
    public int id;
    public String correctWord;
    public int turn;
    public String guesses;
    public boolean hasWon;
    public int winner;
    public String key;

    public GameModel toGameModel() {
        GameModel output = new GameModel();
        output.winner = this.winner;
        output.hasWon = this.hasWon;
        output.guesses = this.guesses;
        output.turn = this.turn;
        output.correctWord = this.correctWord;
        output.key = this.key;

        return output;
    }
}
