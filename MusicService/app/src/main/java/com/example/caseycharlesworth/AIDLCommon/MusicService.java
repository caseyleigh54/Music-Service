/*
    @Author: Casey Charlesworth
    Project 5
    Application: Music Service
    U. of Illinois at Chicago
    CS 478 Spring 2018

 */

package com.example.caseycharlesworth.AIDLCommon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import  com.example.caseycharlesworth.musicservice.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicService extends Service {
    private String Tag = "ServiceApp";
    private static String LOG_TAG = "Music Service";
    private MediaPlayer mPlayer;
    ArrayList<Integer> songIds;
    boolean mMediaStarted;
    private int mStartID;
    List<String> songTitles;
    int mSongId;

    public MusicService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();

        mMediaStarted = false;

        //Songs to be played
        songIds = new ArrayList<>();
        songIds.add(R.raw.badnews);
        songIds.add(R.raw.chadcrouch);
        songIds.add(R.raw.podington);

        //Song titles
        songTitles = Arrays.asList(getResources().getStringArray(R.array.song_titles));

        if (mPlayer != null){
            mPlayer.setLooping(false);

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSelf();
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }
            });
        }




    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid){
        return START_NOT_STICKY;
    }

    // Create new media player for change of song & start of service
    private void startNewSong(int songId){
        mPlayer = MediaPlayer.create(this, songIds.get(songId));
        mMediaStarted = true;
    }

    @Override
    public void onRebind(Intent intent){
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }
    @Override
    public boolean onUnbind(Intent intent){
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }


    @Override
    public void onDestroy() {

        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Tag, "On Bind");
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    //Shared AIDL
    private final IAdd.Stub mBinder = new IAdd.Stub() {
        @Override
        //Pause/play current song, else start from first song in list
        public String toggleSong() throws RemoteException{
            if(!mMediaStarted){
                Log.d(Tag, "On Start Command");
                startNewSong(0);
                mSongId = 0;
            }
            //String id = intent.getStringExtra("cmd");
            if (mPlayer != null) {

                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    //notifyMusicStatus(1);
                } else {
                    mPlayer.start(); }
            }
            return "hello";
        }
        // returns list of song titles avaliable to play
        public List<String> songList(){
            return Arrays.asList(getResources().getStringArray(R.array.song_titles));
        }
        // stops current song, plays new song
        public void playSong(int id){
            if (mPlayer != null) {mPlayer.stop();}
            //mPlayer.release();
            mSongId = id;
            startNewSong(id);
            mPlayer.start();
        }
        // returns current song title being played
        public String currentSong(){
            return songTitles.get(mSongId);
        }
        // returns status of media player
        // options: "stop", "start", "paused"
        public String getStatus(){
            if(mPlayer == null){
                return "stop";
            }
            else if(mPlayer.isPlaying()){
                return "play";
            }
            else{
                return "pause";
            }
        }
        //stops media player
        public void stopPlayer(){
            mPlayer.stop();
            mPlayer = null;
            mMediaStarted = false;
        }

    };


}
