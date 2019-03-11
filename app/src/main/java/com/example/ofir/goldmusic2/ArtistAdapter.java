package com.example.ofir.goldmusic2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofir on 20-Jul-18.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>
{
    private ArrayList<Artist> dataset;
    private TabAdapter tabAdapter;
    private Context context;
    private MusicPlayer musicPlayer;
    private PlaylistHandler playlistHandler;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        //        ImageView single;
        CardView card_view;
        ImageView top_left;
        ImageView top_right;
        ImageView bottom_left;
        ImageView bottom_right;

        ViewHolder(View itemView)
        {
            super(itemView);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.textView = itemView.findViewById(R.id.title);
//            this.single = itemView.findViewById(R.id.thumbnail);
            this.top_left = itemView.findViewById(R.id.top_left);
            this.top_right = itemView.findViewById(R.id.top_right);
            this.bottom_left = itemView.findViewById(R.id.bottom_left);
            this.bottom_right = itemView.findViewById(R.id.bottom_right);
        }
    }

    ArtistAdapter(ArrayList<Artist> dataset, TabAdapter tabAdapter, Context context,
                  MusicPlayer musicPlayer, PlaylistHandler playlistHandler)
    {
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.context = context;
        this.musicPlayer = musicPlayer;
        this.playlistHandler = playlistHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Artist artist = dataset.get(position);
        holder.textView.setText(artist.name);

        Picasso.get().load(artist.getCoverUri(0)).placeholder(R.drawable.question_mark_low_res).into(holder.top_left);
        Picasso.get().load(artist.getCoverUri(1)).placeholder(R.drawable.question_mark_low_res).into(holder.top_right);
        Picasso.get().load(artist.getCoverUri(2)).placeholder(R.drawable.question_mark_low_res).into(holder.bottom_left);
        Picasso.get().load(artist.getCoverUri(3)).placeholder(R.drawable.question_mark_low_res).into(holder.bottom_right);

        holder.card_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tabAdapter.add(new AlbumAdapter(artist.albums, tabAdapter, context, musicPlayer, playlistHandler), 2);
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.artist_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.play_next:
                                playlistHandler.addArtist(artist, true);
                                break;

                            case R.id.add_all:
                                playlistHandler.addArtist(artist, false);
                                break;

                            case R.id.show_all_songs:
                                tabAdapter.add(new SongAdapter(artist.getSongs(), tabAdapter, context, musicPlayer, playlistHandler), 1);
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