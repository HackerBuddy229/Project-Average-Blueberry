package tech.beryllium.hangman_bluebarry.Services;

import java.util.ArrayList;

import tech.beryllium.hangman_bluebarry.models.DataModel;
import tech.beryllium.hangman_bluebarry.models.GameDataModel;

public class DataModelBuilder {
    private GameDataModel _gameDataModel;
    private DataModel _dataModel;

    private UtilityService _utilityService;

    /**
     * uses a gamedatamodel to create a wready to write instance
     * @param gameDataModel a not null gamedatamodel
     */
    public DataModelBuilder(GameDataModel gameDataModel) {
        this._gameDataModel = gameDataModel;
        this._utilityService = new UtilityService();
    }

    /**
     * creates an instance in need of a gamedatamodel to be able to write
     */
    public DataModelBuilder() {
        this._utilityService = new UtilityService();
    }

    public void setGameDataModel(GameDataModel _gameDataModel) {
        this._gameDataModel = _gameDataModel;
    }

    /**
     * Sorts the guesses contained in the gamedatamodel into two arrays differed by their relation to the correct word
     * @param gameDataModel the aforementioned gamedatamodel
     */
    private void sortGuesses(GameDataModel gameDataModel) {
        ArrayList correctList = new ArrayList<Character>();
        ArrayList wrongList = new ArrayList<Character>();

        for (String guess: gameDataModel.guesses.split(",")) {
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

    /**
     * A really stupid and slow meathod that converts a Character arraylist to a character array
     * since java is a bad programming language
     * @param input the arraylist of characters
     * @return the mapped array representing the arraylist
     */
    private Character[] characterCollectionToArray(ArrayList<Character> input) {
        Character[] array = new Character[input.size()];
        for (int index = 0; index < array.length; index++) {
            array[index] = input.get(index);
        }
        return array;
    }

    /**
     * Checks weather or not the current instance is ready to write a DataModel
     * @return weather or not the current instance is ready
     */
    private boolean objectIsComplete() {
        if (this._gameDataModel == null || this._dataModel == null) {
            return false;
        }
        return true;
    }

    /**
     * The first step of the writeDataModel() both instanciates the global datamodel
     * but also uses the global gamedatamodel to calculate the contained values of the dataModel
     */
    private void CreateDataModel() {
        this._dataModel = new DataModel();

        sortGuesses(this._gameDataModel);
        calculateGuessNumber(this._gameDataModel);
        createRepresentativeString(this._dataModel, this._gameDataModel);
        this._dataModel.progression = this._dataModel.wrongGuesses.length;
    }

    /**
     * Calculates the amount of guesses that has been made
     * !Is deprecated(Not in use)
     * @param gameDataModel the global gamedatamodel should be used to calculate the amount of guesses made
     */
    private void calculateGuessNumber(GameDataModel gameDataModel) {
        _dataModel.guess = gameDataModel.guesses.split(",").length;
    }

    /**
     * Creates a representative string of the current guessing progress, in which not yet guessed characters take
     * the place of "-":s
     * @param dataModel the global data model is used to handle only the correct guesses to save time
     * @param gameDataModel the global gamedatamodel should be used to fetch the correct word
     */
    private void createRepresentativeString(DataModel dataModel, GameDataModel gameDataModel) {
        StringBuilder dash = new StringBuilder();
        for (int i = 0; i < gameDataModel.correctWord.length(); i++) {
            dash.append("-");
        }
        String repString = dash.toString();

        for(int dashIndex = 0; dashIndex < repString.length(); dashIndex++) {
            for (char guess :
                    dataModel.correctGuesses) {
                if(guess == gameDataModel.correctWord.toUpperCase().charAt(dashIndex)) {
                    repString = this._utilityService.replaceCharAtIndex(repString, dashIndex, guess);
                    break;
                }
            }
        }

        this._dataModel.representativeString = repString;
    }

    /**
     * executes the writeDataModel() chain and checks it's validity before returning the product
     * @return the produced DataModel
     */
    public DataModel writeDataModel() {

        CreateDataModel();
        //create dataModel private
        if (!objectIsComplete()) {
            return null;
        }
        return _dataModel;
    }
}
