package tech.beryllium.views;

import tech.beryllium.models.AsciiModel;
import tech.beryllium.models.DataModel;

public class GameView extends UtilityView{

    public static void printAscii(AsciiModel asciiModel) {
        System.out.println(asciiModel);
    }
    public static void printRoundStats(DataModel dataModel) {
        System.out.println("Correct guesses");
        for (var guess :
                dataModel.correctGuesses) {
            System.out.print(guess);
        }
        System.out.println("Wrong guesses");
        for (var guess :
                dataModel.wrongGuesses) {
            System.out.print(guess);
        }

    }

}
