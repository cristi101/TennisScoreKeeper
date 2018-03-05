package eu.baboi.cristian.tennisscorekeeper.counters;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by cristi on 01.03.2018.
 */

public final class FaultCounter implements Counter {

    // The keys for saving the double faults counters
    private final static String FAULTS_PLAYER_A = "FAULTS_A";
    private final static String FAULTS_PLAYER_B = "FAULTS_B";
    private final static String R_FAULTS_PLAYER_A = "R_FAULTS_A";
    private final static String R_FAULTS_PLAYER_B = "R_FAULTS_B";

    // Double fault counters for the two players
    private final int[] faults = {0, 0};
    private final int[] retained = {0, 0};

    // Reference to the interface
    private final TextView[] mtvFaults;

    // The constructor
    public FaultCounter(TextView[] tvFaults) {
        mtvFaults = tvFaults;
        reset();
    }

    // The counter interface methods

    // Count a double fault for the player
    @Override
    public int increment(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        faults[player]++;
        displayFaultCounter(player);
        return 0;
    }

    // Retrieve the current value of the counters
    @Override
    public int value(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        return faults[player];
    }

    // Reset the counters
    @Override
    public void reset() {
        faults[0] = 0;
        faults[1] = 0;
        displayFaultCounter(0);
        displayFaultCounter(1);
    }

    // Retain the current value of the counters
    @Override
    public void retain() {
        retained[0] = faults[0];
        retained[1] = faults[1];
    }

    // Restore the counters to the previous value
    @Override
    public void undo() {
        faults[0] = retained[0];
        faults[1] = retained[1];
        displayFaultCounter(0);
        displayFaultCounter(1);
    }

    // Save the counters state
    public void save(Bundle outState) {
        // Save the number of double faults scored
        outState.putInt(FAULTS_PLAYER_A, faults[0]);
        outState.putInt(FAULTS_PLAYER_B, faults[1]);

        outState.putInt(R_FAULTS_PLAYER_A, retained[0]);
        outState.putInt(R_FAULTS_PLAYER_B, retained[1]);
    }

    // Restore the counters state
    public void restore(Bundle savedInstanceState) {
        // Retrieve the number of double faults scored
        faults[0] = savedInstanceState.getInt(FAULTS_PLAYER_A);
        faults[1] = savedInstanceState.getInt(FAULTS_PLAYER_B);

        retained[0] = savedInstanceState.getInt(R_FAULTS_PLAYER_A);
        retained[1] = savedInstanceState.getInt(R_FAULTS_PLAYER_B);
        // Display the counters
        displayFaultCounter(0);
        displayFaultCounter(1);
    }

    // Various helper methods

    // Update the double fault counter on the interface
    private void displayFaultCounter(int player) {
        mtvFaults[player].setText(String.valueOf(faults[player]));
    }
}
