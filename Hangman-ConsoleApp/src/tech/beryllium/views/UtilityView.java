package tech.beryllium.views;

import tech.beryllium.models.ChoiceModel;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UtilityView {

    private static Scanner scanner;

    /**
     * instantiates a new instance with a global scanner
     */
    public UtilityView () {
        this.scanner = new Scanner(System.in);
    }

    /**
     * presents the client with a choice and lets the player respond
     * @param options an array of the choices the player has
     * @param prompt The prompt to be presented to the player
     * @return the selected choice(unsecured)
     */
    public ChoiceModel getChoice(ChoiceModel[] options, String prompt) {
        presentPrompt(prompt);

        for (var choice : options) {
            presentPrompt(Integer.toString(choice.getId()) + ". " + choice.getChoise());
        }
        try {
            var selection = scanner.nextInt();
            return options[selection];
        } catch (InputMismatchException exception) {
            return null;
        } catch (ArrayIndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * prints a prompt to the console along with a "\n" character
     * @param prompt the prompt to be presented to the client
     */
    public static void presentPrompt(String prompt) {
        System.out.println(prompt);
    }

    /**
     * uses the global scanner to fetch the input from the client
     * @return a string consisting of the answer provided by the client
     */
    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * uses th global scanner to await the player pressing enter
     */
    public void awaitInput() {
        scanner.nextLine();
    }
}
