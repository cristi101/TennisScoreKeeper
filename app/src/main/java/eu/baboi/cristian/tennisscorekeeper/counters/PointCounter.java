package eu.baboi.cristian.tennisscorekeeper.counters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import eu.baboi.cristian.tennisscorekeeper.R;

/**
 * Created by cristi on 28.02.2018.
 */

public final class PointCounter implements Counter {

    // The keys for saving the point counters
    private final static String POINTS_PLAYER_A = "POINTS_A";
    private final static String POINTS_PLAYER_B = "POINTS_B";
    private final static String TIE_BREAK = "TIE";
    private final static String WINNER = "WINNER";

    // Constants
    private final static int TIE = 0;
    private final static int DEUCE = 1;
    private final static int POINTS = 2;

    // The player names
    private final String[] names = {"", ""};

    // The winner messages
    private final int[] messages = {R.string.win_game, R.string.win_set, R.string.win_match};

    // The labels for the tvGameInfo TextView
    private final String[] labels = {"Tie", "Deuce", "Points"};

    // The mapping from points to display value
    private final String[] mapping = {"love", "15", "30", "40"};

    // The game counters
    private final Counter games;

    // Point counters for the two players
    private final int[] points = {0, 0};

    // True if we are in a tie-break game
    private boolean tie;

    // Reference to the interface
    private final TextView[] mtvPoints;
    private final TextView mtvGameInfo;

    // The winner message and the dialog box
    private String message;
    private Dialog dialog;

    // The constructor
    public PointCounter(TextView[] tvPoints, TextView tvGameInfo, Counter gameCounter) {
        mtvPoints = tvPoints;
        mtvGameInfo = tvGameInfo;
        games = gameCounter;
        getPlayerNames();
        getLabels();
        reset();
    }

    // The Counter interface methods

    // Score a point for the player
    @Override
    public int increment(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        points[player]++;
        if (displayPointCounters()) {// Update the display and check if end of game
            int code = games.increment(player); // count a new game for the current player
            winner(code, player); // Announce the winner
            reset(); //Start a new game
            return code + 1;
        }
        return 0;
    }

    // Retrieve the current value of the counters
    @Override
    public int value(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        return points[player];
    }

    // Reset the point counters - the order of resetting the counters matter
    public void reset() {
        tie = isTieBreak();
        points[0] = 0;
        points[1] = 0;
        displayPointCounters();
    }

    // Restore the counters to the previous value
    @Override
    public void undo() {
        // TO DO
    }

    // Error - There is more state to save&restore

    // Save the counter state
    public void save(Bundle outState) {

        // Save the winner dialog message
        if (message != null && dialog != null) { // The dialog box is open
            outState.putString(WINNER, message);
            dialog.dismiss(); // Destroy the dialog box when rotating the screen
            dialog = null;
            message = null;
        }

        // Save the tie-break state
        outState.putBoolean(TIE_BREAK, tie);

        // Save the number of points scored
        outState.putInt(POINTS_PLAYER_A, points[0]);
        outState.putInt(POINTS_PLAYER_B, points[1]);
    }

    // Restore the counter state
    public void restore(Bundle savedInstanceState) {

        // Restore the winner message
        message = savedInstanceState.getString(WINNER);
        if (message != null) displayWinner(mtvGameInfo.getContext());

        // Retrieve the tie-break state
        tie = savedInstanceState.getBoolean(TIE_BREAK);

        // Retrieve the number of points scored
        points[0] = savedInstanceState.getInt(POINTS_PLAYER_A);
        points[1] = savedInstanceState.getInt(POINTS_PLAYER_B);

        // Display the counters
        displayPointCounters();
    }

    // Various helper methods

    // Called to update the display.
    // Return true if we are at the end of the game
    private boolean displayPointCounters() {
        // find the min and max of the counters
        int min = points[0];
        int max = points[1];
        if (min > max) {
            max = points[0];
            min = points[1];
        }
        // calculate the point difference
        int delta = max - min;
        if (tie) {// This is a tie game
            mtvGameInfo.setText(labels[TIE]);
            mtvPoints[0].setText(String.valueOf(points[0]));
            mtvPoints[1].setText(String.valueOf(points[1]));
            if (max >= 7 && delta >= 2) { // check if this is the end of the tie-break game
                return true;
            }
        } else { // This is a normal game

            if (max < 4) { // This is the normal play
                if (min == 3 && max == 3) mtvGameInfo.setText(labels[DEUCE]);
                else mtvGameInfo.setText(labels[POINTS]);
                mtvPoints[0].setText(mapping[points[0]]);
                mtvPoints[1].setText(mapping[points[1]]);
            } else { // This is the end play

                switch (delta) {
                    case 0: // This is the Deuce state
                        mtvGameInfo.setText(labels[DEUCE]);
                        mtvPoints[0].setText("-");
                        mtvPoints[1].setText("-");
                        break;
                    case 1: // This is the Adv state
                        int adv = (points[0] == max) ? 0 : 1;
                        mtvGameInfo.setText(labels[POINTS]);
                        mtvPoints[adv].setText("Adv");
                        mtvPoints[1 - adv].setText("-");
                        break;
                    default: // This is the end of the game
                        int winner = (points[0] == max) ? 0 : 1;
                        mtvGameInfo.setText(labels[POINTS]);
                        mtvPoints[winner].setText("G");
                        mtvPoints[1 - winner].setText("-");
                        return true;
                }
            }
        }
        return false;
    }

    // Return true if the parent is in a tie
    private boolean isTieBreak() {
        return games.value(0) == 6 && games.value(1) == 6;
    }

    // Called to announce the winner of the current game
    private void winner(int code, int player) {
        Context context = mtvGameInfo.getContext();
        message = context.getString(messages[code], names[player]);
        displayWinner(context);
    }

    // This show a dialog with the message from the message field
    private void displayWinner(Context context) {
        dialog = new Dialog(context);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        window.setDimAmount(0); // No display dimming
        window.setBackgroundDrawableResource(R.drawable.border); //Rounded border with white background
        window.requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog);

        TextView tvMessage = dialog.findViewById(R.id.message);
        Button okButton = dialog.findViewById(R.id.ok);
        tvMessage.setText(message);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
                message = null;
            }
        });
        dialog.show();
    }

    // Retrieve the labels from the resources
    private void getLabels() {
        Context context = mtvGameInfo.getContext();
        labels[TIE] = context.getString(R.string.tie);
        labels[POINTS] = context.getString(R.string.points);
    }

    // Retrieve the player names from the resources
    private void getPlayerNames() {
        // Find the names of the players
        Context context = mtvGameInfo.getContext();
        names[0] = context.getString(R.string.player_a);
        names[1] = context.getString(R.string.player_b);
    }
}
