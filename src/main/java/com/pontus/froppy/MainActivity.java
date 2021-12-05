package com.pontus.froppy;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    //A ProgressDialog object
    private ProgressDialog progressDialog;
    // Our OpenGL Surfaceview
    private GLSurfaceView glSurfaceView;
    // decor view
    private View mDecorView;
    private Intent music;


    private boolean isFirstPause = true;

    LoadViewTask myLoadViewTask;


    private boolean isMusicPlaying = false;
    private boolean mIsBound = false;
    public MusicService mServ;

    private ServiceConnection Scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Super
        super.onCreate(savedInstanceState);

        //doBindService();
        // set initial screen orientation
        initActivityScreenOrientLandscape();

        // hide UI features
        hideSystemUI();

        // We create our Surfaceview for our OpenGL here.
        glSurfaceView = new GLSurf(this);

        // Set our view.
        setContentView(R.layout.activity_main);

        // Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);

        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // Add adView to the bottom of the screen.
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        layout.addView(glSurfaceView, glParams);

        //Initialize a LoadViewTask object and call the execute() method
        launchLoadViewTask();

    }


    public void doBindService() {

        try {
            music = new Intent();
            music.setClass(this, MusicService.class);
            bindService(music, Scon, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        } catch (Exception exp) {
            System.out.println("Exception. Could not bind music service");

        }
    }


    public void doUnbindService() {
        try {
            if (mIsBound) {
                unbindService(Scon);
                mIsBound = false;
            }
        } catch (Exception exp) {

            System.out.println("Exception. Could not UN-Bind music service");
            // exp.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            stopService(music);
            mServ.stopMusic();
            doUnbindService();
        } catch (Exception ex) {

            System.out.println("Exception. Could not Stop and or unbind music service: " + ex.getMessage());
            //ex.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        hideSystemUI();

    }



    public void playMusic() {

        try {
            if (!isMusicPlaying && mIsBound) {

                startService(music);
            }
        } catch (Exception exp) {

            System.out.println("Exception.  Starting Music: " + exp.getMessage());
        }
        try {
            if (mServ != null) {
                mServ.playMusic();
                mServ.setVolumeLevel(0.5f);
                isMusicPlaying = true;
            }
        } catch (Exception exp) {

            System.out.println("Exception.  Starting Music: " + exp.getMessage());
        }

        try {
            if (!mIsBound) {
                doBindService();
            }


        } catch (Exception exp) {
            System.out.println("Exception.  on Play Music: " + exp.getMessage());
        }

    }


    public void setMusicVolume(float volume) {
        try {
            if (volume < 0 || volume > 1) {
                mServ.setVolumeLevel(.5f);
                throw new Exception("Music Volume Exception: volume level should be between 0 and 1");
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            //  e.printStackTrace();

            System.out.println("Exception. music volume exception:" + e.getMessage());
            return;
        }
        mServ.setVolumeLevel(volume);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //System.out.println("MAIN ACTIVITY ON RESUME");
        hideSystemUI();
        glSurfaceView.onResume();
        if (!mIsBound && mServ != null) {
            mServ.resumeMusic();
            mIsBound = true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {

        initActivityScreenOrientLandscape();
        hideSystemUI();
    }


    // manage initial screen settings
    private void initActivityScreenOrientLandscape() {

        // 	Avoid screen rotations
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        // Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set window fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Test if it is VISUAL in landscape mode by simply checking it's size
        boolean bIsVisualLandscape = (metrics.heightPixels <= metrics.widthPixels);
        if (!bIsVisualLandscape) {
            // Swap the orientation to match the VISUAL portrait mode
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        } else {
            //IF visually on portrait, lock screen orientation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            mDecorView.setSystemUiVisibility(View.GONE);
        }
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            mDecorView.setSystemUiVisibility(View.VISIBLE);
        }
    }



    public void launchLoadViewTask() {
        myLoadViewTask = null;
        myLoadViewTask = new LoadViewTask();
        myLoadViewTask.execute();
    }

    // CLASS CONSTRUCTOR
    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Froppy Snack time by Pontus",
                    "Please wait. Loading...  \n" +
                            " All content (including but not limited to: music, sounds and images)" +
                            " is copyrighted by Pontus", false, false);
        }

        //The code to be executed in a old_background thread.
        @Override
        protected Void doInBackground(Void... params) {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a old_background
             * thread must be placed.
             */
            try {
                //Get the current thread's token

                synchronized (this) {

                    //Initialize an integer (that will act as a counter) to zero


                        int counter=0;

                        while (counter < 4 ) {
                            // Wait 500 milliseconds
                            this.wait(500);
                            counter++;
                            publishProgress(counter);
                        }

                }

            } catch (Exception e) {

                System.out.println("Exception.  in LoadViewTask doInBackground method: " + e.getMessage());
                //e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
            System.out.println("Exception. Counter value:" + values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {

            //close the progress dialog
            try {
                progressDialog.dismiss();
            } catch (Exception exp) {
                exp.printStackTrace();
            }

        }
    }
}