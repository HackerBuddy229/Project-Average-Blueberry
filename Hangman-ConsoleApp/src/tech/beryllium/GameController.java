package tech.beryllium;

import tech.beryllium.models.ChoiceModel;
import tech.beryllium.models.GameDataModel;
import tech.beryllium.services.DataService;
import tech.beryllium.services.GameEntityService;
import tech.beryllium.views.UtilityView;

import java.io.IOException;
import java.util.Random;

public class GameController {
    private DataService _dataService;
    private UtilityView _utilityView;

    private boolean host;
    private int hostDesignation;

    private GameDataModel currentGameState;

    public GameController() {
        this._utilityView = new UtilityView();
    }



    public void setupGame() throws Exception {
        if (doJoinGame()) {
            this.host = false;
            this.hostDesignation = 2;
            int id = fetchId();
            this._dataService = new DataService(id);
            var game = Hangman.joinGame(this._dataService);
            if (game == null) {
                throw new Exception("id was wrong");
            }
            this.currentGameState = game;
            return;
        }
        this.host = true;
        this.hostDesignation = 1;

        var difficulty = fetchDifficulty();
        this._dataService = new DataService();

        var GameData = Hangman.createGame(_dataService, new GameEntityService(new Random()), difficulty);
        this.currentGameState = GameData;
    }

    public void startGame() throws IOException {
        while(this.currentGameState.hasWon == false) {
            if(Hangman.isClientTurn(this._dataService, this.hostDesignation)) {
                var hangman = new Hangman(this._dataService);

            }
            UtilityView.PresentPrompt("Press enter to refresh");
            this._utilityView.awaitInput();
        }
    }

    private int fetchDifficulty() {
        var prompt = "Please choose your difficulty";
        var options = new ChoiceModel[] {
          new ChoiceModel("Easy", 1),
                new ChoiceModel("Moderate", 2),
                new ChoiceModel("Difficult", 3)
        };
        ChoiceModel choice = null;
        do {
          choice = this._utilityView.GetChoice(options, "choose you desired difficulty");
        } while (choice != null);

        return choice.getId();
    }

    private int fetchId() {
        var prompt = "Please enter game id:";
        this._utilityView.PresentPrompt(prompt);

        int response;
        do {
            try {
                var initInput = this._utilityView.getInput();
                response = Integer.parseInt(initInput);
            } catch (Exception exception) {
                response = 0;
            }
        } while (response != 0);

        return response;
    }

    private boolean doJoinGame() {
        var choices = new ChoiceModel[] {
          new ChoiceModel("join", 0),
          new ChoiceModel("create", 1)
        };
        var prompt = "Create or join";

        ChoiceModel input;
        do {
            input = _utilityView.GetChoice(choices, prompt);
        } while (input == null);

        if (input.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
