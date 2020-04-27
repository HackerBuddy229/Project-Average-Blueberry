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
        this.getUrl = new URL(String.format("https://beryllium.tech/api/HangmanGameModels/%x", id));

        this.postUrl = new URL("https://beryllium.tech/api/HangmanGameModels");

        this.getHttpURLConnection = (HttpURLConnection) this.getUrl.openConnection();
        this.getHttpURLConnection.setRequestMethod("GET");

        this.gson = new Gson();
    }

    public DataService() {

    }


    private int id;
    private URL getUrl;
    private URL postUrl;
    private HttpURLConnection getHttpURLConnection;
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
        //TODO: impliment type security


        //return
        return output;
    }

    private String getJsonByURL() throws IOException {
        var stringBuilder = new StringBuilder();

        var buffReader = new BufferedReader(new InputStreamReader(this.getHttpURLConnection.getInputStream()));
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

    private String ToJson(GameModel gameModel) {
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
        var connection = (HttpURLConnection) this.postUrl.openConnection();
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

    private GameDataModel postGameModel(GameModel gameModel) throws IOException {
        var json = ToJson(gameModel);
        //to json
        String response;
        if (id == 0) {
            response = postJson(json);
        } else {
            response = putJson(json);
        }
        //post
        GameDataModel returnModel = null;
        if (response != null || response != "") {
            returnModel = ToGameDataModel(response);
        }
        //from json
        return  returnModel;
        //return

    }

}
