package eu.baboi.cristian.tennisscorekeeper.counters;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by cristi on 01.03.2018.
 */

public final class AceCounter implements Counter {

    // The keys for saving the aces counters
    private final static String ACES_PLAYER_A = "ACES_A";
    private final static String ACES_PLAYER_B = "ACES_B";

    // Ace counters for the two players
    private final int[] aces = {0, 0};

    // Reference to the interface
    private final TextView[] mtvAces;

    // The constructor
    public AceCounter(TextView[] tvAces) {
        mtvAces = tvAces;
        reset();
    }

    // The counter interface methods

    // Score an ace for the player
    @Override
    public int increment(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        aces[player]++;
        displayAceCounter(player);
        return 0;
    }

    // Retrieve the current value of the counters
    @Override
    public int value(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        return aces[player];
    }

    // Reset the counters
    @Override
    public void reset() {
        aces[0] = 0;
        aces[1] = 0;
        displayAceCounter(0);
        displayAceCounter(1);
    }

    // Restore the counters to the previous value
    @Override
    public void undo() {
        // TO DO
    }

    // Save the state of the counters
    public void save(Bundle outState) {
        // Save the number of aces scored
        outState.putInt(ACES_PLAYER_A, aces[0]);
        outState.putInt(ACES_PLAYER_B, aces[1]);
    }

    // Restore the state of the counters
    public void restore(Bundle savedInstanceState) {
        // Retrieve the number of aces scored
        aces[0] = savedInstanceState.getInt(ACES_PLAYER_A);
        aces[1] = savedInstanceState.getInt(ACES_PLAYER_B);
        // Display the counters
        displayAceCounter(0);
        displayAceCounter(1);
    }

    // Various helper methods

    // Update the ace counter on the interface
    private void displayAceCounter(int player) {
        mtvAces[player].setText(String.valueOf(aces[player]));
    }
}
