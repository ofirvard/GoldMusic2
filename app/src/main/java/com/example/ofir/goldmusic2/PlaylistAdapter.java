package com.example.ofir.goldmusic2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ofir on 21-Aug-18.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter
{
    private ArrayList<Song> dataset;
    private TabAdapter tabAdapter;
    private Context context;
    private MusicPlayer musicPlayer;
    private final OnStartDragListener mDragStartListener;
    private DrawerLayout drawer;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView time;
        ImageView drag;
        ImageView imageView;
        CardView card_view;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.drag = itemView.findViewById(R.id.drag);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.title = itemView.findViewById(R.id.title);
            this.time = itemView.findViewById(R.id.time);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    public PlaylistAdapter(ArrayList<Song> dataset, TabAdapter tabAdapter, Context context, MusicPlayer musicPlayer, OnStartDragListener dragStartListener, DrawerLayout drawer)
    {
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.context = context;
        this.musicPlayer = musicPlayer;
        this.mDragStartListener = dragStartListener;
        this.drawer = drawer;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card4, parent, false);

        return new PlaylistAdapter.ViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final PlaylistAdapter.ViewHolder holder, int position)
    {
        final Song song = dataset.get(position);
        final int i = position;
        holder.title.setText(song.title);
        holder.time.setText(song.durationS);

        Picasso.get().load(song.album.coverUri).placeholder(R.drawable.question_mark_low_res).into(holder.imageView);

        View.OnClickListener play = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                musicPlayer.play(i);
            }
        };
        holder.time.setOnClickListener(play);
        holder.title.setOnClickListener(play);
        holder.imageView.setOnClickListener(play);

        holder.drag.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                    mDragStartListener.onStartDrag(holder);
                return false;
            }
        });


        holder.card_view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.playlist_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.go_to_album:
                                tabAdapter.add(new SongAdapter(song.album.songs, tabAdapter, context, musicPlayer), 1);
                                drawer.closeDrawer(Gravity.END);
                                break;

                            case R.id.go_to_artist:
                                tabAdapter.add(new AlbumAdapter(song.album.artistPath.albums, tabAdapter, context, musicPlayer), 2);
                                drawer.closeDrawer(Gravity.END);
                                break;

                            case R.id.go_to_artist_songs:
                                tabAdapter.add(new SongAdapter(song.album.artistPath.getSongs(), tabAdapter, context, musicPlayer), 1);
                                drawer.closeDrawer(Gravity.END);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

    @Override
    public void onItemDismiss(int position)
    {
        //todo add effect to music player
//        dataset.remove(position);
        musicPlayer.remove(position);//todo check if works
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        //todo add effect to music player
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(dataset, i, i + 1);
            }
        }
        else
        {
            for (int i = fromPosition; i > toPosition; i--)
            {
                Collections.swap(dataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}

