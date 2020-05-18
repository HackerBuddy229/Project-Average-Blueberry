package tech.beryllium.hangman_bluebarry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tech.beryllium.hangman_bluebarry.models.MainActivityModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityModel mainActivityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initMainActivityModel() {
        this.mainActivityModel.ascii = findViewById(R.id.AsciiArt);
        this.mainActivityModel.guess = findViewById(R.id.Guess);
        this.mainActivityModel.hints = findViewById(R.id.textView4);
        this.mainActivityModel.refresh = findViewById(R.id.Refresh);
        this.mainActivityModel.stats = findViewById(R.id.textView3);
        this.mainActivityModel.submit = findViewById(R.id.Submit);

    }
}
