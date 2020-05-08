package tech.beryllium.services;

import tech.beryllium.models.AsciiModel;

public class AsciiService {

    private AsciiModel[] asciiModels;
    public AsciiService() {
        createAsciiModels(this.asciiFigures);
    }

    public AsciiModel getAsciiByProgression(int progresssion) {
        if (progresssion > 6 || progresssion < 0) {
            return null;
        }
        return asciiModels[progresssion];
    }


    private void createAsciiModels(String[] asciiFigures) {
        asciiModels = new AsciiModel[7];

        for (int progression = 0; progression <= 6; progression++) {
            this.asciiModels[progression] = new AsciiModel();
            this.asciiModels[progression].setAsciiArt(asciiFigures[progression]);
            this.asciiModels[progression].setProgress(progression);
        }
    }




    private final static String[] asciiFigures = new String[] {
                    "  +---+\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\ |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\ |\n" +
                    " /    |\n" +
                    "      |\n" +
                    "=========",
                    "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    " / \\  |\n" +
                    "      |\n" +
                    "========="
    };

}
