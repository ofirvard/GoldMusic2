package com.example.ofir.goldmusic2;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by ofir on 20-Jul-18.
 */

public class Artist
{
    String name;
    ArrayList<Album> albums = new ArrayList<>();

    public Artist(Song song)
    {
        this.name = song.artistName;
        addSong(song);
    }

    public void addSong(Song song)
    {
        boolean found = false;
        for (Album album : albums)
        {
            if (album.name.equals(song.albumName))
            {
                found = true;
                album.add(song);
                break;
            }
        }
        if (!found)
        {
            albums.add(new Album(song, this));
        }
    }

    public ArrayList<Song> getSongs()//todo make long click open all
    {
        ArrayList<Song> songs = new ArrayList<>();
        for (Album album : albums)
        {
            songs.addAll(album.songs);
        }
        Library.sortSongs(songs);
        return songs;
    }

    public long getCover(int i)
    {
        int modulo = i % albums.size();
        return albums.get(modulo).albumId;
    }

    public Uri getCoverUri(int i)
    {
        int modulo = i % albums.size();
        return albums.get(modulo).coverUri;
    }
}
