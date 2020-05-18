package tech.beryllium.hangman_bluebarry.Controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import tech.beryllium.hangman_bluebarry.MainActivity;
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

        private MainActivity mainActivity;
        private Boolean valid = true;

        private Hangman roundHangman;

        /**
         * instantiates a new instance with a global GameView
         */
        public GameController(MainActivity mainActivity, MainActivityModel mainActivityModel) {
            this._mainActivityModel = mainActivityModel;
            this._gameView = new GameView(this._mainActivityModel);
            this.mainActivity = mainActivity;
        }


        /**
         * querys the player about minimal information and the continues to set up a minimal game controller ready for the
         * startGame() method call
         * @throws Exception see nested javadoc
         */
        public void setupGame() {
            doJoinGame();

        }

        private void ifHostGame() {
            try {
                this.isHost = true;
                this.clientDesignation = 1;

                fetchDifficulty();
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.mainActivity, e.getMessage());
            }

        }

        private void setDifficulty(int difficulty) {
            try {
                this._dataService = new DataService();
                GameDataModel GameData = Hangman.createGame(_dataService, new GameEntityService(new Random()), difficulty);
                this.currentGameState = GameData;
                this._gameView.PresentPrompt("your id is:" + this.currentGameState.id);
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.mainActivity, e.getMessage());
            }


        }

        private void ifJoinGame() {
            try {
                this.isHost = false;
                this.clientDesignation = 2;
                fetchId();

            } catch (Exception e) {
                ErrorService.displayNetworkError(this.mainActivity, e.getMessage());
            }

        }

        private void ifJoinSetId(int id) {
            try {
                this._dataService = new DataService(id);
                GameDataModel game = Hangman.joinGame(this._dataService);
                if (game == null) {
                    throw new IOException("id was wrong");
                }

                this.currentGameState = game;
                this._gameView.PresentPrompt("your id is:" + this.currentGameState.id);
            } catch (IOException e) {
                ErrorService.displayNetworkError(this.mainActivity, e.getMessage());
            }

        }

        /**
         * primary execution chain starting and maintaining the game until finished
         * @throws IOException general network errors
         */
        public void iterateGame() throws IOException {

            if (valid) {
                if(Hangman.isClientTurn(this._dataService, this.clientDesignation)) {

                    this.roundHangman = new Hangman(this._dataService);
                    PresentRound(roundHangman.getDataModel());

                    this.currentGameState = this._dataService.getGameDataModel();
                    if (roundHangman.getDataModel().progression >= 6) {
                        roundHangman.timeDeath(isHost);
                        promptLoss(true);
                        this.valid = false;
                        return;
                    } else if (this.currentGameState.hasWon == true) {
                        promptLoss(false);
                        this.valid = false;
                        return;
                    }


                    fetchGuess();
                }
            }



        }

        private void doGuess() {
            try {
                this._mainActivityModel.submit.setOnClickListener(null);
                char guess = this._mainActivityModel.guess.getText().toString().toUpperCase().charAt(0);
                this.currentGameState = roundHangman.nextTurn(guess, this.isHost);
                this._mainActivityModel.hints.setText("Please refresh until the other player is finished");
                this._mainActivityModel.guess.setText("");

                if (this.currentGameState.hasWon && this.currentGameState.winner == clientDesignation) {
                    promptWin();
                    this.valid = false;
                    return;
                }
            } catch (Exception e) {
                ErrorService.displayNetworkError(this.mainActivity, e.getMessage());
            }

        }

        private void promptLoss(boolean byTime) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);

            builder.setTitle("Yay");
            builder.setMessage("You have won!");

        builder.create().show();
    }

        /**
         * query and accepts the players guess
         * @return secured guess as char
         */
        private void fetchGuess() {
            this._gameView.PresentPrompt("Please enter your guess and press submit:");

            this._mainActivityModel.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doGuess();
                }
            });
        }

        /**
         * Uses the dataModel provided by hangman to present relevant information to the player
         * @param dataModel the datamodel extracted from hangman
         */
        private void PresentRound(DataModel dataModel) {
            this._gameView.printAscii(new AsciiService()
                    .getAsciiByProgression(dataModel.progression));
            this._gameView.printRoundStats(dataModel);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
            builder.setTitle("Select Difficulty");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    setDifficulty(index);
                }
            });
            builder.create().show();

        }

        /**
         * query and accepts the players session id
         * @return the id as an integer
         */
        private void fetchId() {
            /*String prompt = "Please enter game id:";
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

            return response;*/

            String prompt = "Please enter game id:";
            this._gameView.PresentPrompt(prompt);

            this._mainActivityModel.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _mainActivityModel.submit.setOnClickListener(null);
                    try {
                        ifJoinSetId(Integer.parseInt(_mainActivityModel.guess.getText().toString()));
                    } catch (Exception e) {
                        ErrorService.displayNetworkError(mainActivity, "Id malformation error");
                    }
                }
            });
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

            AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
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
            builder.create().show();

        }
    }

