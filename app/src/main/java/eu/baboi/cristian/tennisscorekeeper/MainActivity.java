package eu.baboi.cristian.tennisscorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import eu.baboi.cristian.tennisscorekeeper.counters.AceCounter;
import eu.baboi.cristian.tennisscorekeeper.counters.Counter;
import eu.baboi.cristian.tennisscorekeeper.counters.FaultCounter;
import eu.baboi.cristian.tennisscorekeeper.counters.GameCounter;
import eu.baboi.cristian.tennisscorekeeper.counters.PointCounter;
import eu.baboi.cristian.tennisscorekeeper.counters.SetCounter;
import eu.baboi.cristian.tennisscorekeeper.listeners.AceListener;
import eu.baboi.cristian.tennisscorekeeper.listeners.FaultListener;
import eu.baboi.cristian.tennisscorekeeper.listeners.PointListener;
import eu.baboi.cristian.tennisscorekeeper.listeners.ResetListener;
import eu.baboi.cristian.tennisscorekeeper.listeners.UndoListener;

public class MainActivity extends AppCompatActivity implements TennisController {

    // The score counters
    private Counter aceCounter;
    private Counter faultCounter;
    private Counter pointCounter;
    private Counter gameCounter;
    private Counter setCounter;

    // The recording of the set scores
    private TextView[] tvSetRecord;

    // The info label
    private TextView tvGameInfo;

    // The counters on the interface for the two players
    private TextView[] tvPoints;
    private TextView[] tvGames;
    private TextView[] tvSets;
    private TextView[] tvFaults;
    private TextView[] tvAces;

    // The buttons on the interface
    private Button[] btnPoint;
    private Button[] btnAce;
    private Button[] btnFault;
    private Button btnReset;
    private Button btnUndo;

    // The Activity lifecycle methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fixBackground(); // this fix the size of the background picture

        findTextViews(); // this remembers the counter views on the interface
        findButtons();  // this remembers the buttons on the interface

