package org.activeacademy.portal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.activeacademy.portal.utils.MessagingService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SplashScreen is the launcher activity of an application which is displayed for a short interval.
 * It typically shows the application's logo, name, version number of something similar while the
 * required resources are being loaded in the background.
 */
public class SplashScreen extends AppCompatActivity {

    /**
     * Milliseconds to wait before advancing to next activity.
     */
    private final static long delay = 1500L;

    /**
     * The timer task which starts the next activity when run.
     */
    private final StartActivityTask startActivityTask;

    /**
     * Default constructor.
     */
    public SplashScreen() {
        startActivityTask = new StartActivityTask(LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer timer = new Timer();
        timer.schedule(startActivityTask, delay);

        Intent i = new Intent(this, MessagingService.class);
        startService(i);

    }

    @Override
    public void onBackPressed() {
        // No-op. Disable back button!
    }

    /**
     * StartActivityTask closes the current activity and starts a new one. It is a timer task which
     * can be scheduled to run after a specified delay.
     */
    private class StartActivityTask extends TimerTask {

        /**
         * The activity to start when this task is run.
         */
        private final Class<? extends Activity> activity;

        /**
         * Default constructor.
         *
         * @param activity the activity to start when this task is run.
         */
        private StartActivityTask(Class<? extends Activity> activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), activity));
            finish();
        }
    }
}