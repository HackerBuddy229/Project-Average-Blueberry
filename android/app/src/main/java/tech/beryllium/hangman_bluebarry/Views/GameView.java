package tech.beryllium.hangman_bluebarry.Views;


import tech.beryllium.hangman_bluebarry.models.AsciiModel;
import tech.beryllium.hangman_bluebarry.models.DataModel;
import tech.beryllium.hangman_bluebarry.models.MainActivityModel;

public class GameView extends UtilityView {
    public GameView(MainActivityModel mainActivityModel){
        super.mainActivityModel = mainActivityModel;
    }
    /**
     * Static method that prints the ascii art of an ascii model instance to the console
     *
     * @param asciiModel the aforementioned ascii model
     */
    public void printAscii(AsciiModel asciiModel) {
        super.mainActivityModel.ascii.setText(asciiModel.getAsciiArt());
    }

    /**
     * Prints the statistics and information provided to the player per round
     *
     * @param dataModel the datamodel from which the information is fetched
     */
    public void printRoundStats(DataModel dataModel) {
        StringBuilder output = new StringBuilder();

        output.append("Representative String: " + dataModel.representativeString + "\n");

        output.append("Correct guesses: ");
        for (char guess :
                dataModel.correctGuesses) {
            output.append(guess + ", ");
        }
        output.append("\n");
        output.append("Wrong guesses");
        for (char guess :
                dataModel.wrongGuesses) {
            output.append(guess + ", ");
        }
        output.append("\n");

        super.mainActivityModel.stats.setText(output.toString());
    }


}

