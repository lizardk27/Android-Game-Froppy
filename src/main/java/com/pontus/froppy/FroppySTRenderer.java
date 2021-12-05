package com.pontus.froppy;
/**
 * Created by gabriel yibirin on 2/13/2016.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;

public class FroppySTRenderer implements Renderer {

    // VARIABLE DEFINITION START
    //--------------------------------------------------------------------------------

    // Our screenresolution
    final static float optimalScreenHeight = 1920;
    // Our view, projection and p&v matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];
    // textures array - the size is defined by
    // GLES20.GL_TEXTURE31 -- predefined texture position
    // which is the highest number of texture predefined pos
    // CHANGED -- TESTING 99 as limit
    // int[] texturenames = new int[32];
    int[] texturenames = new int[9];
    long mLastTime;
    long msLastTimeBugsCreated = 0;
    int fly_speed = 10;
    int fly_speed_level = 10;
    int fly_creation_counter = 5000;

    //ARRAY LISTS TO HOLD DIFFRENT SPRITES

    private float currScreenHeight = 1920;
    private float currScreenWidth = 1080;
    private float scale;
    private PointF center_stage;
    private long last_flies_created = 0;
    private long last_flies_updated = 0;
    private long last_eels_updated = 0;
    private long last_ladybugs_updated = 0;
    private long final_lastRender = 0;
    private int flyCounter = 0;
    private int beeCounter = 0;
    private int queen_beeCounter = 0;
    private int eelsCounter = 0;
    private int dragonflyCounter = 0;
    private int heartsCounter = 0;
    private int flyAteCounter = 0;
    private int flyAteType = 0;
    private int activeBug = 0;
    private int tongueSize = 20;
    private int instructionClicks = 0;
    private int finalSongDuration = 15000;
    private int image_showing = 0;
    private int finalSongPeriod = 100;

    private ArrayList<Fly> flies = new ArrayList<Fly>();
    private ArrayList<TileObject> snow = new ArrayList<TileObject>();
    private ArrayList<TileObject> rain = new ArrayList<TileObject>();
    private ArrayList<TileObject> eels = new ArrayList<TileObject>();
    private ArrayList<TileObject> ladybugs = new ArrayList<TileObject>();

    private float tongueX;

    private float tongueY;
    private float tonguePivotX;
    private float tonguePivotY;
    private float scoreYpos;
    private float scoreXpos;
    private float levelYpos;
    private float levelXpos;
    private float timerYpos;
    private float timerXpos;
    float angle = 0;

    // Menu TileObject
    private TileObject play_TileObject;
    private TileObject exit_TileObject;
    private TileObject musicControl_TileObject;
    private TileObject soundControl_TileObject;
    private TileObject pause_button_TileObject;
    private TileObject confirm_text_TileObject;
    private TileObject confirm_yes_TileObject;
    private TileObject confirm_no_TileObject;

    //Sprites
    private Tongue the_tongue_sprite;
    private Sprite menu_sprite;
    private Sprite background_sprite;

    //froppy green
    private TileObject froppy_main_tileObj;
    private TileObject froppy_main_cheeks_tileObj;
    private TileObject froppy_main_cheeks2_tileObj;
    //froppy blue
    private TileObject froppy_main_blue_cheeks_tileObj;
    //froppy yellow
    private TileObject froppy_main_yellow_cheeks_tileObj;

    //froppy umbrella
    private TileObject froppy_main_umbrella_tileObj;
    //froppy snowcap
    private TileObject froppy_main_snowcap_tileObj;
    //froppy electrocuted
    private TileObject froppy_main_elecrocuted_tileObj;
    //froppy eyes
    private TileObject froppy_main_left_eye_tileObj;
    private TileObject froppy_main_right_eye_tileObj;
    //froppy final song
    private TileObject froppy_final_1a;
    private TileObject froppy_final_2a;
    private TileObject froppy_final_3a;

    private TileObject froppy_final_1b;
    private TileObject froppy_final_2b;
    private TileObject froppy_final_3b;

    private TileObject froppy_final_1c;
    private TileObject froppy_final_2c;
    private TileObject froppy_final_3c;

    // Misc
    private Context mContext;
    private float minXpos;
    private float maxXpos;
    private float middleXpos;

    //private float angle;
    private RectF base;
    private int talking_timer = 0;
    private float minXVCpos;
    private float maxXVCpos;
    private float middleXVCpos;

    private int score = 0;
    private int level = 0;
    private int timer = 0;
    private long last_timer_change = 0;
    private int timer_start_value = 99; //99
    private int timer_change_period = 200; //1500

    private boolean ateFly = false;
    private boolean isPaused = true;
    private boolean isSurfaceCreated = false;
    private boolean levelChangeLocked = true;
    private boolean isFirstRun = true;
    private boolean gameIsWon = false;
    private boolean gameIsLost = false;
    private boolean showPlayButton = true;
    private boolean returningFromPause = false;
    private boolean showInstructions = false;
    private boolean isSnowing = false;
    private boolean isRaining = false;
    private boolean eels_showing = false;
    private boolean eels_change = false;
    private boolean ladybugs_showing = false;
    private boolean ladybugs_change = false;
    private boolean lb_left = false;
    private boolean lb_right = false;
    boolean returningToPause = false;
    boolean confirmingExit = false;
    boolean returningFromInstructions = false;


    // private SignsTileManager banners_manager;
    private FliesTileManager flies_manager;
    private FroppyTileManager froppy_tile_manager;
    private MenuTileManager menu_tile_manager;
    private LevelBannersTileManager banners_tile_manager;
    private TextManager tm;

    private float left_eye_posX;
    private float right_eye_posX;
    private float left_eye_posY;
    private float right_eye_posY;

    private TextObject timer_text;
    private TextObject score_text;
    private TextObject level_text;

    private SoundManager froppySoundManager;

    //--------------------------------------------------------------------------------
    // VARIABLE DEFINITION END

    // Constructor
    //    FroppySTRenderer(Context c)
    public FroppySTRenderer(Context c) {


        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        currScreenHeight = metrics.heightPixels;
        currScreenWidth = metrics.widthPixels;

        // SET BASE SQUARE FOR OUR IMAGE, INITIAL POSITION (translation)
        base = new RectF(
                -(((float) (currScreenHeight * 1.7777777777777777777777777) * 1.25f) / 2),                                    // left,
                ((((float) (currScreenHeight * 1.7777777777777777777777777) * 1.25f) / 2) / 1.48148148148148148148148148f),   // top,
                (((float) (currScreenHeight * 1.7777777777777777777777777) * 1.25f) / 2),                                   // right,
                -((((float) (currScreenHeight * 1.7777777777777777777777777) * 1.25f) / 2) / 1.48148148148148148148148148f));     // bottom

        center_stage = new PointF(currScreenWidth / 2, currScreenHeight / 2);

        scale = 1f;
        angle = 0f;

        // ADDING BACKGROUND
        background_sprite = new Sprite(c, scale, angle, center_stage, base);

        // calculate scale factor for all images
        // 1920 x 1080 is the "normal" resolution
        // so, since all images are set to that resolution
        // we change the scale in accordance
        // to the difference between the optimalScreenHeight and currScreenHeight

        scale = currScreenHeight / optimalScreenHeight;


        // SIZE: 430x650      left,    top,     right,     bottom
        base = new RectF(-215f, 325f, 215f, -325f);
        // Position from center
        float xpos = center_stage.x + 10 * scale;
        float ypos = center_stage.y + 10 * scale;
        PointF translation = new PointF(xpos, ypos);

        menu_sprite = new Sprite(c, scale * 2, angle, translation, base);

        froppySoundManager = new SoundManager(mContext);

        // SIZE: 220x90     left,    top,     right,     bottom
        base = new RectF(0f, -45f, 220f, 45f);

        // Position from center
        tongueX = center_stage.x - 32 * scale;
        tongueY = center_stage.y + 500 * scale;

        tonguePivotX = center_stage.x - 40 * scale;
        tonguePivotY = center_stage.y - 520 * scale;

        translation = new PointF(tongueX, tongueY);
        the_tongue_sprite = new Tongue(c, scale, angle, translation, base);
        the_tongue_sprite.growFromLeft((float) 20);
        the_tongue_sprite.setAngle((float) Math.PI / 2);

        xpos = center_stage.x + 1150 * scale;
        ypos = center_stage.y + 765 * scale;

        pause_button_TileObject = new TileObject(7, xpos, ypos, 152, 152);

        //PAUSE MENU AND START MENU OVER ALL RENDERED LAST !!!!
        xpos = center_stage.x + 30 * scale;

        ypos = center_stage.y + 70 * scale;
        soundControl_TileObject = new TileObject(3, xpos, ypos, 80, 80);

        ypos = center_stage.y - 120 * scale;
        musicControl_TileObject = new TileObject(3, xpos, ypos, 80, 80);

        xpos = center_stage.x - 270 * scale;
        ypos = center_stage.y - 480 * scale;
        play_TileObject = new TileObject(1, xpos, ypos, 100, 150);

        xpos = center_stage.x + 90 * scale;
        exit_TileObject = new TileObject(2, xpos, ypos, 200, 150);

        xpos = center_stage.x - 230 * scale;
        ypos = center_stage.y - 200 * scale;
        confirm_text_TileObject = new TileObject(6, xpos, ypos, 410, 410);


        ypos = center_stage.y - 400 * scale;
        xpos = center_stage.x - 200 * scale;
        confirm_yes_TileObject = new TileObject(4, xpos, ypos, 120, 120);

        ypos = center_stage.y - 400 * scale;
        xpos = center_stage.x + 50 * scale;
        confirm_no_TileObject = new TileObject(5, xpos, ypos, 120, 120);

        // GAME ANNOUNCE SIGNS / BANNERS
        ypos = center_stage.y + 10 * scale;
        xpos = center_stage.x + 10 * scale;

        froppy_main_tileObj = new TileObject(0, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_cheeks_tileObj = new TileObject(1, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_cheeks2_tileObj = new TileObject(6, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_blue_cheeks_tileObj = new TileObject(5, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_yellow_cheeks_tileObj = new TileObject(3, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_umbrella_tileObj = new TileObject(2, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_snowcap_tileObj = new TileObject(4, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_main_elecrocuted_tileObj = new TileObject(19, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);

        froppy_final_1a = new TileObject(7, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_final_1b = new TileObject(8, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);
        froppy_final_1c = new TileObject(9, tonguePivotX - 295 * scale, tonguePivotY + 810 * scale, 170, 170);

        froppy_final_2a = new TileObject(7, tonguePivotX - 800 * scale, tonguePivotY + 1 * scale, 170, 170);
        froppy_final_2b = new TileObject(8, tonguePivotX - 800 * scale, tonguePivotY + 1 * scale, 170, 170);
        froppy_final_2c = new TileObject(9, tonguePivotX - 800 * scale, tonguePivotY + 1 * scale, 170, 170);

        froppy_final_3a = new TileObject(7, tonguePivotX + 300 * scale, tonguePivotY + 1 * scale, 170, 170);
        froppy_final_3b = new TileObject(8, tonguePivotX + 300 * scale, tonguePivotY + 1 * scale, 170, 170);
        froppy_final_3c = new TileObject(9, tonguePivotX + 300 * scale, tonguePivotY + 1 * scale, 170, 170);


        left_eye_posX = tonguePivotX - 295 * scale;
        left_eye_posY = tonguePivotY + 810 * scale;
        right_eye_posX = tonguePivotX - 295 * scale;
        right_eye_posY = tonguePivotY + 810 * scale;

        froppy_main_left_eye_tileObj = new TileObject(23, left_eye_posX, left_eye_posY, 170, 170);
        froppy_main_right_eye_tileObj = new TileObject(24, right_eye_posX, right_eye_posY, 170, 170);

    }


    public void onDestroy() {
        ((MainActivity) mContext).onDestroy();
        MusicManager.release();
        System.exit(0);
    }


    public void onPause() {
        if(gameIsWon | gameIsLost) {
            gameIsLost = false;
            gameIsWon = false;
        }
        returningFromPause = true;

        ((MainActivity) mContext).onPause();
    }

    public void showPlayButton(boolean state) {
        showPlayButton = state;
    }


    private void setLevel(int level_value) throws IOException {
        if(level_value < 0 || level_value > 11) {
            throw new IOException("level value should be between 0 and 11");
        }
        // Create our new textobject
        String tmp = String.valueOf(level);
        while (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        // Prepare the text for rendering
        tm.removeText(level_text);
        level_text = new TextObject(tmp, levelXpos, levelYpos);
        tm.addText(level_text);
        tm.PrepareDraw();

    }

    private void setTimer(int timer_value) throws IOException {

        String tmp = String.valueOf(timer_value);
        while (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        // Prepare the text for rendering
        tm.removeText(timer_text);
        timer_text = new TextObject(tmp, timerXpos, timerYpos);
        tm.addText(timer_text);
        tm.PrepareDraw();

    }


    public void onResume() {

        if(!isPaused) {
            ((MainActivity) (mContext)).closeContextMenu();
            MusicManager.pause();

//            MusicManager.start(mContext, MusicManager.MUSIC_GAME);
        }
    }

    private boolean musicPlaying = false;

    @Override
    public synchronized void onDrawFrame(GL10 unused) {


        if (isPaused & !musicPlaying) {
            MusicManager.release();
            MusicManager.getMusicVolume(mContext);
            MusicManager.setVolume(0.5f);
            MusicManager.start(mContext,MusicManager.MUSIC_GAME,true);
            musicPlaying=true;
        }

        if (!isPaused & musicPlaying){
            MusicManager.pause();
            musicPlaying=false;
        }

        isSurfaceCreated = true;

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if(mLastTime > now) return;


        float ypos = center_stage.y + 1 * scale;
        float xpos = center_stage.x + 1 * scale;


        //GAME IS WON FINALE
        if(gameIsWon) {

            finalSongDuration--;
            System.out.println("NOTE: finalSongDuration> "+finalSongDuration);

            if(finalSongDuration == 0) {
                System.out.println("NOTE: REACHED End of finale");
                level = 0;
                score = 0;
                isPaused = true;

                onPause();
            }

            if(final_lastRender == 0 | ((now - final_lastRender) > 190)) {

                froppy_tile_manager.removeAll();
                flies_manager.removeAll();
                menu_tile_manager.removeAll();
                banners_tile_manager.removeAll();

                image_showing++;
                if(image_showing == 3) {
                    image_showing = 0;
                }
                System.out.println("NOTE: image_showing:" + image_showing);
                //add froppys

                switch (image_showing) {
                    case 0:
                        froppy_tile_manager.addTileObject(froppy_final_1a);
                        froppy_tile_manager.addTileObject(froppy_final_2a);
                        froppy_tile_manager.addTileObject(froppy_final_3a);
                        break;
                    case 1:
                        froppy_tile_manager.addTileObject(froppy_final_1b);
                        froppy_tile_manager.addTileObject(froppy_final_2b);
                        froppy_tile_manager.addTileObject(froppy_final_3b);
                        break;
                    case 2:
                        froppy_tile_manager.addTileObject(froppy_final_1c);
                        froppy_tile_manager.addTileObject(froppy_final_2c);
                        froppy_tile_manager.addTileObject(froppy_final_3c);
                        break;
                }

                final_lastRender = now;
            }


            int x_init = (int) Math.floor(Math.random() * currScreenWidth);
            int y_init = (int) Math.floor(Math.random() * currScreenHeight);
            int type = (int) Math.floor(Math.random() * 4);
            int heart_pos = 0;

            switch (type) {
                case 0:
                    heart_pos = 39;
                    break;
                case 1:
                    heart_pos = 47;
                    break;
                case 2:
                    heart_pos = 55;
                    break;
                case 3:
                    heart_pos = 63;
                    break;
            }

            flies_manager.addTileObject(new TileObject(heart_pos, x_init, y_init, 85, 85));
         }

        if(gameIsWon) {

            //background as level 1
            background_sprite.SetupImage("drawable/background_day", 0, texturenames);
            background_sprite.UpdateSprite();
            Render(mtrxProjectionAndView);
            renderSprite(background_sprite, mtrxProjectionAndView);
            froppy_tile_manager.PrepareDraw();
            if(froppy_tile_manager != null) {
                froppy_tile_manager.Draw(mtrxProjectionAndView);
            }
            flies_manager.PrepareDraw();
            if(flies_manager != null) {
                flies_manager.Draw(mtrxProjectionAndView);
            }
            mLastTime = now;
            return;
        }

        //System.out.println("NOTE: --SHOULD  NOT RUN ON FINALE !!!!!");
        //END OF GAME IS WON FINALE

        // BLOCK  RUNS IF NOT PAUSED
        if(!isPaused & !showInstructions) {
            //System.out.println("NOTE: in not paused");
            try {
                setScore(score);

                if(now - last_timer_change > timer_change_period) {

                    timer--;
                    if(timer <= 0) {
                        if(returningFromInstructions) {
                            timer = timer_start_value;
                            levelChangeLocked = true;
                            returningFromInstructions = false;
                        } else {
                            level++;
                            if(level < 11) {
                                isPaused = true;
                                showInstructions = true;
                                timer = 0;
                                levelChangeLocked = true;
                                returningFromInstructions = true;
                                return;
                            } else {
                                playWonAnimation();
                                return;
                            }
                        }

                    }

                    setTimer(timer);

                    last_timer_change = now;
                }

                if(talking_timer > 0) {
                    talking_timer--;
                }

                //test original code
//
//                if(talking_timer == 0 && score > 0 && score % 9 == 0) {

                if(talking_timer == 0) {

                    int pos = (int) Math.floor(Math.random() * 5);
                    switch (pos) {
                        case 0:
                            froppySoundManager.playSound(SoundManager.DELICIOUS);
                            talking_timer = 1000;
                            break;
                        case 1:
                            froppySoundManager.playSound(SoundManager.TASTY);
                            talking_timer = 1000;
                            break;
                        case 2:
                            froppySoundManager.playSound(SoundManager.CRUNCHY);
                            talking_timer = 1000;
                            break;
                        case 3:
                            froppySoundManager.playSound(SoundManager.TASTE_GOOD);
                            talking_timer = 1000;
                            break;
                        case 4:
                            froppySoundManager.playSound(SoundManager.YOU_GREAT);
                            talking_timer = 1000;
                            break;

                        default:
                            talking_timer = 1000;
                            break;

                    }
                }


                if(returningFromPause | isFirstRun | levelChangeLocked) {

                    if(!returningFromPause) {
                        if(isFirstRun) {
                            level = 1;
                        }
                    }

                    if(levelChangeLocked) {
                        while (rain.size() > 0) {
                            rain.remove(0);
                        }
                        while (snow.size() > 0) {
                            snow.remove(0);
                        }
                        while (flies.size() > 0) {
                            flies.remove(0);
                        }
                    }

                    setLevel(level);
                    switch (level) {
                        case 1:
                            background_sprite.SetupImage("drawable/background_day", 0, texturenames);
                            isRaining = false;
                            ladybugs_showing = false;
                            eels_showing = false;
                            isSnowing = false;
                            fly_creation_counter = 1500;
                            fly_speed = fly_speed_level = 10;
                            break;
                        case 2:
                            background_sprite.SetupImage("drawable/background_day_rb", 0, texturenames);
                            isRaining = true;
                            ladybugs_showing = true;
                            lb_left = true;
                            lb_right = true;
                            eels_showing = false;
                            isSnowing = false;
                            fly_creation_counter = 1300;
                            fly_speed = fly_speed_level = 12;
                            break;
                        case 3:
                            background_sprite.SetupImage("drawable/background_day", 0, texturenames);
                            isRaining = false;
                            ladybugs_showing = true;
                            lb_left = true;
                            lb_right = true;
                            eels_showing = false;
                            isSnowing = true;
                            fly_creation_counter = 1100;
                            fly_speed = fly_speed_level = 14;
                            break;
                        case 4:
                            background_sprite.SetupImage("drawable/background_afternoon", 0, texturenames);
                            isRaining = false;
                            ladybugs_showing = true;
                            lb_left = true;
                            lb_right = true;
                            eels_showing = false;
                            isSnowing = false;
                            fly_creation_counter = 1000;
                            fly_speed = fly_speed_level = 16;
                            break;
                        case 5:
                            background_sprite.SetupImage("drawable/background_afternoon_rb", 0, texturenames);
                            isRaining = true;
                            ladybugs_showing = true;
                            lb_left = true;
                            lb_right = true;
                            isSnowing = false;
                            eels_showing = false;
                            fly_creation_counter = 900;
                            fly_speed = fly_speed_level = 20;
                            break;
                        case 6:
                            background_sprite.SetupImage("drawable/background_afternoon", 0, texturenames);
                            isRaining = false;
                            ladybugs_showing = true;
                            lb_left = true;
                            lb_right = true;
                            isSnowing = true;
                            eels_showing = false;
                            fly_creation_counter = 800;
                            fly_speed = fly_speed_level = 23;
                            break;
                        case 7:
                            background_sprite.SetupImage("drawable/background_night", 0, texturenames);
                            isRaining = false;
                            ladybugs_showing = false;
                            eels_showing = false;
                            isSnowing = false;
                            fly_creation_counter = 1000;
                            fly_speed = fly_speed_level = 26;
                            break;
                        case 8:
                            background_sprite.SetupImage("drawable/background_night", 0, texturenames);
                            isRaining = true;
                            isSnowing = false;
                            ladybugs_showing = false;
                            eels_showing = true;
                            fly_creation_counter = 700;
                            fly_speed = fly_speed_level = 30;
                            break;
                        case 9:
                            background_sprite.SetupImage("drawable/background_night", 0, texturenames);
                            isRaining = false;
                            isSnowing = true;
                            ladybugs_showing = false;
                            eels_showing = true;
                            fly_creation_counter = 600;
                            fly_speed = fly_speed_level = 34;
                            break;
                        case 10:
                            background_sprite.SetupImage("drawable/background_night", 0, texturenames);
                            isRaining = true;
                            isSnowing = true;
                            ladybugs_showing = true;
                            eels_showing = true;
                            fly_creation_counter = 550;
                            fly_speed = fly_speed_level = 38;
                            break;

                        default:
                            background_sprite.SetupImage("drawable/background_day", 0, texturenames);
                            isRaining = false;
                            isSnowing = false;
                            eels_showing = false;
                            fly_creation_counter = 500;
                            fly_speed = fly_speed_level = 44;
                            break;
                    }//end switch level

                    levelChangeLocked = false;
                    isFirstRun = false;
                    returningFromPause = false;

                }//end if  ( levelChangeLocked | isFirstRun | returningFromPause )

                // END OF TRY IN NOT PAUSED LOOP
            } catch (IOException e) {
                e.printStackTrace();
            }

            // STILL IN NOT PAUSED
            pause_button_TileObject.setNotTouched();


            //  UPDATE FLIES if game not won or lost a
            //  AND
            //  TIME CONDITION: (last_flies_updated == 0 || (now - last_flies_updated)

            if((last_flies_updated == 0 || (now - last_flies_updated) > 100)
                    && (!gameIsLost && !gameIsWon)) {
                try {
                    for (int fly_count = 0; fly_count < flies.size(); fly_count++) {
                        Fly tmp = flies.get(fly_count);

                        int heading = (int) Math.floor(Math.random() * 30) + 1;

                        if(heading < 6) {
                            tmp.setHeading(heading);
                        }
                        tmp.setStep(fly_speed);
                        tmp.move();
                        if(tmp.x < 0) {
                            tmp.x = currScreenWidth;
                        }
                        if(tmp.x > (currScreenWidth - (400 * scale))) {
                            tmp.x = 0;
                        }
                        if(tmp.y < 0) {
                            tmp.y = currScreenHeight;
                        }
                        if(tmp.y > currScreenHeight) {
                            tmp.y = 0;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                last_flies_updated = now;

            }// END LAST FLIES UPDATED


            //  CREATE Flies if game not won or lost a
            //  AND
            //  TIME CONDITION: (now - last_flies_created) > fly_creation_counter

            if((last_flies_created == 0 || (now - last_flies_created) > fly_creation_counter)
                    && (!gameIsLost && !gameIsWon)) {

                int x_init = (int) Math.floor(Math.random() * currScreenWidth - (400 * scale)) + 1;
                int y_init = (int) Math.floor(Math.random() * currScreenHeight) + 1;

                int type_of_Flying_bug = (int) Math.floor(Math.random() * 100);

                Fly aFly = new Fly(Fly.TYPE_BEE, x_init, y_init, 85, 85);

                //check if there are flies on the screen
                boolean areFlies=false;
                for (Fly tmp:flies){
                    if (tmp.getType()==Fly.TYPE_FLY | tmp.getType()==Fly.TYPE_FIRE_FLY){
                        areFlies=true;
                    }
                }
                //if there are not any flies, then add one
                if (!areFlies){

                    if(level < 7) {
                        aFly = new Fly(Fly.TYPE_FLY, x_init, y_init, 85, 85);
                    } else {
                        aFly = new Fly(Fly.TYPE_FIRE_FLY, x_init, y_init, 85, 85);
                    }

                    try {
                        int heading = (int) Math.floor(Math.random() * 6) + 1;
                        aFly.setHeading(heading);
                        aFly.setStep(fly_speed);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    flies.add(aFly);
                    last_flies_created = now;


                //if there are flies then randomly choose type

                }else if(type_of_Flying_bug > 55) {
                    if(level < 7) {
                        aFly = new Fly(Fly.TYPE_FLY, x_init, y_init, 85, 85);
                    } else {
                        aFly = new Fly(Fly.TYPE_FIRE_FLY, x_init, y_init, 85, 85);
                    }
                } else {
                    if(type_of_Flying_bug > 35 & level < 7) {
                        aFly = new Fly(Fly.TYPE_BEE, x_init, y_init, 85, 85);
                    } else {
                        if(level > 3) {
                            int type = (int) Math.floor(Math.random() * 3);
                            switch (type) {
                                case 0:
                                    if(level < 7) {
                                        aFly = new Fly(Fly.TYPE_QUEEN_BEE, x_init, y_init, 85, 85);
                                    }else{

                                    }
                                    break;
                                case 1:
                                    aFly = new Fly(Fly.TYPE_BLUE_FLY, x_init, y_init, 85, 85);
                                    break;
                                case 2:
                                    if(level > 4)
                                        aFly = new Fly(Fly.TYPE_DRAGON_FLY, x_init, y_init, 85, 85);
                                    break;
                            }
                        }
                    }
                }


                try {
                    int heading = (int) Math.floor(Math.random() * 6) + 1;
                    aFly.setHeading(heading);
                    aFly.setStep(fly_speed);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                flies.add(aFly);

                last_flies_created = now;
            }// END CREATE FLIES

            froppy_tile_manager.removeAll();
            flies_manager.removeAll();

            // 13 14 left
            // 22 25 right
            if(eels_showing) {
                // if no eels on array. run once.
                if(eels.size() < 2) {
                    eels.add(new TileObject(14, currScreenWidth - 50 * scale, center_stage.y - 100 * scale, 425, 70));
                    eels.add(new TileObject(22, 50 * scale, center_stage.y - 200 * scale, 425, 70));
                }
                eels_change = !eels_change;

                if(last_eels_updated == 0 || (now - last_eels_updated) > 100) {

                    TileObject eelLeft = eels.get(0);
                    TileObject eelRight = eels.get(1);

                    eelLeft.move(-10, 0);
                    if(eelLeft.x < 50 * scale) {
                        eelLeft.x = currScreenWidth - 50 * scale;
                    }

                    eelRight.move(10, 0);
                    if(eelRight.x > currScreenWidth - 50 * scale) {
                        eelRight.x = 50 * scale;
                    }

                    while (eels.size() > 0) {
                        eels.remove(0);
                    }

                    if(eels_change) {
                        eels.add(new TileObject(13, eelLeft.x, eelLeft.y, 425, 70));
                        eels.add(new TileObject(25, eelRight.x, eelRight.y, 425, 70));
                    } else {
                        eels.add(new TileObject(14, eelLeft.x, eelLeft.y, 425, 70));
                        eels.add(new TileObject(22, eelRight.x, eelRight.y, 425, 70));
                    }
                    last_eels_updated = now;
                }
            }

            // 21 33 right
            // 22 34 left on 6x6tile
            if(ladybugs_showing) {
                // if no ladybugs on array. run once.
                if(ladybugs.size() < 2) {
                    ladybugs.add(new TileObject(57, currScreenWidth - 50 * scale, center_stage.y - 900 * scale, 170, 170));
                    ladybugs.add(new TileObject(59, 50 * scale, center_stage.y - 850 * scale, 170, 170));
                }
                ladybugs_change = !ladybugs_change;

                if(last_ladybugs_updated == 0 || (now - last_ladybugs_updated) > 100) {

                    TileObject ladybugLeft = ladybugs.get(0);
                    TileObject ladybugRight = ladybugs.get(1);

                    ladybugLeft.move(-60, 0);
                    if(ladybugLeft.x < 50 * scale) {
                        ladybugLeft.x = currScreenWidth - 50 * scale;
                    }

                    ladybugRight.move(80, 0);
                    if(ladybugRight.x > currScreenWidth - 50 * scale) {
                        ladybugRight.x = 50 * scale;
                    }

                    while (ladybugs.size() > 0) {
                        ladybugs.remove(0);
                    }

                    if(ladybugs_change) {
                        ladybugs.add(new TileObject(57, ladybugLeft.x, ladybugLeft.y, 170, 170));
                        ladybugs.add(new TileObject(59, ladybugRight.x, ladybugRight.y, 170, 170));
                    } else {
                        ladybugs.add(new TileObject(58, ladybugLeft.x, ladybugLeft.y, 170, 170));
                        ladybugs.add(new TileObject(60, ladybugRight.x, ladybugRight.y, 170, 170));
                    }
                    last_ladybugs_updated = now;
                }
            }


            for (int fly_count = 0; fly_count < flies.size(); fly_count++) {
                flies_manager.addTileObject(flies.get(fly_count));
            }

            flies_manager.PrepareDraw();

            if(eels_showing) {
                for (int eel_count = 0; eel_count < eels.size(); eel_count++) {
                    froppy_tile_manager.addTileObject(eels.get(eel_count));
                }
            }

            if(ladybugs_showing) {
                if(lb_left) {
                    flies_manager.addTileObject(ladybugs.get(0));
                }
                if(lb_right) {
                    flies_manager.addTileObject(ladybugs.get(1));
                }

            }
            if(isRaining) {
                int x_init = (int) Math.floor(Math.random() * currScreenWidth);
                int y_init = (int) currScreenHeight;
                TileObject raindrop = new TileObject(8, x_init, y_init, 85, 85);
                rain.add(raindrop);
                raindrop = new TileObject(8, x_init, y_init, 85, 85);
                rain.add(raindrop);
                raindrop = new TileObject(8, x_init, y_init, 85, 85);
                rain.add(raindrop);
                ArrayList<TileObject> rain_remove_list = new ArrayList<TileObject>();
                for (TileObject rainD : rain) {
                    int step = (int) Math.floor(Math.random() * 15) + 1;
                    rainD.move(0, -step);
                    if(rainD.y < -200) {
                        rain_remove_list.add(rainD);
                    }
                }
                for (TileObject rainD : rain_remove_list) {
                    rain.remove(rainD);
                }
            }

            if(isSnowing) {
                for (int count_snow = 0; count_snow < 3; count_snow++) {
                    int x_init = (int) Math.floor(Math.random() * currScreenWidth);
                    int y_init = (int) currScreenHeight;
                    int type = (int) Math.floor(Math.random() * 4);
                    int flake_pos = 0;
                    switch (type) {
                        case 0:
                            flake_pos = 7;
                            break;
                        case 1:
                            flake_pos = 15;
                            break;
                        case 2:
                            flake_pos = 23;
                            break;
                        case 3:
                            flake_pos = 31;
                            break;
                    }
                    TileObject snowFlake = new TileObject(flake_pos, x_init, y_init, 85, 85);
                    snow.add(snowFlake);
                }

                ArrayList<TileObject> snow_remove_list = new ArrayList<TileObject>();
                for (TileObject snowF : snow) {
                    int step = (int) Math.floor(Math.random() * 15) + 1;
                    snowF.move(0, -step);
                    if(snowF.y < -200) {
                        snow_remove_list.add(snowF);
                    }
                }
                for (TileObject snowF : snow_remove_list) {
                    snow.remove(snowF);
                }
            }

            if(beeCounter > 0) {

                froppySoundManager.playSound(SoundManager.BURP);
                beeCounter--;
                int x_init = (int) (center_stage.x + 10 * scale);
                int y_init = (int) (center_stage.y + 500 * scale);
                Fly aFly;
                if (level<7) {
                    aFly= new Fly(Fly.TYPE_FLY, x_init, y_init, 85, 85);
                }else{
                    aFly= new Fly(Fly.TYPE_FIRE_FLY, x_init, y_init, 85, 85);
                }
                try {
                    int heading = (int) Math.floor(Math.random() * 6) + 1;
                    aFly.setHeading(heading);
                    aFly.setStep(fly_speed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                flies.add(aFly);
            }

            if(queen_beeCounter > 0) {

                froppySoundManager.playSound(SoundManager.BURP);
                queen_beeCounter--;
                int x_init = (int) (center_stage.x + 10 * scale);
                int y_init = (int) (center_stage.y + 500 * scale);
                Fly aFly = new Fly(Fly.TYPE_BEE, x_init, y_init, 85, 85);
                try {
                    int heading = (int) Math.floor(Math.random() * 6) + 1;
                    aFly.setHeading(heading);
                    aFly.setStep(fly_speed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                flies.add(aFly);
            }

            if(heartsCounter > 0) {
                heartsCounter--;
                int x_init = (int) center_stage.x + (int) Math.floor(Math.random() * 600) - 300;
                int y_init = (int) (center_stage.y + 600 * scale) + (int) Math.floor(Math.random() * 400) - 400;
                int type = (int) Math.floor(Math.random() * 4);
                int heart_pos = 0;
                switch (type) {
                    case 0:
                        heart_pos = 39;
                        break;
                    case 1:
                        heart_pos = 47;
                        break;
                    case 2:
                        heart_pos = 55;
                        break;
                    case 3:
                        heart_pos = 63;
                        break;
                }
                flies_manager.addTileObject(new TileObject(heart_pos, x_init, y_init, 85, 85));
            }

            if(dragonflyCounter > 0) {
                fly_speed = 5;
                dragonflyCounter--;
            } else {
                fly_speed = fly_speed_level;
            }


            if(eelsCounter > 0) {

                froppySoundManager.playSound(SoundManager.ELECTROCUTED);
                eelsCounter--;
                int x_init = (int) center_stage.x + (int) Math.floor(Math.random() * 600) - 300;
                int y_init = (int) (center_stage.y + 600 * scale) + (int) Math.floor(Math.random() * 400) - 400;
                int type = (int) Math.floor(Math.random() * 5) + 23;

                // 20 y 21 flash
                int flash = (int) Math.floor(Math.random() * 2) + 1;
                if(flash == 1) {
                    froppy_tile_manager.addTileObject(new TileObject(20, x_init, y_init, 170, 170));
                } else {
                    froppy_tile_manager.addTileObject(new TileObject(21, x_init, y_init, 170, 170));
                }
            }

            if(ateFly) {
                if(flyAteCounter > 15) {
                    froppy_tile_manager.addTileObject(froppy_main_cheeks2_tileObj);
                } else {
                    if(flyAteType == Fly.TYPE_FLY)
                        froppy_tile_manager.addTileObject(froppy_main_cheeks_tileObj);
                    if(flyAteType == Fly.TYPE_BLUE_FLY)
                        froppy_tile_manager.addTileObject(froppy_main_blue_cheeks_tileObj);
                    if(flyAteType == Fly.TYPE_BEE)
                        froppy_tile_manager.addTileObject(froppy_main_yellow_cheeks_tileObj);
                    if(flyAteType == Fly.TYPE_FIRE_FLY)
                        froppy_tile_manager.addTileObject(froppy_main_cheeks_tileObj);
                    if(flyAteType == Fly.TYPE_QUEEN_BEE)
                        froppy_tile_manager.addTileObject(froppy_main_cheeks_tileObj);
                    if(flyAteType == Fly.TYPE_DRAGON_FLY)
                        froppy_tile_manager.addTileObject(froppy_main_cheeks_tileObj);
                }
                if(flyAteCounter > 0) {
                    flyAteCounter--;
                } else {
                    ateFly = false;
                }

            } else {
                froppy_tile_manager.addTileObject(froppy_main_tileObj);
            }

            if(eelsCounter > 0) {
                froppy_tile_manager.addTileObject(froppy_main_elecrocuted_tileObj);
                int eyeMove = 18;
                float realLeftEyePosX = tonguePivotX - 50 * scale;
                float realLeftEyePosY = tonguePivotY - 150 * scale;
                float realRightEyePosX = tonguePivotX + 50 * scale;
                float realRightEyePosY = tonguePivotY - 150 * scale;
                froppy_main_left_eye_tileObj.x = left_eye_posX;
                froppy_main_left_eye_tileObj.y = left_eye_posY;
                froppy_main_right_eye_tileObj.x = right_eye_posX;
                froppy_main_right_eye_tileObj.y = right_eye_posY;

                double xx = Math.random() * currScreenWidth;
                double yy = Math.random() * currScreenHeight;
                if(realLeftEyePosX > xx) {
                    froppy_main_left_eye_tileObj.x -= eyeMove * scale;
                } else {
                    froppy_main_left_eye_tileObj.x += eyeMove * scale;
                }
                if(realLeftEyePosY > yy) {
                    froppy_main_left_eye_tileObj.y += eyeMove * scale;
                } else {
                    froppy_main_left_eye_tileObj.y -= eyeMove * scale;
                }
                if(realRightEyePosX > xx) {
                    froppy_main_right_eye_tileObj.x -= eyeMove * scale;
                } else {
                    froppy_main_right_eye_tileObj.x += eyeMove * scale;
                }
                if(realRightEyePosY > yy) {
                    froppy_main_right_eye_tileObj.y += eyeMove * scale;
                } else {
                    froppy_main_right_eye_tileObj.y -= eyeMove * scale;
                }
            }

            froppy_tile_manager.addTileObject(froppy_main_left_eye_tileObj);
            froppy_tile_manager.addTileObject(froppy_main_right_eye_tileObj);

            if(isSnowing) {
                froppy_tile_manager.addTileObject(froppy_main_snowcap_tileObj);
            }

            if(isRaining) {
                froppy_tile_manager.addTileObject(froppy_main_umbrella_tileObj);
            }
            froppy_tile_manager.PrepareDraw();

        }// END OF BLOCK THAT RUNS WHEN NOT PAUSED AND NOT SHOW INSTRUCTIONS

        //System.out.println("NOTE: block runs paused or not paused");

        // Render all
        Render(mtrxProjectionAndView);


        background_sprite.UpdateSprite();

        renderSprite(background_sprite, mtrxProjectionAndView);

        // Render the signs when needed from image tile
        if(froppy_tile_manager != null && !isPaused) {
            froppy_tile_manager.Draw(mtrxProjectionAndView);
        }

        for (TileObject aFlake : snow) {
            flies_manager.addTileObject(aFlake);
        }

        for (TileObject rainDrop : rain) {
            flies_manager.addTileObject(rainDrop);
        }

        flies_manager.PrepareDraw();
        // Render the flies and other
        if(flies_manager != null && !isPaused) {
            flies_manager.Draw(mtrxProjectionAndView);
        }

        tm.PrepareDraw();
        // Render the text
        if(tm != null && !isPaused) {
            tm.Draw(mtrxProjectionAndView);

        }


        the_tongue_sprite.UpdateSprite();

        renderSprite(the_tongue_sprite, mtrxProjectionAndView);

        menu_tile_manager.removeAll();
        banners_tile_manager.removeAll();

        //RENDERS WHEN PAUSED

        if(isPaused) {

            if(showInstructions) {
                if(level <= 0) {
                    level = 1;
                }
                int curr_lev = level - 1;
                banners_tile_manager.addTileObject(new TileObject(curr_lev, (center_stage.x - 1200 * scale), (center_stage.y - 950 * scale), 1000, 1000));
                banners_tile_manager.PrepareDraw();
                banners_tile_manager.Draw(mtrxProjectionAndView);

            } else {

                if(confirmingExit) {
                    menu_tile_manager.addTileObject(confirm_no_TileObject);
                    menu_tile_manager.addTileObject(confirm_text_TileObject);
                    menu_tile_manager.addTileObject(confirm_yes_TileObject);
                } else {

                    menu_tile_manager.addTileObject(play_TileObject);
                    menu_tile_manager.addTileObject(exit_TileObject);
                    menu_tile_manager.addTileObject(musicControl_TileObject);
                    menu_tile_manager.addTileObject(soundControl_TileObject);
                }


                menu_sprite.UpdateSprite();
                renderSprite(menu_sprite, mtrxProjectionAndView);

                menu_tile_manager.PrepareDraw();
                menu_tile_manager.Draw(mtrxProjectionAndView);
            }
            //end isPaused
        } else {
            //the pause button - here for layering
            menu_tile_manager.addTileObject(pause_button_TileObject);
            menu_tile_manager.PrepareDraw();
            menu_tile_manager.Draw(mtrxProjectionAndView);
        }


        // Save the current time to see how long it took :).
        mLastTime = System.currentTimeMillis();
    }

    private synchronized void renderSprite(Sprite theSprite, float[] matrix) {


        // Set our shaderprogram to image shader
        GLES20.glUseProgram(riGraphicTools.sp_Image);


        // No culling of back faces
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // No depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_ALWAYS);

        // Enable blending
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        int mTexCoordLoc = 0;

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, theSprite.getVertexBuffer());

        // Get handle to texture coordinates location
        mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, theSprite.getUvBuffer());

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, matrix, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "s_texture");

        // Set the sampler texture unit to sprite.texture_position, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, theSprite.getTexturePos());

        // Draw the triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, theSprite.getIndices().length, GLES20.GL_UNSIGNED_SHORT, theSprite.getDrawListBuffer());

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }


    private synchronized void Render(float[] matrix) {


        // Set our shaderprogram to image shader
        GLES20.glUseProgram(riGraphicTools.sp_Image);


        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // No culling of back faces
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // No depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_ALWAYS);

        // Enable blending
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        int mTexCoordLoc = 0;

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        currScreenWidth = width;
        currScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) currScreenWidth, (int) currScreenHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, currScreenWidth, 0.0f, currScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    private void goToPontus() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pontusdd.com"));
        ((MainActivity) (mContext)).startActivity(browserIntent);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        // Load image as textures
        // texture position marker
        background_sprite.SetupImage("drawable/background_day", 0, texturenames);
        the_tongue_sprite.SetupImage("drawable/tongue", 1, texturenames);
        menu_sprite.SetupImage("drawable/menu", 2, texturenames);

        SetupTextAndImageTiles();


        isSurfaceCreated = true;

        // the middle of the control stick of the volume control menus (position 0)
        middleXVCpos = menu_sprite.getX();
        minXVCpos = middleXVCpos - 150 * scale;
        maxXVCpos = middleXVCpos + 150 * scale;


        // Set the clear color to black
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1);


        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);


        // Create the shaders, solid color
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);
        // create empty OpenGL ES Program
        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();
        // add the vertex shader to program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);


        // Create the shaders, images
        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);
        // create empty OpenGL ES Program
        riGraphicTools.sp_Image = GLES20.glCreateProgram();
        // add the vertex shader to program
        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(riGraphicTools.sp_Image);

        // Text shader
        int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
        int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);

        riGraphicTools.sp_Text = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
        GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert);        // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_Text);                  // creates OpenGL ES program executables


        // Set our shader programm
        GLES20.glUseProgram(riGraphicTools.sp_Image);


        try {
            setLevel(level);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean isSurfaceCreated() {
        return isSurfaceCreated;
    }

    public void processTouchEvent(MotionEvent event) {

        if(isPaused) {
            int pointerCount = event.getPointerCount();

            for (int i = 0; i < pointerCount; i++) {
                final float x = event.getX(i);
                final float y = event.getY(i);
                int id = event.getPointerId(i);
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        if(showInstructions) {
                            instructionClicks++;
                            if(instructionClicks >= 2) {
                                instructionClicks = 0;
                                showInstructions = false;
                                returningFromInstructions = true;
                                isPaused = false;
                            }
                            break;
                        }

                        exit_TileObject.handleActionDown(x, (currScreenHeight - y));
                        play_TileObject.handleActionDown(x, (currScreenHeight - y));

                        musicControl_TileObject.handleActionDown(x, (currScreenHeight - y));
                        soundControl_TileObject.handleActionDown(x, (currScreenHeight - y));
                        confirm_no_TileObject.handleActionDown(x, (currScreenHeight - y));
                        confirm_yes_TileObject.handleActionDown(x, (currScreenHeight - y));
                        menu_sprite.handleActionDown(x, (currScreenHeight - y));

                        if(menu_sprite.isTouched()) {

                            if(y * scale < 300 * scale) {
                                goToPontus();
                            }
                        }


                        if(play_TileObject.isTouched()) {
                            showInstructions = true;
                            play_TileObject.setNotTouched();


                        } else if(exit_TileObject.isTouched()) {
                            exit_TileObject.setNotTouched();
                            confirmExit();
                        }
                        if(confirmingExit) {
                            if(confirm_no_TileObject.isTouched()) {
                                returnToPauseMenu();
                            }
                            if(confirm_yes_TileObject.isTouched()) {
                                onDestroy();
                            }
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if(musicControl_TileObject.isTouched()) {
                            // the stick is being dragged
                            float yPos = musicControl_TileObject.y;
                            float xPos = event.getX();
                            if(xPos >= minXVCpos && xPos <= maxXVCpos) {
                                musicControl_TileObject.moveTo(event.getX(), yPos);

                                //is control is near center (40 - center to center + 40)
                                if(xPos < (middleXVCpos + 10 * scale) && xPos > (middleXVCpos - 10 * scale)) {

                                    MusicManager.setVolume(0.5f);
                                    //((MainActivity) mContext).setMusicVolume(0.5f);
                                } else {
                                    if(xPos > middleXVCpos) {
                                        MusicManager.setVolume(0.75f);
                                        //((MainActivity) mContext).setMusicVolume(0.75f);
                                    } else {
                                        MusicManager.setVolume(0.25f);
                                        //((MainActivity) mContext).setMusicVolume(0.25f);
                                    }
                                }
                            } else if(xPos > maxXVCpos) {
                                MusicManager.setVolume(1f);
                                //((MainActivity) mContext).setMusicVolume(1f);
                                musicControl_TileObject.moveTo(maxXVCpos, yPos);

                            } else if(xPos < minXVCpos) {
                                MusicManager.setVolume(0f);
                                //((MainActivity) mContext).setMusicVolume(0f);
                                musicControl_TileObject.moveTo(minXVCpos, yPos);

                            }
                            break;
                        }

                        if(soundControl_TileObject.isTouched()) {
                            // the stick is being dragged
                            float yPos = soundControl_TileObject.y;
                            float xPos = event.getX();
                            if(xPos >= minXVCpos && xPos <= maxXVCpos) {
                                soundControl_TileObject.moveTo(event.getX(), yPos);

                                //is control is near center (40 - center to center + 40)
                                if(xPos < (middleXVCpos + 10 * scale) && xPos > (middleXVCpos - 10 * scale)) {
                                    froppySoundManager.setVolumeLevel(50f);
                                } else {
                                    if(xPos > middleXVCpos) {
                                        froppySoundManager.setVolumeLevel(75f);
                                    } else {
                                        froppySoundManager.setVolumeLevel(25f);
                                    }
                                }
                            } else if(xPos > maxXVCpos) {
                                soundControl_TileObject.moveTo(maxXVCpos, yPos);
                                froppySoundManager.setVolumeLevel(100f);

                            } else if(xPos < minXVCpos) {
                                soundControl_TileObject.moveTo(minXVCpos, yPos);
                                froppySoundManager.setVolumeLevel(0f);

                            }
                        }
                        break;
                    default:
                        ////System.out.println("SWITCH(" + action + ") has no case. On default (do nothing).");
                        break;

                }//CLOSE SWITCH

                //////System.out.println("ACTION: " + actionString);
            }//CLOSE FOR LOOP
        } // END IF ON PAUSE

        // RUN WHEN NOT PAUSED
        else {


            float orientation = event.getOrientation();
            int pointerCount = event.getPointerCount();

            for (int i = 0; i < pointerCount; i++) {
                final float x = event.getX(i);
                final float y = event.getY(i);
                int id = event.getPointerId(i);
                int action = event.getActionMasked();
                int actionIndex = event.getActionIndex();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:


                        pause_button_TileObject.handleActionDown(x, (currScreenHeight - y));


                        if(pause_button_TileObject.isTouched() && !isPaused) {
                            returnToPauseMenu();
                            isPaused = true;
                            break;

                        } else if(!(eelsCounter > 0)) {

                            int eyeMove = 18;

                            float realLeftEyePosX = tonguePivotX - 50 * scale;
                            float realLeftEyePosY = tonguePivotY - 150 * scale;
                            float realRightEyePosX = tonguePivotX + 50 * scale;
                            float realRightEyePosY = tonguePivotY - 150 * scale;


                            if(realLeftEyePosX > x) {
                                froppy_main_left_eye_tileObj.x -= eyeMove * scale;
                            } else {
                                froppy_main_left_eye_tileObj.x += eyeMove * scale;
                            }
                            if(realLeftEyePosY > y) {
                                froppy_main_left_eye_tileObj.y += eyeMove * scale;
                            } else {
                                froppy_main_left_eye_tileObj.y -= eyeMove * scale;
                            }
                            if(realRightEyePosX > x) {
                                froppy_main_right_eye_tileObj.x -= eyeMove * scale;
                            } else {
                                froppy_main_right_eye_tileObj.x += eyeMove * scale;
                            }
                            if(realRightEyePosY > y) {
                                froppy_main_right_eye_tileObj.y += eyeMove * scale;
                            } else {
                                froppy_main_right_eye_tileObj.y -= eyeMove * scale;
                            }

                            double angle = Math.atan2((currScreenHeight - y) - tongueY, x - tongueX);
                            the_tongue_sprite.setAngle((float) angle);

                            growTongueToPos(x, y);
                            froppySoundManager.playSound(SoundManager.WHIP);

                            if(ladybugs_showing) {
                                for (int lb_index = 0; lb_index < ladybugs.size(); lb_index++) {
                                    TileObject lb = ladybugs.get(lb_index);
                                    lb.handleActionDown(x, y);
                                    lb.handleActionDown(x, currScreenHeight - y);
                                    if(lb.isTouched()) {
                                        for (int ifly = 0; ifly < flies.size(); ifly++) {
                                            Fly aFly = flies.get(ifly);
                                            if(aFly.getType() == Fly.TYPE_BEE | aFly.getType() == Fly.TYPE_QUEEN_BEE) {
                                                flies.remove(ifly);
                                                ifly = 0;
                                            }
                                        }
                                        if(lb.getIndex() == 57 | lb.getIndex() == 58) {
                                            lb_left = false;
                                        } else {
                                            lb_right = false;
                                        }
                                    }
                                }
                            }
                            if(eels_showing) {
                                for (TileObject eel : eels) {
                                    eel.handleActionDown(x, y);
                                    eel.handleActionDown(x, currScreenHeight - y);
                                    if(eel.isTouched()) {
                                        eelsCounter = 80;
                                    }
                                }
                            }
                            for (Fly aFly : flies) {
                                aFly.handleActionDown(x, y);
                                aFly.handleActionDown(x, currScreenHeight - y);

                            }
                            for (int ifly = 0; ifly < flies.size(); ifly++) {
                                Fly aFly = flies.get(ifly);
                                if(aFly.isTouched()) {

                                    flyAteType = aFly.getType();
                                    flyAteCounter = 30;
                                    ateFly = true;
                                    if(flyAteType == Fly.TYPE_FLY | flyAteType == Fly.TYPE_FIRE_FLY | flyAteType == Fly.TYPE_DRAGON_FLY) {
                                        score++;
                                    }


                                    if(flyAteType == Fly.TYPE_BLUE_FLY) {
                                        score = score + 20;
                                        heartsCounter = 200;

                                        froppySoundManager.playSound(SoundManager.DELICIOUS);
                                    }

                                    if(flyAteType == Fly.TYPE_BEE) {
                                        score = score - 5;
                                        if(score < 0) score = 0;
                                        beeCounter = 10;
                                        froppySoundManager.playSound(SoundManager.OUCH);
                                    }

                                    if(flyAteType == Fly.TYPE_QUEEN_BEE) {
                                        queen_beeCounter = 10;
                                        froppySoundManager.playSound(SoundManager.OUCH);
                                    }

                                    if(flyAteType == Fly.TYPE_DRAGON_FLY) {
                                        fly_speed_level = fly_speed;
                                        dragonflyCounter = 100;
                                        froppySoundManager.playSound(SoundManager.DELICIOUS);
                                    }

                                    flies.remove(ifly);
                                    ifly = 0;
                                }
                            }

                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        // TO-DO
                        the_tongue_sprite.growFromLeft((float) 20);
                        the_tongue_sprite.setAngle((float) Math.PI / 2);

                        froppy_main_left_eye_tileObj.x = left_eye_posX;
                        froppy_main_left_eye_tileObj.y = left_eye_posY;
                        froppy_main_right_eye_tileObj.x = right_eye_posX;
                        froppy_main_right_eye_tileObj.y = right_eye_posY;
                        break;


                }//CLOSE SWITCH
            }//CLOSE FOR LOOP
        }// CLOSE ELSE: RUN WHEN NOT PAUSED
    }//CLOSE METHOD

    private boolean growTongueToPos(float x, float y) {

        the_tongue_sprite.handleActionDownTransformedVertices(x, (currScreenHeight - y));
        if(the_tongue_sprite.isTouched()) {
            tongueSize = 20;
            return true;
        }

        tongueSize += 80;
        if(tongueSize > 3000) {
            tongueSize = 20;
            return false;
        }

        the_tongue_sprite.growFromLeft(tongueSize);

        return growTongueToPos(x, y);
    }

    private void confirmExit() {
        isPaused = true;
        confirmingExit = true;
    }

    private void returnToPauseMenu() {
        returningToPause = true;
        returningFromPause = true;
        confirmingExit = false;
        isPaused = true;
    }


    private synchronized void setScore(int score_value) throws IOException {


        // Create our new textobject
        String tmp = String.valueOf(score);
        while (tmp.length() < 3) {
            tmp = "0" + tmp;
        }
        tm.removeText(score_text);
        score_text = new TextObject(tmp, scoreXpos, scoreYpos);
        tm.addText(score_text);
        // Prepare the text for rendering
        tm.PrepareDraw();
    }


    private void playWonAnimation() {

        // REMOVE WHEN MUSIC CHECKED OK!
       // ((MainActivity) mContext).mServ.stopMusic();

        MusicManager.release();
        MusicManager.start(mContext, MusicManager.MUSIC_END_GAME);

       // froppySoundManager.repeatSound(SoundManager.SONG,50);
        gameIsWon = true;

    }



    public void SetupTextAndImageTiles() {


        //  for the text texture
        int id = mContext.getResources().getIdentifier("drawable/font", null, mContext.getPackageName());
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[3]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();


        // Create our text manager
        tm = new TextManager();

        // Tell our text manager to use index 35 of textures loaded
        tm.setTextureID(3);

        // Pass the uniform scale
        tm.setUniformscale(scale * 4);

        // Position from center
        scoreYpos = center_stage.y - 650 * scale;
        levelYpos = scoreYpos;

        scoreXpos = center_stage.x - 670 * scale;


        // Create our new textobject

        score_text = new TextObject("000", scoreXpos, scoreYpos);

        levelXpos = center_stage.x + 470 * scale;

        level_text = new TextObject("000", levelXpos, levelYpos);

        timerXpos = center_stage.x + 1145 * scale;
        timerYpos = center_stage.y + 555 * scale;

        timer_text = new TextObject("00", timerXpos, timerYpos);

        // Add it to our manager
        tm.addText(timer_text);
        tm.addText(score_text);
        tm.addText(level_text);

        // Prepare the text for rendering
        tm.PrepareDraw();


        //-----------------------------------------------------------------


        //  for the images tile texture manager
        int id2 = mContext.getResources().getIdentifier("drawable/froppy_tile", null, mContext.getPackageName());
        Bitmap bmp2 = BitmapFactory.decodeResource(mContext.getResources(), id2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[4]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp2, 0);
        bmp2.recycle();

        // Create our image tile managers
        froppy_tile_manager = new FroppyTileManager();

        // Tell our manager to use index of textures loaded
        froppy_tile_manager.setTextureID(4);

        // Pass the uniform scale
        froppy_tile_manager.setUniformscale(scale * 2.5f);


        //-----------------------------------------------------------------

        //  for the BANNERS-SIGNS images tile texture manager
        int fliesTile = mContext.getResources().getIdentifier("drawable/flies_tile", null, mContext.getPackageName());
        Bitmap bmp1 = BitmapFactory.decodeResource(mContext.getResources(), fliesTile);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 5);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[5]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp1, 0);
        bmp1.recycle();

        // Create our image tile managers
        flies_manager = new FliesTileManager();

        // Tell our manager to use index of textures loaded
        flies_manager.setTextureID(5);

        // Pass the uniform scale
        flies_manager.setUniformscale(scale * 0.80f);


        //-------------------------------ojo aca poner tile de menuy cambiar # textura etc+}----------------------------------

        //  for the BANNERS-SIGNS images tile texture manager
        int bannersTile = mContext.getResources().getIdentifier("drawable/banners_tile", null, mContext.getPackageName());
        Bitmap bmp5 = BitmapFactory.decodeResource(mContext.getResources(), bannersTile);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 6);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[6]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp5, 0);
        bmp5.recycle();

        // Create our image tile managers
        banners_tile_manager = new LevelBannersTileManager();

        // Tell our manager to use index of textures loaded
        banners_tile_manager.setTextureID(6);
        banners_tile_manager.setUniformscale(scale * 10f);


        //-------------------------------ojo aca poner tile de menu y cambiar # textura etc+}----------------------------------

        //  for the BANNERS-SIGNS images tile texture manager
        int menuTile = mContext.getResources().getIdentifier("drawable/itemsmenu", null, mContext.getPackageName());
        Bitmap bmp3 = BitmapFactory.decodeResource(mContext.getResources(), menuTile);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 7);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[7]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp3, 0);
        bmp3.recycle();

        // Create our image tile managers
        menu_tile_manager = new MenuTileManager();

        // Tell our manager to use index of textures loaded
        menu_tile_manager.setTextureID(7);

        menu_tile_manager.setUniformscale(scale * 2.5f);

    }


}