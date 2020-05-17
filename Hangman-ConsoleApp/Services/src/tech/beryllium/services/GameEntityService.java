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

    public GameEntityService(Random random) {
        this._random = random;
    }

    public String GetGameEntityByDifficulty(int difficulty) {
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
