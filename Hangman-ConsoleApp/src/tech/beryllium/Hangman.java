package tech.beryllium;

import tech.beryllium.models.DataModel;
import tech.beryllium.models.GameDataModel;
import tech.beryllium.services.DataModelBuilder;
import tech.beryllium.services.DataService;
import tech.beryllium.services.GameEntityService;

import java.io.IOException;

public class Hangman {

    private DataModel dataModel;
    private GameDataModel gameDataModel;
    private DataService _dataService;

    /**
     * Instantiates with global secure dataservice
     * @param dataService secure init dataservice
     * @throws IOException see nested javadoc
     */
    public Hangman(DataService dataService) throws IOException {
        this._dataService = dataService;
        initGlobal();
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    /**
     * initiates global variables with the global dataservice
     * @throws IOException
     */
    private void initGlobal() throws IOException {
        this.gameDataModel = _dataService.getGameDataModel();
        this.dataModel = new DataModelBuilder(this.gameDataModel).writeDataModel();
    }

    /**
     * uses a secure dataService to join a game
     * @param dataService a secure dataservice
     * @return the updated GameDataModel
     * @throws IOException see nested javadoc
     */
    public static GameDataModel joinGame(DataService dataService) throws IOException {
        var fetchedData = dataService.getGameDataModel();
        if (fetchedData.turn != 3) {
            return null;
        }

        fetchedData.turn = 1;
        dataService.postGameModel(fetchedData);

        return fetchedData;
    }

    /**
     * uses a secured data service and misc to create and post a game to the api.
     * @param dataService a secure init dataservice
     * @param gameEntityService a secure gameEntityService
     * @param difficulty a secure integer representing difficulty
     * @return an updated gameDataModel
     * @throws IOException see nested javadoc
     */
    public static GameDataModel createGame(DataService dataService, GameEntityService gameEntityService, int difficulty) throws IOException {
        var newGame = new GameDataModel();
        newGame = initNewDataModel(newGame);

        newGame.correctWord = gameEntityService.getGameEntityByDifficulty(difficulty);

        dataService.postGameModel(newGame);
        return dataService.getGameDataModel();
    }

    /**
     * Uses a secure init DataService to ascertain the claim
     * @param dataService a secure dataservice
     * @param clientIdent an integer representing the client identity
     * @return a boolean representing the claim
     * @throws IOException see nested javadoc
     */
    public static boolean isClientTurn(DataService dataService, int clientIdent) throws IOException {
        var gameDataModel = dataService.getGameDataModel();
        if (gameDataModel.turn == clientIdent) {
            return true;
        }
        return false;
    }

    /**
     * Prepares and posts a gameDataModel to increment the turn
     * @param guess the secured guess provided by the client during their current turn
     * @param isHost a boolean representing the claim in relation to the game
     * @return the updated GameDataModel
     * @throws IOException see nested javadoc
     */
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

    /**
     * preps and posts a GameDataModel to declare timeDeath
     * @param isHost a boolean representing the claim in relation to the game
     * @throws IOException see nested javadoc
     */
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

    /**
     * uses the global gameDataModel to assert the claim
     * @return a boolean representing the claim in relation to the game
     */
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

    /**
     * initiates and provides a basic gameDataModel
     * @param gameDataModel a new gameDatamodel instance
     * @return the init gameDataModel
     */
    private static GameDataModel initNewDataModel(GameDataModel gameDataModel) {
        gameDataModel.turn = 3;
        gameDataModel.guesses = "";
        gameDataModel.hasWon = false;
        gameDataModel.key = "";
        gameDataModel.winner = 3;
        return gameDataModel;
    }
}
