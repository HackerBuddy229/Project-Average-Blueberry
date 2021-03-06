package tech.beryllium;

import tech.beryllium.models.ChoiceModel;
import tech.beryllium.models.DataModel;
import tech.beryllium.models.GameDataModel;
import tech.beryllium.services.AsciiService;
import tech.beryllium.services.DataService;
import tech.beryllium.services.GameEntityService;
import tech.beryllium.views.GameView;
import tech.beryllium.views.UtilityView;

import java.io.IOException;
import java.util.Random;

public class GameController {
    private DataService _dataService;
    private GameView _gameView;

    private boolean isHost;
    private int clientDesignation;

    private GameDataModel currentGameState;

    /**
     * instantiates a new instance with a global GameView
     */
    public GameController() {
        this._gameView = new GameView();
    }


    /**
     * querys the player about minimal information and the continues to set up a minimal game controller ready for the
     * startGame() method call
     * @throws Exception see nested javadoc
     */
    public void setupGame() throws Exception {
        PrintIntro();
        if (doJoinGame()) {
            this.isHost = false;
            this.clientDesignation = 2;
            int id = fetchId();
            this._dataService = new DataService(id);
            var game = Hangman.joinGame(this._dataService);
            if (game == null) {
                throw new Exception("Game-Key was wrong");
            }

            this.currentGameState = game;
            GameView.presentPrompt("your Game-Key is:" + this.currentGameState.id);
            return;
        }
        this.isHost = true;
        this.clientDesignation = 1;

        var difficulty = fetchDifficulty();
        this._dataService = new DataService();

        var GameData = Hangman.createGame(_dataService, new GameEntityService(new Random()), difficulty);
        this.currentGameState = GameData;
        GameView.presentPrompt("your Game-Key is:" + this.currentGameState.id);
        GameView.presentPrompt("Please refresh until a second player has joined...");
    }

    private void PrintIntro() {
        GameView.presentPrompt("Hangman console app");
        GameView.presentPrompt("by Rasmus Bengtsson and Beryllim Tech");
        GameView.breakLine();
        GameView.presentPrompt("please Setup your game according to the following guide...");
    }

    /**
     * primary execution chain starting and maintaining the game until finished
     * @throws IOException general network errors
     */
    public void startGame() throws IOException {
        while(!this.currentGameState.hasWon) {
            if(Hangman.isClientTurn(this._dataService, this.clientDesignation)) {

                var hangman = new Hangman(this._dataService);
                presentRound(hangman.getDataModel());

                this.currentGameState = this._dataService.getGameDataModel();
                if (hangman.getDataModel().progression >= 6) {
                    hangman.timeDeath(isHost);
                    GameView.presentPrompt("You Lose!");
                    break;
                } else if (this.currentGameState.hasWon == true) {
                    GameView.presentPrompt("You Lose!");
                    break;
                }


                var guess = fetchGuess();
                this.currentGameState = hangman.nextTurn(guess, this.isHost);

                if (this.currentGameState.hasWon && this.currentGameState.winner == clientDesignation) {
                    GameView.presentPrompt("Congratulations! you have won");
                    break;
                }

                GameView.presentPrompt("Please refresh until your opponent is finished...");
            }
            UtilityView.presentPrompt("Press enter to refresh");
            this._gameView.awaitInput();
        }
    }

    /**
     * query and accepts the players guess
     * @return secured guess as char
     */
    private char fetchGuess() {
        GameView.presentPrompt("Please enter your guess and press enter:");
        String raw;
        do {
            raw = this._gameView.getInput();
        } while (raw == null || raw.equals(""));

        return raw.toUpperCase().charAt(0);
    }

    /**
     * Uses the dataModel provided by hangman to present relevant information to the player
     * @param dataModel the datamodel extracted from hangman
     */
    private void presentRound(DataModel dataModel) {
        GameView.printRound(dataModel.guess);
        GameView.printAscii(new AsciiService()
                            .getAsciiByProgression(dataModel.progression));
        GameView.printRoundStats(dataModel);
    }

    /**
     * query and accepts the players requested difficulty
     * @return secured difficulty represented by an integer
     */
    private int fetchDifficulty() {
        var prompt = "Please choose your difficulty";
        var options = new ChoiceModel[] {
          new ChoiceModel("Easy(4-5 Characters)", 0),
                new ChoiceModel("Moderate(5 Characters)", 1),
                new ChoiceModel("Difficult(6+ Characters)", 2)
        };
        ChoiceModel choice = null;
        do {
          choice = this._gameView.getChoice(options, prompt);
        } while (choice == null);

        return choice.getId();
    }

    /**
     * query and accepts the players session id
     * @return the id as an integer
     */
    private int fetchId() {
        var prompt = "Please enter the Game-Key of the game you want to join:";
        this._gameView.presentPrompt(prompt);

        int response;
        do {
            try {
                var initInput = this._gameView.getInput();
                response = Integer.parseInt(initInput);
            } catch (Exception exception) {
                response = 0;
            }
        } while (response == 0);

        return response;
    }

    /**
     * querys the player weather they want to join a game
     * @return secured boolean representing the claim
     */
    private boolean doJoinGame() {
        var choices = new ChoiceModel[] {
          new ChoiceModel("join", 0),
          new ChoiceModel("create", 1)
        };
        var prompt = "Create or join";

        ChoiceModel input;
        do {
            input = _gameView.getChoice(choices, prompt);
        } while (input == null);

        if (input.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
