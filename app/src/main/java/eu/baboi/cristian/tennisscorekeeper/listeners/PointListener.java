package eu.baboi.cristian.tennisscorekeeper.listeners;

import android.view.View;

import eu.baboi.cristian.tennisscorekeeper.TennisController;

/**
 * Created by cristi on 28.02.2018.
 */

public final class PointListener implements View.OnClickListener {

    private final TennisController mTennisController; // reference to the main activity
    private final int mPlayer; // the player

    // The constructor remembers the main activity class
    public PointListener(TennisController tennisController, int player) {
        mTennisController = tennisController;
        mPlayer = player;
    }


    // Record a point
    @Override
    public void onClick(View v) {
        mTennisController.point(mPlayer);
    }
}
