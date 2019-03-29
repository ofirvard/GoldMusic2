package com.example.ofir.goldmusic2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ofir on 21-Aug-18.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter
{
    private Context context;
    private ArrayList<Song> dataset;
    private TabAdapter tabAdapter;
    private MusicPlayerHandler musicPlayerHandler;
    private final OnStartDragListener mDragStartListener;
    private DrawerLayout drawer;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView time;
        ImageView drag;
        ImageView imageView;
        CardView card_view;

        ViewHolder(View itemView)
        {
            super(itemView);
            this.drag = itemView.findViewById(R.id.drag);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.title = itemView.findViewById(R.id.title);
            this.time = itemView.findViewById(R.id.time);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    public PlaylistAdapter(Context context, TabAdapter tabAdapter, MusicPlayerHandler musicPlayerHandler, OnStartDragListener mDragStartListener, DrawerLayout drawer)
    {
        this.context = context;
        this.dataset = musicPlayerHandler.getPlaylist();
        this.tabAdapter = tabAdapter;
        this.musicPlayerHandler = musicPlayerHandler;
        this.mDragStartListener = mDragStartListener;
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
        if (musicPlayerHandler.getCurrent() == i)
            holder.card_view.setBackgroundColor(context.getResources().getColor(R.color.pink));
        else
            holder.card_view.setBackgroundColor(context.getResources().getColor(R.color.grey_dark));

        Picasso.get().load(song.album.coverUri).placeholder(R.drawable.question_mark_low_res).into(holder.imageView);

        final View.OnClickListener play = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                musicPlayerHandler.play(i);
                notifyDataSetChanged();
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

        View.OnLongClickListener openMenu = new View.OnLongClickListener()
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
                                tabAdapter.add(new SongAdapter(context, song.album.songs, tabAdapter, musicPlayerHandler), 1);
                                drawer.closeDrawer(Gravity.END);
                                break;

                            case R.id.go_to_artist:
                                tabAdapter.add(new AlbumAdapter(context, song.album.artistPath.albums, tabAdapter, musicPlayerHandler), 2);
                                drawer.closeDrawer(Gravity.END);
                                break;

                            case R.id.go_to_artist_songs:
                                tabAdapter.add(new SongAdapter(context, song.album.artistPath.getSongs(), tabAdapter, musicPlayerHandler), 1);
                                drawer.closeDrawer(Gravity.END);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

                return true;
            }
        };

        holder.time.setOnLongClickListener(openMenu);
        holder.title.setOnLongClickListener(openMenu);
        holder.imageView.setOnLongClickListener(openMenu);

    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }

    @Override
    public void onItemDismiss(int position)
    {
        musicPlayerHandler.remove(position);
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

        if (fromPosition == musicPlayerHandler.getCurrent())
            musicPlayerHandler.setCurrent(toPosition);

        return true;
    }
}

