package eu.baboi.cristian.tennisscorekeeper.counters;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by cristi on 28.02.2018.
 */

public final class SetCounter implements Counter {

    // The keys for saving the set counters
    private final static String SETS_PLAYER_A = "SETS_A";
    private final static String SETS_PLAYER_B = "SETS_B";

    // Set counters for the two players
    private final int[] sets = {0, 0};

    // Reference to the interface
    private final TextView[] mtvSets;

    // The constructor
    public SetCounter(TextView[] tvSets) {
        mtvSets = tvSets;
        reset();
    }

    // The counter interface methods

    // count a set for the player
    @Override
    public int increment(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        sets[player]++;
        displaySetCounter(player);
        if (isWinner(player)) return 1; // Check if we have a winner and announce him
        return 0;
    }


    // Retrieve the current value of the counter
    @Override
    public int value(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        return sets[player];
    }

    // Reset the counters
    @Override
    public void reset() {
        sets[0] = 0;
        sets[1] = 0;
        displaySetCounter(0);
        displaySetCounter(1);
    }

    // Restore the counters to the previous state
    @Override
    public void undo() {
        // TO DO
    }

    // Store the counters state
    public void save(Bundle outState) {
        // Save the number of sets scored
        outState.putInt(SETS_PLAYER_A, sets[0]);
        outState.putInt(SETS_PLAYER_B, sets[1]);
    }

    // Restore the counters state
    public void restore(Bundle savedInstanceState) {

        // Retrieve the number of sets scored
        sets[0] = savedInstanceState.getInt(SETS_PLAYER_A);
        sets[1] = savedInstanceState.getInt(SETS_PLAYER_B);

        // Display the counters
        displaySetCounter(0);
        displaySetCounter(1);
    }

    // Various helper methods

    // Display the counter on the screen
    private void displaySetCounter(int player) {
        mtvSets[player].setText(String.valueOf(sets[player]));
    }

    // Check if we have a winner
    private boolean isWinner(int player) {
        return sets[player] >= 3;
    }
}
