package com.example.ofir.goldmusic2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ofir on 05-Oct-18.
 * This will handle the playlist, all requests and inserts will be handled by it
 */

class PlaylistHandler
{
    private ArrayList<Song> playlist = new ArrayList<>();
    private int current = -1;

    Song get(int i)
    {
        current = i;
        return playlist.get(i);
    }

    int getNext()
    {
        if (hasNext())
            return ++current;
        return current = -1;
//        return -1;
    }

    private boolean hasNext()
    {
        return current < playlist.size() - 1;
    }

    int getPrevious()
    {
        if (hasPrevious())
            return --current;

        return isEmpty() ? -1 : 0;
    }

    private boolean hasPrevious()
    {
        return current > 0;
    }

    void addSongs(List<Song> songs, boolean asNext)
    {
        if (asNext)
            playlist.addAll(current + 1, songs);
        else
            playlist.addAll(songs);
    }

    void addSongs(Song song, boolean asNext)
    {
        if (asNext)
            playlist.add(current + 1, song);
        else
            playlist.add(song);
    }

    void remove(int i)
    {
        if (current >= i)
            current--;
        playlist.remove(i);
    }

    void removeAll()
    {
        playlist.clear();
        current = -1;
    }

    void randomize()
    {
        if (playlist.size() > 1)
        {
            Song song = playlist.get(current);
            playlist.remove(current);
            Collections.shuffle(playlist);
            playlist.add(0, song);
            current = 0;
        }
    }

    int size()
    {
        return playlist.size();
    }

    int getCurrent()
    {
        return current;
    }

    public void setCurrent(int current)
    {
        this.current = current;
    }

    public ArrayList<Song> getPlaylist()
    {
        return playlist;
    }

    boolean isEmpty()
    {
        return playlist.isEmpty();
    }
}
