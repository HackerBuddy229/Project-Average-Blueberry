package tech.beryllium.services;

import java.util.Random;

public class GameEntityService {
    private final String[] EasyWords = new String[] {
      "Easy",
      "small",
            "asdf",
            "kill"
    };

    private final String[] ModarateWords = new String[] {
            "maybe",
            "false",
            "there",
            "where"
    };

    private final String[] DifficultWords = new String[] {
            "difficult",
            "ridicule",
            "amazing",
            "random"
    };
    private Random _random;

    /**
     * Instantiates the class with a global random instance
     * @param random the random instance to be used globally
     */
    public GameEntityService(Random random) {
        this._random = random;
    }

    /**
     * fetces a randomly generated word by difficulty specified
     * @param difficulty the difficulty shall be an integer of -1 < difficulty < 3
     * @return
     */
    public String getGameEntityByDifficulty(int difficulty) {
        switch (difficulty) {
            case 0:
                return EasyWords[this._random.nextInt(EasyWords.length-1)];
            case 1:
                return ModarateWords[this._random.nextInt(ModarateWords.length-1)];
            case 2:
                return DifficultWords[this._random.nextInt(DifficultWords.length-1)];
            default:
                return null;
        }
    }
}
