package com.example.ofir.goldmusic2;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.Collections;

public class MusicPlayerHandler implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
{
    private MediaPlayer mediaPlayer;
    private PlaylistHandler playlistHandler;
    private PlaylistAdapter playlistAdapter;

    public MusicPlayerHandler(Context context)
    {
        if (this.mediaPlayer == null)
        {
            this.playlistHandler = new PlaylistHandler();
            this.mediaPlayer = new MediaPlayer();

            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnPreparedListener(this);
        }
        else
            this.mediaPlayer.reset();
    }

    public void setPlaylistAdapter(PlaylistAdapter playlistAdapter)
    {
        this.playlistAdapter = playlistAdapter;
    }

    // use only this
    void play(int i)
    {
        if (i != -1)
            try
            {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(playlistHandler.get(i).path);
                mediaPlayer.prepare();

                //// TODO: 3/11/2019 show info
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        else
            reset();
    }

    void reset()
    {
        mediaPlayer.stop();
        mediaPlayer.reset();
        // TODO: 3/11/2019  reset info shown and close notification
    }

    // releases the media player
    public void end()
    {
        mediaPlayer.release();
    }

    // if playing pauses, if paused plays and nothing playlist is empty
    public void pause()
    {
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            else
                mediaPlayer.start();
    }

    void nextSong()
    {
        play(playlistHandler.getNext());
    }

    void previousSong()
    {
        play(playlistHandler.getPrevious());
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        nextSong();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        playlistAdapter.notifyDataSetChanged();
        mediaPlayer.start();
    }

    void addSongs(ArrayList<Song> songs, boolean asNext)
    {
        boolean empty = playlistHandler.isEmpty();

        playlistHandler.addSongs(songs, asNext);

        if (empty)
            play(0);

        playlistAdapter.notifyDataSetChanged();
    }

    void addSongs(Song song, boolean asNext)
    {
        boolean empty = playlistHandler.isEmpty();

        playlistHandler.addSongs(song, asNext);

        if (empty)
            play(0);

        playlistAdapter.notifyDataSetChanged();
    }

    void remove(int i)
    {
        if (playlistHandler.getCurrent() == i)
            nextSong();
        playlistHandler.remove(i);

        playlistAdapter.notifyDataSetChanged();
    }

    void removeAll()
    {
        playlistHandler.removeAll();
        reset();
        playlistAdapter.notifyDataSetChanged();
    }

    void randomize()
    {
        playlistHandler.randomize();
        playlistAdapter.notifyDataSetChanged();
    }

    int getCurrent()
    {
        return playlistHandler.getCurrent();
    }

    void setCurrent(int i)
    {
        playlistHandler.setCurrent(i);
    }

    ArrayList<Song> getPlaylist()
    {
        return playlistHandler.getPlaylist();
    }
}
