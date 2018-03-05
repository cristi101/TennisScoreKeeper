package eu.baboi.cristian.tennisscorekeeper.listeners;

import android.view.View;

import eu.baboi.cristian.tennisscorekeeper.TennisController;

/**
 * Created by cristi on 28.02.2018.
 */

public final class UndoListener implements View.OnClickListener {

    private final TennisController mTennisController; // reference to the main activity

    // The constructor remembers the main activity class
    public UndoListener(TennisController tennisController) {
        mTennisController = tennisController;
    }

    // Undo the last action
    @Override
    public void onClick(View v) {
        mTennisController.undo();
    }
}
