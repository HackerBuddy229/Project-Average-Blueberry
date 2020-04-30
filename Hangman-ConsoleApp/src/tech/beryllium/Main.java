package tech.beryllium;

import tech.beryllium.models.*;
import tech.beryllium.services.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        var model = new GameDataModel();
            model.id = 25;
            model.correctWord = "hi";
            model.turn = 1;
            model.guesses = "b,d,e";
            model.hasWon = true;
            model.winner = 0;
            model.key = "asdgff";


        try {
            var dataService = new DataService();

            var returnModel = dataService.postGameModel(model);
            System.out.println(returnModel.key);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
