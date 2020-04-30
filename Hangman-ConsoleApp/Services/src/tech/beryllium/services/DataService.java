package tech.beryllium.services;

import com.google.gson.Gson;
import tech.beryllium.models.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataService {

    public DataService(int  id) throws IOException {
        //TODO: verify id
        this.id = id;
        this.getUrl = new URL(String.format("https://beryllium.tech/api/HangmanGameModels/%x", this.id));

        this.postUrl = new URL("https://beryllium.tech/api/HangmanGameModels");



        this.gson = new Gson();
    }

    public DataService() throws MalformedURLException {
        this.postUrl = new URL("https://beryllium.tech/api/HangmanGameModels");
        this.gson = new Gson();

    }


    private int id;
    private URL getUrl;
    private URL postUrl;
    private Gson gson;

    public GameDataModel getGameDataModel() throws IOException {

        if (id == 0) {
            throw new IllegalStateException("Initialize with id if you want to begin with getGameModel()");
        }

        //get request
        var rawJson = getJsonByURL();
        if(rawJson == null) {
            throw new IOException("Failed to fetch item");
        }


        //json
        var output = ToGameDataModel(rawJson);



        //return
        return output;
    }

    private String getJsonByURL() throws IOException {
        var getHttpURLConnection = (HttpURLConnection) this.getUrl.openConnection();
        getHttpURLConnection.setRequestMethod("GET");

        var stringBuilder = new StringBuilder();

        var buffReader = new BufferedReader(new InputStreamReader(getHttpURLConnection.getInputStream()));

        String line;

        while ((line = buffReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();

    }

    private GameDataModel ToGameDataModel(String json) {
        GameDataModel output = this.gson.fromJson(json, GameDataModel.class);
        return output;
    }

    private String gameModelToJson(GameModel gameModel) {
        String rawJson = this.gson.toJson(gameModel);
        return rawJson;
    }

    private String gameDataModelToJson(GameDataModel gameModel) {
        String rawJson = this.gson.toJson(gameModel);
        return rawJson;
    }

    private String postJson(String json) throws IOException {
        var connection = (HttpURLConnection) this.postUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        var jsonBytes = json.getBytes("utf-8");
        connection.getOutputStream().write(jsonBytes, 0, jsonBytes.length);

        var buffredReader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(),
                        "utf-8"));
        String line;
        var response = new StringBuilder();
        while ((line = buffredReader.readLine()) != null) {
            response.append(line);
        }



        return response.toString();

    }

    private String putJson(String json) throws IOException {
        var connection = (HttpURLConnection) this.getUrl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        var jsonBytes = json.getBytes("utf-8");
        connection.getOutputStream().write(jsonBytes, 0, jsonBytes.length);

        var buffredReader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(),
                        "utf-8"));
        String line;
        var response = new StringBuilder();
        while ((line = buffredReader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    public GameDataModel postGameModel(GameDataModel gameModel) throws IOException {

        //to json
        String response;
        if (id == 0) {
            response = postJson(gameModelToJson(gameModel.toGameModel()));
        } else {
            response = putJson(gameDataModelToJson(gameModel));
        }
        //post
        GameDataModel returnModel = new GameDataModel();
        if (response != null || response != "") {
            returnModel = ToGameDataModel(response);
        }

        if (id == 0) {
            this.id = returnModel.id;
            this.getUrl = new URL(String.format("https://beryllium.tech/api/HangmanGameModels/%x", this.id));
        }
        //from json
        return  returnModel;
        //return

    }

    //TODO: add finalizer to delete db entry

}
