package tech.beryllium;

import tech.beryllium.services.DataService;

import javax.xml.crypto.Data;

public class GameController {
    private DataService _dataService;

    public GameController(DataService dataService) { //TODO: add data service paremeter
        this._dataService = dataService;
    }
}