        createCounters(); // this creates the score counters
        wireButtons();  // this connects the click listeners to the buttons
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setCounter.save(outState);// Save the number of sets
        gameCounter.save(outState);// Save the number of games
        pointCounter.save(outState);// Save the number of points
        aceCounter.save(outState);// Save the number of aces scored
        faultCounter.save(outState);// Save the number of double faults scored
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCounter.restore(savedInstanceState);// Retrieve the number of sets
        gameCounter.restore(savedInstanceState);// Retrieve the number of games
        pointCounter.restore(savedInstanceState);// Retrieve the number of points
        aceCounter.restore(savedInstanceState);// Retrieve the number of aces scored
        faultCounter.restore(savedInstanceState);// Retrieve the number of double faults scored
    }

    // The TennisController interface methods

    // Undo the last action - need changes
    public void undo() {
        setCounter.undo();
        gameCounter.undo();
        pointCounter.undo();
        aceCounter.undo();
        faultCounter.undo();
    }

    // Reset the scores
    public void reset() {
        setCounter.reset();
        gameCounter.reset(); // The setCounter must be reset first
        pointCounter.reset();// The gameCounter must be reset first
        aceCounter.reset();
        faultCounter.reset();
    }

    // Record a point for the player
    public void point(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        pointCounter.increment(player);
    }

    // Record an ace for the player
    public void ace(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        aceCounter.increment(player);
        pointCounter.increment(player);
    }

    // Record a double fault for the player
    public void fault(int player) {
        if (player < 0 || player > 1) throw new ArrayIndexOutOfBoundsException();
        faultCounter.increment(player);
        pointCounter.increment(1 - player);
    }

    // Various utility methods

    // Creates the score counters
    private void createCounters() {
        setCounter = new SetCounter(tvSets);
        gameCounter = new GameCounter(tvGames, tvSetRecord, setCounter);
        pointCounter = new PointCounter(tvPoints, tvGameInfo, gameCounter);
        aceCounter = new AceCounter(tvAces);
        faultCounter = new FaultCounter(tvFaults);
    }

    // Find the score counters on the interface
    private void findTextViews() {

        // find the Ace counters on the interface
        tvAces = new TextView[2];
        tvAces[0] = findViewById(R.id.tv_aces_a);
        tvAces[1] = findViewById(R.id.tv_aces_b);

        // find the Double fault counters on the interface
        tvFaults = new TextView[2];
        tvFaults[0] = findViewById(R.id.tv_faults_a);
        tvFaults[1] = findViewById(R.id.tv_faults_b);

        // find the info label
        tvGameInfo = findViewById(R.id.tv_game_info);

        // find the Point counters on the interface
        tvPoints = new TextView[2];
        tvPoints[0] = findViewById(R.id.tv_points_a);
        tvPoints[1] = findViewById(R.id.tv_points_b);

        // find the Game counters on the interface
        tvGames = new TextView[2];
        tvGames[0] = findViewById(R.id.tv_games_a);
        tvGames[1] = findViewById(R.id.tv_games_b);

        // find the Set counters on the interface
        tvSets = new TextView[2];
        tvSets[0] = findViewById(R.id.tv_sets_a);
        tvSets[1] = findViewById(R.id.tv_sets_b);

        // Also find the set record
        tvSetRecord = new TextView[2];
        tvSetRecord[0] = findViewById(R.id.tv_set_record_a);
        tvSetRecord[1] = findViewById(R.id.tv_set_record_b);
    }

    // The methods for the buttons follow

    // Find the buttons on the interface
    private void findButtons() {

        btnPoint = new Button[2];
        btnPoint[0] = findViewById(R.id.point_a);
        btnPoint[1] = findViewById(R.id.point_b);

        btnAce = new Button[2];
        btnAce[0] = findViewById(R.id.ace_a);
        btnAce[1] = findViewById(R.id.ace_b);

        btnFault = new Button[2];
        btnFault[0] = findViewById(R.id.fault_a);
        btnFault[1] = findViewById(R.id.fault_b);

        btnReset = findViewById(R.id.reset);
        btnUndo = findViewById(R.id.undo);
    }

    // Wire the buttons to the click event handlers
    private void wireButtons() {
        btnPoint[0].setOnClickListener(new PointListener(this, 0));
        btnPoint[1].setOnClickListener(new PointListener(this, 1));

        btnAce[0].setOnClickListener(new AceListener(this, 0));
        btnAce[1].setOnClickListener(new AceListener(this, 1));

        btnFault[0].setOnClickListener(new FaultListener(this, 0));
        btnFault[1].setOnClickListener(new FaultListener(this, 1));

        btnReset.setOnClickListener(new ResetListener(this));
        btnUndo.setOnClickListener(new UndoListener(this));
    }


    // Some screen related methods follow

    // This method sets the limits for the background size to the screen size
    private void fixBackground() {

        // Get the display metrics
        final DisplayMetrics metrics = new DisplayMetrics();
        getMetrics(metrics);

        final ImageView background = findViewById(R.id.bkgd);

        // Put bounds to the background size
        background.setMaxWidth(metrics.widthPixels);
        background.setMaxHeight(metrics.heightPixels - (int) (24 * metrics.density));

    }

    /**
     * This is an utility method for getting the screen metrics
     *
     * @param metrics the DisplayMetrics to get
     */
    void getMetrics(DisplayMetrics metrics) {

        // See https://developer.android.com/reference/android/util/DisplayMetrics.html

        // Get the display
        final Display display = getWindowManager().getDefaultDisplay();

        // Get the metrics
        display.getMetrics(metrics);
    }

    /**
     * This is an utility method for detecting the metrics orientation
     *
     * @param metrics The screen metrics to check
     * @return true if metrics are for Landscape orientation, false if not
     */
    boolean isLandscape(DisplayMetrics metrics) {
        return (metrics.widthPixels > metrics.heightPixels);
    }


}
