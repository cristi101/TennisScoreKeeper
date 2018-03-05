package eu.baboi.cristian.tennisscorekeeper.listeners;

import android.view.View;

import eu.baboi.cristian.tennisscorekeeper.TennisController;

/**
 * Created by cristi on 28.02.2018.
 */

public final class FaultListener implements View.OnClickListener {

    private final TennisController mTennisController;// reference to the main activity
    private final int mPlayer; // the player

    // The constructor remembers the main activity class
    public FaultListener(TennisController tennisController, int player) {
        mTennisController = tennisController;
        mPlayer = player;
    }

    // Record a double fault
    @Override
    public void onClick(View v) {
        mTennisController.fault(mPlayer);
    }
}
