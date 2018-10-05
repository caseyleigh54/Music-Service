/*
    @Author: Casey Charlesworth
    Project 5
    Application: Music Client
    U. of Illinois at Chicago
    CS 478 Spring 2018

 */

package com.example.caseycharlesworth.musicclient;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caseycharlesworth.AIDLCommon.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Button playBttn;
    private Button stopBttn;
    protected  IAdd addService;
    private String Tag = "ClientApp";
    private List<String> songTitles;
    private ListView lv;
    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> requestsAdapter;
    boolean toggleBttn;
    List<String> requests;
    String mPlay = "play";
    String mPause = "pause";
    String mStop = "stop";
    String mItemSelected = "List Item Selected: ";
    final int STOP = 0;
    final int START = 1;
    final int PAUSE = 2;
    final int LIST_ITEM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag, "On Create");
        setContentView(R.layout.activity_main);

        /* Play/Pause Button
        *   requests music service to pause/play music player */

        toggleBttn = false; // change play/pause button text, pause = false, play = true

        playBttn = findViewById(R.id.playBttn);

        playBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(Tag, "On Click Listener");
                try {
                    addService.toggleSong(); //send request to music service
                    updateStatus(); //set text view to current status of music player

                    //Change text on play/pause button
                    if(toggleBttn){
                        playBttn.setText("Pause");
                        updateRequests(START, 0);
                    }else{
                        playBttn.setText("Play");
                        updateRequests(PAUSE, 0);
                    }

                    Log.d(Tag, "toggle song");

                } catch (RemoteException e){
                    Log.d(Tag, "Connection cannot be establish");
                    e.printStackTrace();
                }
            }
        });

        /* Stop Button
        *   Stops music player in music service*/

        stopBttn = findViewById(R.id.stopBttn);

        stopBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addService.stopPlayer();
                    updateStatus(); // update text view status of music player service
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        });


        /* Songs List View
         *   selected item from songs list is requested to be played.
         *   Request sent to music service
         *   Song titles stored in music service fill list view*/

        lv = findViewById(R.id.songsListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(addService != null){
                    try {
                        addService.playSong(position);
                        updateStatus(); // update status on text view
                        updateRequests(LIST_ITEM, position); // log requests
                        if(toggleBttn){
                            playBttn.setText("Pause");
                        }else{
                            playBttn.setText("Play");
                        }
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        /* Log requests from user
         *   Stored in list view
         *   Options: Play, Pause, Stop, List Item Selected */

        requests = new ArrayList<String>();
        requestsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

        ListView listView = (ListView) findViewById(R.id.requestView);
        listView.setAdapter(requestsAdapter);

        // Initialize connection & bind to service
        initConnection();
    }

    /* Initializes connection to service and binds service*/
    private void initConnection() {
        Log.d(Tag, "Service Connecting");
        if (addService == null) {
            Log.d(Tag, "Service Connected");
            Intent intent = new Intent(IAdd.class.getName());
            intent.setAction("service.music"); // set action
            intent.setPackage("com.example.caseycharlesworth.musicservice"); // set location
            bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);


        }
    }

    //Service connection
    //On connection, fill songs list view with song titles avaliable to play from music service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            addService = IAdd.Stub.asInterface((IBinder) iBinder);
            Log.d(Tag, "Service Bound");
            try{
                songTitles= addService.songList(); // songs titles avaliable from service
                fillListView(songTitles);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            addService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    // fill list view with song titles
    void fillListView(List<String> array){
        itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);

        ListView listView = (ListView) findViewById(R.id.songsListView);
        listView.setAdapter(itemsAdapter);
    }

    //Updates text view of current status of music player
    //Options: Playing "Song Title", Paused "Song Title", "Not Playing", "Music Player Stopped"
    private void updateStatus(){
        try{
            String res = addService.getStatus();
            TextView txtView = findViewById(R.id.txtView);
            if(res.equals("play")){
                txtView.setText("Now Playing: "+addService.currentSong());
                toggleBttn = true;
            }else if(res.equals("pause")){
                txtView.setText("Paused: "+addService.currentSong());
                toggleBttn = false;
            }else{
                txtView.setText("Music Player Stopped");
                updateRequests(STOP, 0);
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    //Adds each user request to requests view log
    private void updateRequests(int requestCode, int position){
        switch(requestCode){
            case STOP:
                requests.add(mStop);
                break;
            case START:
                requests.add(mPlay);
                break;
            case PAUSE:
                requests.add(mPause);
                break;
            case LIST_ITEM:
                requests.add(mItemSelected + Integer.toString(position));
                break;
        }
        requestsAdapter.notifyDataSetChanged();
    }



}
