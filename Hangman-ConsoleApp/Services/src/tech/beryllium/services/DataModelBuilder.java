package tech.beryllium.services;

import tech.beryllium.models.DataModel;
import tech.beryllium.models.GameDataModel;

import java.util.ArrayList;

public class DataModelBuilder {
    private GameDataModel _gameDataModel;
    private DataModel _dataModel;

    private UtilityService _utilityService;

    public DataModelBuilder(GameDataModel gameDataModel) {
        this._gameDataModel = gameDataModel;
        this._utilityService = new UtilityService();
    }

    public DataModelBuilder() {
        this._utilityService = new UtilityService();
    }

    public void setGameDataModel(GameDataModel _gameDataModel) {
        this._gameDataModel = _gameDataModel;
    }

    private void sortGuesses(GameDataModel gameDataModel) {
        var correctList = new ArrayList<Character>();
        var wrongList = new ArrayList<Character>();

        for (var guess: gameDataModel.guesses.split(",")) {
            try { guess.charAt(0); } catch (Exception e) { continue; }
            if(this._utilityService.charArrayContains(gameDataModel.correctWord.toUpperCase().toCharArray(), guess.charAt(0))) {
                correctList.add(guess.charAt(0));
            } else {
                wrongList.add(guess.charAt(0));
            }
        }

        _dataModel.correctGuesses = characterCollectionToArray(correctList);
        _dataModel.wrongGuesses = characterCollectionToArray(wrongList);
    }

    private Character[] characterCollectionToArray(ArrayList<Character> input) {
        var array = new Character[input.size()];
        for (var index = 0; index < array.length; index++) {
            array[index] = input.get(index);
        }
        return array;
    }

    private boolean objectIsComplete() {
        if (this._gameDataModel == null || this._dataModel == null) {
            return false;
        }
        return true;
    }

    private void CreateDataModel() {
        this._dataModel = new DataModel();

        sortGuesses(this._gameDataModel);
        calculateGuessNumber(this._gameDataModel);
        createRepresentativeString(this._dataModel, this._gameDataModel);
        this._dataModel.progression = this._dataModel.wrongGuesses.length;
    }

    private void calculateGuessNumber(GameDataModel gameDataModel) {
        _dataModel.guess = gameDataModel.guesses.split(",").length;
    }

    private void createRepresentativeString(DataModel dataModel, GameDataModel gameDataModel) {
        var dash = new StringBuilder();
        for (int i = 0; i < gameDataModel.correctWord.length(); i++) {
            dash.append("-");
        }
        var repString = dash.toString();

        for(int dashIndex = 0; dashIndex < repString.length(); dashIndex++) {
            for (var guess :
                    dataModel.correctGuesses) {
                if(guess == gameDataModel.correctWord.toUpperCase().charAt(dashIndex)) {
                    repString = this._utilityService.replaceCharAtIndex(repString, dashIndex, guess);
                    break;
                }
            }
        }

        this._dataModel.representativeString = repString;
    }


    public DataModel writeDataModel() {

        CreateDataModel();
        //create dataModel private
        if (!objectIsComplete()) {
            return null;
        }
        return _dataModel;
    }
}
