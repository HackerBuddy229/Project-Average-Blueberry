package tech.beryllium.hangman_bluebarry.Services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class ErrorService {
    public static void displayNetworkError (Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("error")
                .setMessage("you have experienced a network error: " + errorMessage);
        builder.create().show();
    }
}
