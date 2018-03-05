package eu.baboi.cristian.tennisscorekeeper.counters;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by cristi on 28.02.2018.
 */

public final class GameCounter implements Counter {

    // The keys for saving the game counters
    private final static String GAMES_PLAYER_A = "GAMES_A";
    private final static String GAMES_PLAYER_B = "GAMES_B";
    private final static String RECORD_PLAYER_A = "RECORD_A";
    private final static String RECORD_PLAYER_B = "RECORD_B";

    // The set counters
    private final Counter sets;

    // Game counters for the two players
    private final int[] games = {0, 0};
    private final String[] record = {"", ""};

    // Reference to the interface
    private final TextView[] mtvGames;
    private final TextView[] mtvSetRecord;

    // The constructor
    public GameCounter(TextView[] tvGames, TextView[] tvSetRecord, Counter setCounter) {
        mtvGames = tvGames;
        mtvSetRecord = tvSetRecord;
        sets = setCounter;
        reset();
    }

    // The counter interface methods

    // Score a game for the player
    @Override
    public int increment(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        games[player]++;
        displayGameCounter(player);
        if (isWinner(player)) {// Check if we have a winner
            recordScore(); // Record the set won
            int code = sets.increment(player); // count a new set for the player
            reset(); // Start a new set
            return code + 1;
        }
        return 0;
    }

    // Retrieve the current counter value
    @Override
    public int value(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        return games[player];
    }

    // Reset the counters
    @Override
    public void reset() {
        games[0] = 0;
        games[1] = 0;
        if (isNewMatch()) {
            record[0] = "";
            record[1] = "";
            displayRecord();
        }
        displayGameCounter(0);
        displayGameCounter(1);
    }

    // Restore the previous value of the counters
    @Override
    public void undo() {
        // TO DO
    }

    // Save the counters state
    public void save(Bundle outState) {
        // Save the number of games scored
        outState.putInt(GAMES_PLAYER_A, games[0]);
        outState.putInt(GAMES_PLAYER_B, games[1]);

        // Save the record of the sets played
        outState.putString(RECORD_PLAYER_A, record[0]);
        outState.putString(RECORD_PLAYER_B, record[1]);
    }

    // Restore the counters state
    public void restore(Bundle savedInstanceState) {

        // Retrieve the number of games scored
        games[0] = savedInstanceState.getInt(GAMES_PLAYER_A);
        games[1] = savedInstanceState.getInt(GAMES_PLAYER_B);

        // Retrieve the set record
        record[0] = savedInstanceState.getString(RECORD_PLAYER_A);
        record[1] = savedInstanceState.getString(RECORD_PLAYER_B);

        // Display the record
        displayRecord();

        // Display the counters
        displayGameCounter(0);
        displayGameCounter(1);
    }

    // Various helper methods follow

    // Update the counters on the screen
    private void displayGameCounter(int player) {
        mtvGames[player].setText(String.valueOf(games[player]));
    }

    // Check if we have a winner
    private boolean isWinner(int player) {
        int games0 = games[player];
        int games1 = games[1 - player];
        return (games0 >= 6 && games0 - games1 >= 2) || (games0 == 7 && games1 == 6);
    }

    // Return true if no set was won
    private boolean isNewMatch() {
        return sets.value(0) + sets.value(1) == 0;
    }

    // Display the set record
    private void displayRecord() {
        mtvSetRecord[0].setText(record[0]);
        mtvSetRecord[1].setText(record[1]);
    }

    // Record the set result
    private void recordScore() {
        record[0] = record[0] + " " + String.valueOf(games[0]);
        record[1] = record[1] + " " + String.valueOf(games[1]);
        displayRecord();
    }
}
