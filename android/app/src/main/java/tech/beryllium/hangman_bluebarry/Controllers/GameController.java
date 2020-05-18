package tech.beryllium.hangman_bluebarry.Controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import tech.beryllium.hangman_bluebarry.Services.AsciiService;
import tech.beryllium.hangman_bluebarry.Services.DataService;
import tech.beryllium.hangman_bluebarry.Services.ErrorService;
import tech.beryllium.hangman_bluebarry.Services.GameEntityService;
import tech.beryllium.hangman_bluebarry.Views.GameView;
import tech.beryllium.hangman_bluebarry.Views.UtilityView;
import tech.beryllium.hangman_bluebarry.models.ChoiceModel;
import tech.beryllium.hangman_bluebarry.models.DataModel;
import tech.beryllium.hangman_bluebarry.models.GameDataModel;
import tech.beryllium.hangman_bluebarry.models.MainActivityModel;

public class GameController {
        private DataService _dataService;
        private GameView _gameView;

        private boolean isHost;
        private int clientDesignation;

        private GameDataModel currentGameState;
        private MainActivityModel _mainActivityModel;

        private Context context;

        /**
         * instantiates a new instance with a global GameView
         */
        public GameController(Context context, MainActivityModel mainActivityModel) {
            this._mainActivityModel = mainActivityModel;
            this._gameView = new GameView(this._mainActivityModel);
            this.context = context;
        }


        /**
         * querys the player about minimal information and the continues to set up a minimal game controller ready for the
         * startGame() method call
         * @throws Exception see nested javadoc
         */
        public void setupGame() throws Exception {
            doJoinGame();

        }
        private void ifHostGame() {
            try {
                this.isHost = true;
                this.clientDesignation = 1;

                fetchDifficulty();
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.context, e.getMessage());
            }

        }
        private void setDifficulty(int difficulty) {
            try {
                this._dataService = new DataService();
                GameDataModel GameData = Hangman.createGame(_dataService, new GameEntityService(new Random()), difficulty);
                this.currentGameState = GameData;
                this._gameView.PresentPrompt("your id is:" + this.currentGameState.id);
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.context, e.getMessage());
            }


        }
        private void ifJoinGame() {
            try {
                this.isHost = false;
                this.clientDesignation = 2;
                int id = fetchId();
                this._dataService = new DataService(id);
                GameDataModel game = Hangman.joinGame(this._dataService);
                if (game == null) {
                    throw new Exception("id was wrong");
                }

                this.currentGameState = game;
                this._gameView.PresentPrompt("your id is:" + this.currentGameState.id);
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.context, e.getMessage());
            }

        }

        /**
         * primary execution chain starting and maintaining the game until finished
         * @throws IOException general network errors
         */
        public void startGame() throws IOException {
            while(!this.currentGameState.hasWon) {
                if(Hangman.isClientTurn(this._dataService, this.clientDesignation)) {

                    Hangman hangman = new Hangman(this._dataService);
                    PresentRound(hangman.getDataModel());

                    this.currentGameState = this._dataService.getGameDataModel();
                    if (hangman.getDataModel().progression >= 6) {
                        hangman.timeDeath(isHost);
                        promptLoss(true);
                        break;
                    } else if (this.currentGameState.hasWon == true) {
                        promptLoss(false);
                        break;
                    }


                    char guess = fetchGuess();
                    this.currentGameState = hangman.nextTurn(guess, this.isHost);

                    if (this.currentGameState.hasWon && this.currentGameState.winner == clientDesignation) {
                        promptWin();
                        break;
                    }
                }
                this._gameView.PresentPrompt("Press Refresh");
                this._gameView.awaitInput();
            }
        }

        private void promptLoss(boolean byTime) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            if (byTime) {
                builder.setTitle("Time!");
                builder.setMessage("You ran out of guesses.");
            } else {
                builder.setTitle("Loss");
                builder.setMessage("Sorry :( You seem to have lost.");
            }
            builder.create().show();
        }

    private void promptWin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

            builder.setTitle("Yay");
            builder.setMessage("You have won!");

        builder.create().show();
    }

        /**
         * query and accepts the players guess
         * @return secured guess as char
         */
        private char fetchGuess() {
            this._gameView.PresentPrompt("Please enter your guess and press enter:");
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
        private void PresentRound(DataModel dataModel) {
            GameView.printAscii(new AsciiService()
                    .getAsciiByProgression(dataModel.progression));
            GameView.printRoundStats(dataModel);
        }

        /**
         * query and accepts the players requested difficulty
         * @return secured difficulty represented by an integer
         */
        private void fetchDifficulty() {
//            String prompt = "Please choose your difficulty";
//            ChoiceModel[] options = new ChoiceModel[] {
//                    new ChoiceModel("Easy", 0),
//                    new ChoiceModel("Moderate", 1),
//                    new ChoiceModel("Difficult", 2)
//            };
//            ChoiceModel choice = null;
//            do {
//                choice = this._gameView.GetChoice(options, prompt);
//            } while (choice == null);
//
//            return choice.getId();
            String[] options = new String[] {
                    "Easy",
                    "Moderate",
                    "Difficult"
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Select Difficulty");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    setDifficulty(index);
                }
            });

        }

        /**
         * query and accepts the players session id
         * @return the id as an integer
         */
        private int fetchId() {
            String prompt = "Please enter game id:";
            this._gameView.PresentPrompt(prompt);

            int response;
            do {
                try {
                    String initInput = this._gameView.getInput();
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
        private void doJoinGame() {
            /*ChoiceModel[] choices = new ChoiceModel[] {
                    new ChoiceModel("join", 0),
                    new ChoiceModel("create", 1)
            };
            String prompt = "Create or join";

            ChoiceModel input;
            do {
                input = _gameView.GetChoice(choices, prompt);
            } while (input == null);

            if (input.getId() == 0) {
                return true;
            } else {
                return false;
            }*/
            String[] options = new String[] {
                    "Create",
                    "Join"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Create or Join?");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int index) {
                    switch (index) {
                        case 0:
                            ifHostGame();
                            break;
                        case 1:
                            ifJoinGame();
                    }

                }
            });


        }
    }

