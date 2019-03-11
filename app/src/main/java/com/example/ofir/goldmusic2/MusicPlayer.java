package com.example.ofir.goldmusic2;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.PowerManager;

import java.util.ArrayList;

/**
 * Created by ofir on 21-Aug-18.
 */

public class MusicPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnPreparedListener
{
    MediaPlayer mediaPlayer;
    PlaylistHandler playlistHandler;
    PlaylistAdapter playlistAdapter;

    // sets up the media player and todo notification controls
    MusicPlayer(final PlaylistHandler playlistHandler, Context context)
    {
        if (this.mediaPlayer == null)
        {
            this.playlistHandler = playlistHandler;

            this.mediaPlayer = new MediaPlayer();

            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnSeekCompleteListener(this);
            this.mediaPlayer.setOnPreparedListener(this);
        }
        else
            this.mediaPlayer.reset();
    }

    void setPlaylistAdapter(PlaylistAdapter playlistAdapter)
    {
        this.playlistAdapter = playlistAdapter;
    }

    // releases the media player
    public void end()
    {
        mediaPlayer.release();
    }

    // use only this
    void play(Song song)
    {
        try
        {

            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.path);
            mediaPlayer.prepare();

            //// TODO: 3/11/2019 show info
        } catch (Exception e)
        {
            e.printStackTrace();
            reset();
        }
    }

    // clears music player amd info shown
    void reset()
    {
        mediaPlayer.reset();
        // TODO: 3/11/2019  reset info shown and close notification
    }

    // if playing pauses, if paused plays and nothing playlist is empty
    public void pause()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    void next()
    {
        if (playlistHandler.hasNext())
            play(playlistHandler.getNext());
        else
        {
            playlistHandler.current = -1;
            reset();
        }
    }

    void previous()
    {
        if (playlistHandler.hasPrevious())
            play(playlistHandler.getPrevious());
        else
        {
            playlistHandler.current = -1;
            reset();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        next();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mediaPlayer.start();
        playlistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp)
    {

    }
}
