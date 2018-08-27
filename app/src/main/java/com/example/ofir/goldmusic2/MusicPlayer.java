package com.example.ofir.goldmusic2;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by ofir on 21-Aug-18.
 */

public class MusicPlayer
{
    MediaPlayer mediaPlayer;
    ArrayList<Song> playlist;
    int playing = 0;
    private boolean setup = false;
    private PlaylistAdapter playlistAdapter;

    // sets up the media player and todo notification controls
    MusicPlayer()
    {
        if (!setup)
        {
            playlist = new ArrayList();
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    public void setPlaylistAdapter(PlaylistAdapter playlistAdapter)
    {
        this.playlistAdapter = playlistAdapter;
    }

    // releases the media player
    public void end()
    {
        mediaPlayer.release();
    }

    // plays the song at position i, if out of bounds exception i is reset to 0
    public void play(int i)
    {
        try
        {
            Song song = playlist.get(i);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playing = i;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            reset();
        }
    }

    // clears music player amd info shown
    public void reset()
    {
        mediaPlayer.reset();
    }

    // if playing pauses, if paused plays and nothing playlist is empty
    public void pause()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    // currently playing song is the new first
    public void randomize()
    {
    }

    // if the playlist is empty first new song will be played
    public void addNext(Song song)
    {
    }

    public void addNext(ArrayList<Song> songs)
    {
    }

    public void addAtEnd(Song song)
    {
        playlistAdapter.notifyDataSetChanged();
        playlist.add(song);
        if (playlist.size() == 1)
        {
            play(0);
        }
    }

    public void addAtEnd(ArrayList<Song> songs)
    {
        playlistAdapter.notifyDataSetChanged();
        playlist.addAll(songs);
        if (playlist.size() == 1)
        {
            play(0);
        }
    }

    // if i dosent exist do nothing
    public void remove(int i)
    {
        playlistAdapter.notifyDataSetChanged();
        try
        {
            if (i == playing)
                next();
            playlist.remove(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removeAll()
    {
    }

    // if dosent exist resets to 0
    public void next()
    {
        if (playing < playlist.size() - 1)
            play(playing + 1);
        else
            reset();
    }

    public void previous()
    {
    }
}
