package com.example.ofir.goldmusic2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ofir on 15-Aug-18.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>
{
    private ArrayList<Song> dataset;
    private TabAdapter tabAdapter;
    private Context context;
    private MusicPlayer musicPlayer;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView time;
        public ImageView imageView;
        CardView card_view;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.title = itemView.findViewById(R.id.title);
            this.time = itemView.findViewById(R.id.time);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    public SongAdapter(ArrayList<Song> dataset, TabAdapter tabAdapter, Context context, MusicPlayer musicPlayer)
    {
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.context = context;
        this.musicPlayer = musicPlayer;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card3, parent, false);

        return new SongAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position)
    {
        final Song song = dataset.get(position);
        holder.title.setText(song.title);
        holder.time.setText(song.durationS);

        Picasso.get().load(song.album.coverUri).placeholder(R.drawable.question_mark_low_res).into(holder.imageView);

        holder.card_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                musicPlayer.addAtEnd(song);
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
