package com.example.ofir.goldmusic2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ofir on 21-Aug-18.
 */

public class MusicPlayerService //extends Service
{
//    public static final String ACTION_PLAY = "action_play";
//    public static final String ACTION_PAUSE = "action_pause";
//    public static final String ACTION_NEXT = "action_next";
//    public static final String ACTION_PREVIOUS = "action_previous";
//    public static final String ACTION_STOP = "action_stop";
//
//    private PlaylistHandler playlistHandler;
//    private PlaylistAdapter playlistAdapter;
//    private MediaSession mediaSession;
//    private MediaController mediaController;
//    private final IBinder localBinder = new MusicBinder();
//
//    public class MusicBinder extends Binder
//    {
//        MusicPlayerService getService()
//        {
//            return MusicPlayerService.this;
//        }
//    }
//
//    // sets up the media player and todo notification controls
//    MusicPlayerService(final PlaylistHandler playlistHandler, Context context)
//    {
//        super();
//
//        if (this.mediaPlayer == null)
//        {
//            this.playlistHandler = playlistHandler;
//
//            this.mediaPlayer = new MediaPlayer();
//
//            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            this.mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
//            this.mediaPlayer.setOnCompletionListener(this);
//            this.mediaPlayer.setOnErrorListener(this);
//            this.mediaPlayer.setOnSeekCompleteListener(this);
//            this.mediaPlayer.setOnPreparedListener(this);
//        }
//        else
//            this.mediaPlayer.reset();
//    }
//
//    public MusicPlayerService()
//    {
//        super();
//    }
//
//    void setPlaylistAdapter(PlaylistAdapter playlistAdapter)
//    {
//        this.playlistAdapter = playlistAdapter;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent)
//    {
//        return localBinder;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent)
//    {
////        mediaPlayer.stop();
////        mediaPlayer.release();
//        mediaSession.release();
//        return super.onUnbind(intent);
//    }
//
//    private void handleIntent(Intent intent)
//    {
//        if (intent == null || intent.getAction() == null)
//            return;
//
//        switch (intent.getAction())
//        {
//            case ACTION_PLAY:
//                mediaController.getTransportControls().play();
//                break;
//
//            case ACTION_PAUSE:
//                mediaController.getTransportControls().pause();
//                break;
//
//            case ACTION_NEXT:
//                mediaController.getTransportControls().skipToNext();
//                break;
//
//            case ACTION_PREVIOUS:
//                mediaController.getTransportControls().skipToPrevious();
//                break;
//
//            case ACTION_STOP:
//                mediaController.getTransportControls().stop();
//
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        initMediaSessions();
//
//        handleIntent(intent);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void initMediaSessions()
//    {
//        mediaSession = new MediaSession(getApplicationContext(), "gold music 2");
//        mediaController = new MediaController(getApplicationContext(), mediaSession.getSessionToken());
//
//        mediaSession.setCallback(new MediaSession.Callback()
//                                 {
//                                     @Override
//                                     public void onPlay()
//                                     {
//                                         super.onPlay();
//                                         Log.e("MediaPlayer", "onPlay");
//                                         Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
//                                         pause();
//                                         buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
//                                     }
//
//                                     @Override
//                                     public void onPause()
//                                     {
//                                         super.onPause();
//                                         Log.e("MediaPlayer", "onPause");
//                                         Toast.makeText(getApplicationContext(), "pause", Toast.LENGTH_SHORT).show();
//                                         pause();
//                                         buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
//                                     }
//
//                                     @Override
//                                     public void onSkipToNext()
//                                     {
//                                         super.onSkipToNext();
//                                         Log.e("MediaPlayer", "onSkipToNext");
//                                         Toast.makeText(getApplicationContext(), "nextSong", Toast.LENGTH_SHORT).show();
//                                         next();
//                                         buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
//                                     }
//
//                                     @Override
//                                     public void onSkipToPrevious()
//                                     {
//                                         super.onSkipToPrevious();
//                                         Log.e("MediaPlayer", "onSkipToPrevious");
//                                         Toast.makeText(getApplicationContext(), "previousSong", Toast.LENGTH_SHORT).show();
//                                         previous();
//                                         buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
//                                     }
//
//                                     @Override
//                                     public void onStop()
//                                     {
//                                         super.onStop();
//                                         Log.e("MediaPlayer", "onStop");
//                                         //Stop media player here
//                                         NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                                         notificationManager.cancel(1);
//                                         Intent intent = new Intent(getApplicationContext(), MediaPlayer.class);
//                                         stopService(intent);
//                                     }
//                                 }
//        );
//    }
//
//
//    private void buildNotification(Notification.Action action)
//    {
//        Notification.MediaStyle style = new Notification.MediaStyle();
//
//        Intent intent = new Intent(getApplicationContext(), MediaPlayer.class);
//        intent.setAction(ACTION_PAUSE);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle("Media Title")
//                .setContentText("Media Artist")
//                .setDeleteIntent(pendingIntent)
//                .setStyle(style);
//
//        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
//        builder.addAction(action);
//        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
//        style.setShowActionsInCompactView(1);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
//    }
//
//    private Notification.Action generateAction(int icon, String title, String intentAction)
//    {
//        Intent intent = new Intent(getApplicationContext(), MusicPlayerService.class);
//        intent.setAction(intentAction);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//        return new Notification.Action.Builder(icon, title, pendingIntent).build();
//    }
//
//    @Override
//    public void onDestroy()
//    {
//        super.onDestroy();
//        //stopping the player when service is destroyed
//        mediaPlayer.stop();
//    }
}
