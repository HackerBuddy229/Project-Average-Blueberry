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
    /**
     * this constructor sets up the instance with an id so that you can start the stack with getGameDataModel()
     * @param id the aforementioned id
     * @throws IOException when network connectivity fails or the id is corrupt leading to a url malformation
     */
    public DataService(int  id) throws IOException {
        //TODO: verify id
        this.id = id;
        this.getUrl = new URL(String.format("https://beryllium.tech/api/HangmanGameModels/" + this.id));

        this.postUrl = new URL("https://beryllium.tech/api/HangmanGameModels");



        this.gson = new Gson();
    }

    /**
     * instantiate without id meaning you can't begin the stack with getGameDataModel()
     * @throws MalformedURLException should not occur if not code changes are applied
     */
    public DataService() throws MalformedURLException {
        this.postUrl = new URL("https://beryllium.tech/api/HangmanGameModels");
        this.gson = new Gson();

    }


    private int id;
    private URL getUrl;
    private URL postUrl;
    private Gson gson;

    /**
     * returns a gamedatamodel fetched from the hangmanGameModels api hosted on beryllium.tech
     * @return an updated gameDataModel
     * @throws IOException at network connectivity issues or id malformation
     */
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

    /**
     * Gets raw json by the global get url
     * @return a string consisting of raw json in the form of a gameDataModel
     * @throws IOException at network connectivity issues of id malformation
     */
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

    /**
     * mapps a raw json string to an gamedatamodel instance
     * @param json raw json in the form of gameDataModel
     * @return an unsecured instance of the gameDataModel
     */
    private GameDataModel ToGameDataModel(String json) {
        GameDataModel output = this.gson.fromJson(json, GameDataModel.class);
        return output;
    }

    /**
     * mapps an instance of the gameModel to raw json
     * @param gameModel the gameModel to be mapped
     * @return a string of raw json representing an gamemodel object
     */
    private String gameModelToJson(GameModel gameModel) {
        String rawJson = this.gson.toJson(gameModel);
        return rawJson;
    }

    /**
     * maps an instance of the gameDataModel to raw json
     * @param gameModel the gameDataModel to be mapped
     * @return a string of raw json representing an gamedatamodel object
     */
    private String gameDataModelToJson(GameDataModel gameModel) {
        String rawJson = this.gson.toJson(gameModel);
        return rawJson;
    }

    /**
     * Creates a post request and executes it, sendig raw json to the REST api
     * @param json the raw json sent
     * @return  raw json representing the gameDataModel
     * @throws IOException on network connectivity or general http errors
     */
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

    /**
     * Creates a put request and executes it, sendig raw json to the REST api
     * @param json the raw json sent
     * @return  raw json representing the gameDataModel
     * @throws IOException on network connectivity or general http errors
     */
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

    /**
     * general purpose publicly accessible method to send and receive an gamedatamodel to the rest api.
     * can automatically determine weather or not an Put or Post is necessary
     * @param gameModel the GameDataModel to be sent
     * @return returns the updated GameDataModel
     * @throws IOException on network connectivity issues or general http errors
     */
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
            this.getUrl = new URL(String.format("https://beryllium.tech/api/HangmanGameModels/" + this.id));
        }
        //from json
        return  returnModel;
        //return

    }

    //TODO: add finalizer to delete db entry

}
