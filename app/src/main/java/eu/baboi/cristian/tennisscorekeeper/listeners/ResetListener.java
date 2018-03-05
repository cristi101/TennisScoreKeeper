package eu.baboi.cristian.tennisscorekeeper.listeners;

import android.view.View;

import eu.baboi.cristian.tennisscorekeeper.TennisController;

/**
 * Created by cristi on 28.02.2018.
 */

public final class ResetListener implements View.OnClickListener {

    private final TennisController mTennisController; // reference to the main activity

    // The constructor remembers the main activity class
    public ResetListener(TennisController tennisController) {
        mTennisController = tennisController;
    }

    // Start a new match
    @Override
    public void onClick(View v) {
        mTennisController.reset();
    }
}
