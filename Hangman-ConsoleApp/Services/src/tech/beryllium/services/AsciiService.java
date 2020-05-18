package tech.beryllium.services;

import tech.beryllium.models.AsciiModel;

public class AsciiService {

    private AsciiModel[] asciiModels;

    /**
     * initiates the class with its list of ascii models from a hardcoded array of strings
     */
    public AsciiService() {
        createAsciiModels(this.asciiFigures);
    }

    /**
     * returns an ascii model by the progression requested
     * @param progression the progression of death that is wanted 0<Progression<7
     * @return returns an asciiModel identified with the progression
     */
    public AsciiModel getAsciiByProgression(int progression) {
        if (progression > 6 || progression < 0) {
            return null;
        }
        return asciiModels[progression];
    }

    /**
     * used to initiate the class by creating an array of ascii models from an array of hardcoded ascii strings
     * @param asciiFigures the hardcoded array of strings containing the ascii art
     */
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
