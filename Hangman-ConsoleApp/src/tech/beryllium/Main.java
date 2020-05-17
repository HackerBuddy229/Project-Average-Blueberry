package tech.beryllium;

import tech.beryllium.models.*;
import tech.beryllium.services.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            playGame();
        } catch (IOException ioException) {
            displayNetworkError(ioException.getMessage());
        } catch (Exception exception) {
            displayGeneralError(exception.getMessage());
        }
    }

    private static void playGame() throws Exception {
        var gameController = new GameController();
        gameController.setupGame();
        gameController.startGame();
    }

    private static void displayNetworkError(String error) {
        System.out.println("you have experienced a network error:" + error);
    }

    private static void displayGeneralError(String error) {
        System.out.println("an error has occurred:" + error);
    }
}
