package tech.beryllium;

import tech.beryllium.models.DataModel;
import tech.beryllium.models.GameDataModel;
import tech.beryllium.models.GameModel;
import tech.beryllium.services.DataModelBuilder;
import tech.beryllium.services.DataService;
import tech.beryllium.services.GameEntityService;

import java.io.IOException;

public class Hangman {

    private DataModel dataModel;
    private GameDataModel gameDataModel;
    private DataService _dataService;

    public Hangman(DataService dataService) throws IOException {
        this._dataService = dataService;
        initGlobal();
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    private void initGlobal() throws IOException {
        this.gameDataModel = _dataService.getGameDataModel();
        this.dataModel = new DataModelBuilder(this.gameDataModel).writeDataModel();
    }

    public static GameDataModel joinGame(DataService dataService) throws IOException {
        var fetchedData = dataService.getGameDataModel();
        if (fetchedData.turn != 3) {
            return null;
        }

        fetchedData.turn = 1;
        dataService.postGameModel(fetchedData);

        return fetchedData;
    }

    public static GameDataModel createGame(DataService dataService, GameEntityService gameEntityService, int difficulty) throws IOException {
        var newGame = new GameDataModel();
        newGame = initNewDataModel(newGame);

        newGame.correctWord = gameEntityService.GetGameEntityByDifficulty(difficulty);

        dataService.postGameModel(newGame);
        return dataService.getGameDataModel();
    }


    public static boolean isClientTurn(DataService dataService, int clientIdent) throws IOException {
        var gameDataModel = dataService.getGameDataModel();
        if (gameDataModel.turn == clientIdent) {
            return true;
        }
        return false;
    }

    public GameDataModel nextTurn(char guess, boolean isHost) throws IOException {
        if (this.gameDataModel.guesses == "" || this.gameDataModel.guesses == null) {
            this.gameDataModel.guesses = this.gameDataModel.guesses + guess;
        } else {
            this.gameDataModel.guesses = this.gameDataModel.guesses + "," + guess;
        }

        if (isHost) {
            this.gameDataModel.turn = 2;
        } else {
            this.gameDataModel.turn = 1;
        }

        if (hasWon()) {
            this.gameDataModel.hasWon = true;
            if (isHost) {
                this.gameDataModel.winner = 1;
            } else {
                this.gameDataModel.winner = 2;
            }
        }

        this._dataService.postGameModel(this.gameDataModel);
        return this.gameDataModel;

    }

    public void timeDeath(boolean isHost) throws IOException {
        this.gameDataModel.hasWon = true;
        this.gameDataModel.winner = 3;
        if (isHost) {
            this.gameDataModel.turn = 2;
        } else {
            this.gameDataModel.turn = 1;
        }

        this._dataService.postGameModel(this.gameDataModel);
    }

    private boolean hasWon() {
        for (var letter : this.gameDataModel.correctWord.toUpperCase().toCharArray()) {
            var contains = false;
            for (var guess : this.gameDataModel.guesses.split(",")) {
                try {
                    if (letter == guess.charAt(0)) {
                        contains = true;
                    }
                } catch (StringIndexOutOfBoundsException exception) {
                    continue;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    private static GameDataModel initNewDataModel(GameDataModel gameDataModel) {
        gameDataModel.turn = 3;
        gameDataModel.guesses = "";
        gameDataModel.hasWon = false;
        gameDataModel.key = "";
        gameDataModel.winner = 3;
        return gameDataModel;
    }
}
