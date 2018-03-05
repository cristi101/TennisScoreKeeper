package eu.baboi.cristian.tennisscorekeeper.listeners;

import android.view.View;

import eu.baboi.cristian.tennisscorekeeper.TennisController;

/**
 * Created by cristi on 28.02.2018.
 */


public final class AceListener implements View.OnClickListener {

    private final TennisController mTennisController; // reference to the main activity
    private final int mPlayer; // the player

    // The constructor remembers the main activity class
    public AceListener(TennisController tennisController, int player) {
        mTennisController = tennisController;
        mPlayer = player;
    }

    // Record an Ace
    @Override
    public void onClick(View v) {
        mTennisController.ace(mPlayer);
    }
}
