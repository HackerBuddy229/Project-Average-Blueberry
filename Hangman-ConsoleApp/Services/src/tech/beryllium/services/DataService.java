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

        this.getHttpURLConnection = (HttpURLConnection) this.getUrl.openConnection();
        this.getHttpURLConnection.setRequestMethod("GET");
    }

    public DataService() {

    }


    private int id;
    private URL getUrl;
    private HttpURLConnection getHttpURLConnection;

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
        GameDataModel output = new Gson().fromJson(json, GameDataModel.class);
        return output;
    }


}
