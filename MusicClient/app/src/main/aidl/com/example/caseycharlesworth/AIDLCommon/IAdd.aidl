// IAdd.aidl
package com.example.caseycharlesworth.AIDLCommon;

// Declare any non-default types here with import statements

interface IAdd {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */


    String toggleSong();
    void playSong(int id);
    String currentSong();
    String getStatus();
    void stopPlayer();
    List<String> songList();
}
