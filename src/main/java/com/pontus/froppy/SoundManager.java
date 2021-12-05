package com.pontus.froppy;

/**
 * Created by gabriel yibirin on 2/13/2016.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

    private  SoundPool mSoundPool;
    private  AudioManager  mAudioManager;

    private int[] sm;
    Context context;
    public static final int NUM_SOUNDS = 20;

    public static final int TASTY = 1;
    public static final int DELICIOUS = 2;
    public static final int CRUNCHY = 3;
    public static final int OUCH = 4;
    public static final int ELECTROCUTED = 5;
    public static final int BURP = 6;
    public static final int TASTE_GOOD = 7;
    public static final int WHIP = 8;
    public static final int YOU_GREAT = 9;

    private final float streamVolumeMax = 100f;
    private float volumeLevel = 50f;
    public boolean soundON = true;

    public SoundManager(Context theContext) {
        context = theContext;
        mSoundPool = new SoundPool(NUM_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        sm = new int[NUM_SOUNDS];

        sm[TASTY] = mSoundPool.load(context, R.raw.tasty, 1);
        sm[DELICIOUS] = mSoundPool.load(context, R.raw.delicious, 1);
        sm[CRUNCHY] = mSoundPool.load(context, R.raw.crunchy, 1);
        sm[OUCH] = mSoundPool.load(context, R.raw.ouch, 1);
        sm[ELECTROCUTED] = mSoundPool.load(context, R.raw.electrocuted, 1);
        sm[BURP] = mSoundPool.load(context, R.raw.burp, 1);
        sm[WHIP] = mSoundPool.load(context, R.raw.whip, 1);

        sm[TASTE_GOOD] = mSoundPool.load(context, R.raw.tastegood, 1);
        sm[YOU_GREAT] = mSoundPool.load(context, R.raw.great, 1);

        volumeLevel = 50f;
    }

    public void setVolumeLevel(float volume) {
        if (volume > 100 || volume < 0) {
            volume = 50;
            try {
                throw new Exception("volume value:" + volume + ". And Should be between 0-100. Set to 50 my default");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            volumeLevel = volume;
        }
    }

    public final void playSound(int sound) {

        if (soundON) {
            float volume = volumeLevel / streamVolumeMax;
            mSoundPool.play(sm[sound], volume, volume, 1, 0, 1f);
        }
    }
    public final void repeatSound(int sound, int times) {

        if (soundON) {
            float volume = volumeLevel / streamVolumeMax;
            mSoundPool.play(sm[sound], volume, volume, 1, times, 1f);
        }
    }

    public final void cleanUp() {
        sm = null;
        context = null;
        mSoundPool.release();
        mSoundPool = null;
    }

    public void turnOnSound() {
        soundON = true;

    }

    public void turnOffSound() {
        soundON = false;

    }



}
