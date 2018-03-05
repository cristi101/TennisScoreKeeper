package eu.baboi.cristian.tennisscorekeeper;

/**
 * Created by cristi on 01.03.2018.
 */

public interface TennisController {
    void undo(); // Undo the last action
    void reset(); // Start a new match
    void ace(int player); // Record an ace
    void fault(int player); // Count a double fault
    void point(int player); // Record a point
}
