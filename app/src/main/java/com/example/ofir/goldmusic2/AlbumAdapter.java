package com.example.ofir.goldmusic2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ofir on 21-Jul-18.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Album> dataset;
    private TabAdapter tabAdapter;
    private MusicPlayerHandler musicPlayerHandler;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;
        CardView card_view;

        ViewHolder(View itemView)
        {
            super(itemView);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.textView = itemView.findViewById(R.id.title);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    AlbumAdapter(Context context, ArrayList<Album> dataset, TabAdapter tabAdapter, MusicPlayerHandler musicPlayerHandler)
    {
        this.context = context;
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.musicPlayerHandler = musicPlayerHandler;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card2, parent, false);

        return new AlbumAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position)
    {
        final Album album = dataset.get(position);
        holder.textView.setText(album.name);

        Picasso.get().load(album.coverUri).placeholder(R.drawable.question_mark_low_res).into(holder.imageView);

        holder.card_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tabAdapter.add(new SongAdapter(context, album.songs, tabAdapter, musicPlayerHandler), 1);
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.album_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.play_next:
                                musicPlayerHandler.addSongs(album.songs, true);
                                break;

                            case R.id.add_all:
                                musicPlayerHandler.addSongs(album.songs, false);
                                break;

                            case R.id.go_to_artist_songs:
                                tabAdapter.add(new SongAdapter(context, album.artistPath.getSongs(), tabAdapter, musicPlayerHandler), 1);
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
}
