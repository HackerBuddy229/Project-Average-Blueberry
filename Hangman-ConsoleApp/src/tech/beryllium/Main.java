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

    /**
     * inits the game controller
     * @throws Exception see nested javadoc
     */
    private static void playGame() throws Exception {
        var gameController = new GameController();
        gameController.setupGame();
        gameController.startGame();
    }

    /**
     * Presents error information regarding network connectivity etc
     * @param error the message of the exception instance
     */
    private static void displayNetworkError(String error) {
        System.out.println("you have experienced a network error:" + error);
    }

    /**
     * Presents error information regarding general issues
     * @param error the message of the exception instance
     */
    private static void displayGeneralError(String error) {
        System.out.println("an error has occurred:" + error);
    }
}
