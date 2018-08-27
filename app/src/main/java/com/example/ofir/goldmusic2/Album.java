package com.example.ofir.goldmusic2;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by ofir on 20-Jul-18.
 */

public class Album
{
    String name;
    String artistName;
    ArrayList<Song> songs = new ArrayList<>();
    Artist artistPath;
    Uri coverUri = null;

    long albumId;

    Album(String name, String artistName, ArrayList<Song> songs, Artist artistPath)
    {
        this.name = name;
        this.artistName = artistName;
        this.songs = songs;
        this.artistPath = artistPath;
    }

    Album(Song song, Artist artist)
    {
        this.albumId = song.albumId;
        this.name = song.albumName;
        this.artistName = song.artistName;
        song.album = this;
        songs.add(song);
        this.artistPath = artist;
    }

    void add(Song song)
    {
        song.album = this;
        songs.add(song);
    }

    public String toString()
    {
        return name + "|" + artistName;
    }
}
