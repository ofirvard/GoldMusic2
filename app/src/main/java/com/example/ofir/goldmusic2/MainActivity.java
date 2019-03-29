package com.example.ofir.goldmusic2;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnStartDragListener
{
    Library library = new Library(this);
    //    private MusicPlayerService musicPlayerService;
//    private PlaylistHandler playlistHandler;
    private MusicPlayerHandler musicPlayerHandler;
    private RecyclerView playlistView;
    private PlaylistAdapter playlistAdapter;
    private ArtistAdapter artistAdapter;
    private CustomViewPager pager;
    private TabAdapter tabAdapter;
    private Toolbar toolbar;
    private boolean playlistShowing = false;
    private DrawerLayout drawer;
    private Menu toolbarMenu;
    private ItemTouchHelper touchHelper;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK}, 1);

        musicPlayerHandler = new MusicPlayerHandler(this);
//        playlistHandler = new PlaylistHandler();
//        musicPlayerService = new MusicPlayerService(playlistHandler, getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.my_drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset)
            {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView)
            {
                toolbarMenu.findItem(R.id.playlist_button).setIcon(R.drawable.playlist_on);
                playlistShowing = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView)
            {
                toolbarMenu.findItem(R.id.playlist_button).setIcon(R.drawable.playlist_off);
                playlistShowing = false;
            }

            @Override
            public void onDrawerStateChanged(int newState)
            {

            }
        });
        pager = findViewById(R.id.pager);
        pager.setPagingEnabled(false);
        tabAdapter = new TabAdapter(getApplicationContext(), pager);
        pager.setAdapter(tabAdapter);
        pager.setOffscreenPageLimit(4);

        playlistView = findViewById(R.id.playlist);
        playlistView.setHasFixedSize(true);
        playlistView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        playlistAdapter = new PlaylistAdapter(this, tabAdapter, musicPlayerHandler, this, drawer);
        playlistView.setAdapter(playlistAdapter);
        musicPlayerHandler.setPlaylistAdapter(playlistAdapter);

//        musicPlayerService.setPlaylistAdapter(playlistAdapter);
//        playlistHandler.setPlaylistAdapter(playlistAdapter);
//        playlistHandler.setMusicPlayerService(musicPlayerService);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(playlistAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(playlistView);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
//            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            //get service
//            musicPlayerService = binder.getService();
            //pass list
//            musicPlayerService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            musicBound = false;
        }
    };

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)//read permission
        {
            Long start = System.currentTimeMillis();
            library.setup();
            Long end = System.currentTimeMillis();

            artistAdapter = new ArtistAdapter(this, library.artists, tabAdapter, musicPlayerHandler);

            tabAdapter.add(artistAdapter, 2);

            Long end2 = System.currentTimeMillis();

            Toast.makeText(this, "it took " + (end - start) + " milliseconds to load library,\n" +
                    " and " + (end2 - end) + " milliseconds to load pager", Toast.LENGTH_LONG).show();

//            startService();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//     Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        toolbarMenu = menu;

        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder)
    {
        touchHelper.startDrag(viewHolder);
    }

    public void playlist(MenuItem item)
    {
        if (!playlistShowing)
        {
            item.setIcon(R.drawable.playlist_on);
            playlistShowing = true;
            drawer.openDrawer(Gravity.END);
        }
        else
        {
            item.setIcon(R.drawable.playlist_off);
            playlistShowing = false;
            drawer.closeDrawer(Gravity.END);
        }
    }

    public void randomize(MenuItem item)
    {
        drawer.closeDrawer(Gravity.END);
        musicPlayerHandler.randomize();
    }

    public void clear_playlist(MenuItem item)
    {
        drawer.closeDrawer(Gravity.END);
        musicPlayerHandler.removeAll();
    }

    @Override
    protected void onDestroy()
    {
//        musicPlayerService.end();
        //todo end notification here too
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (tabAdapter.getCount() > 0)
            tabAdapter.remove(pager.getCurrentItem());
        else
            super.onBackPressed();
    }

    public void startService()
    {
        if (playIntent == null)
        {
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
}
