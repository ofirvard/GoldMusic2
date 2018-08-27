package com.example.ofir.goldmusic2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ofir on 21-Jul-18.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>
{
    private ArrayList<Album> dataset;
    private TabAdapter tabAdapter;
    private Context context;
    private MusicPlayer musicPlayer;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;
        CardView card_view;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.textView = itemView.findViewById(R.id.title);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    public AlbumAdapter(ArrayList<Album> dataset, TabAdapter tabAdapter, Context context, MusicPlayer musicPlayer)
    {
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.context = context;
        this.musicPlayer = musicPlayer;
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
                tabAdapter.add(new SongAdapter(album.songs, tabAdapter, context, musicPlayer), 1);
            }
        });

        //todo set on click and long click (open menu)
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }
}
