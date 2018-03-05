package eu.baboi.cristian.tennisscorekeeper.counters;

import android.os.Bundle;

/**
 * Created by cristi on 01.03.2018.
 */

public interface Counter {
    int increment(int player); // Increment the counter
    int value(int player); // return the current value
    void reset(); // set the counter to 0
    void undo(); // restore the counter to previous value
    void save(Bundle outState); // save the counter state
    void restore(Bundle savedInstanceState); // restore the counter state
}
