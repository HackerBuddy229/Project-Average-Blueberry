package tech.beryllium.hangman_bluebarry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import java.io.IOException;

import tech.beryllium.hangman_bluebarry.Controllers.GameController;
import tech.beryllium.hangman_bluebarry.Services.ErrorService;
import tech.beryllium.hangman_bluebarry.models.MainActivityModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityModel mainActivityModel;
    private GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivityModel();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mainActivityModel.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame(fetchContext());
            }
        });
    }

    private void playGame(Context context) {
        this.mainActivityModel.refresh.setText("Refresh");
        setupGame(this);
        enableGame();
    }

    public Context fetchContext() {
        return this;
    }

    private void setupGame(Context context) {
        this.gameController = new GameController(this, this.mainActivityModel);
        this.gameController.setupGame();


    }

    private void enableGame() {
        this.mainActivityModel.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gameController.iterateGame();
                } catch (IOException e) {
                    ErrorService.displayNetworkError(v.getContext(), e.getMessage());
                }

            }
        });
    }

    private void initMainActivityModel() {
        this.mainActivityModel = new MainActivityModel();

        this.mainActivityModel.ascii = findViewById(R.id.AsciiArt);
        this.mainActivityModel.guess = findViewById(R.id.Guess);
        this.mainActivityModel.hints = findViewById(R.id.textView4);
        this.mainActivityModel.refresh = findViewById(R.id.Refresh);
        this.mainActivityModel.stats = findViewById(R.id.textView3);
        this.mainActivityModel.submit = findViewById(R.id.Submit);

    }
}
