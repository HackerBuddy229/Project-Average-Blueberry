package tech.beryllium.views;

import org.w3c.dom.ranges.RangeException;
import tech.beryllium.models.ChoiceModel;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UtilityView {

    private static Scanner scanner;

    public UtilityView () {
        this.scanner = new Scanner(System.in);
    }


    public ChoiceModel GetChoice(ChoiceModel[] options, String prompt) {
        PresentPrompt(prompt);

        for (var choice : options) {
            PresentPrompt(Integer.toString(choice.getId()) + "." + " " + choice.getChoise());
        }
        try {
            var selection = scanner.nextInt();
            return options[selection];
        } catch (InputMismatchException exception) {
            return null;
        } catch (RangeException exception) {
            return new ChoiceModel();
        }

    }

    public static void PresentPrompt(String prompt) {
        System.out.println(prompt);
    }
}
