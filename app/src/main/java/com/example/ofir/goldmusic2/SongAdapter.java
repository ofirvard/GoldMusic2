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
 * Created by ofir on 15-Aug-18.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Song> dataset;
    private TabAdapter tabAdapter;
    private MusicPlayerHandler musicPlayerHandler;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView time;
        public ImageView imageView;
        CardView card_view;

        ViewHolder(View itemView)
        {
            super(itemView);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.title = itemView.findViewById(R.id.title);
            this.time = itemView.findViewById(R.id.time);
            this.imageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    SongAdapter(Context context, ArrayList<Song> dataset, TabAdapter tabAdapter, MusicPlayerHandler musicPlayerHandler)
    {
        this.context = context;
        this.dataset = dataset;
        this.tabAdapter = tabAdapter;
        this.musicPlayerHandler = musicPlayerHandler;
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
                ArrayList<Song> songs = new ArrayList<>();
                songs.add(song);
                musicPlayerHandler.addSongs(songs, false);
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.song_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.play_next:
                                musicPlayerHandler.addSongs(song, true);
                                break;

                            case R.id.add_at_end:
                                musicPlayerHandler.addSongs(song, false);
                                break;

                            case R.id.add_all_next:
                                musicPlayerHandler.addSongs(dataset, true);
                                break;

                            case R.id.add_all_at_end:
                                musicPlayerHandler.addSongs(dataset, false);
                                break;

                            case R.id.go_to_album:
                                tabAdapter.add(new SongAdapter(context, song.album.songs, tabAdapter, musicPlayerHandler), 1);
                                break;

                            case R.id.go_to_artist:
                                tabAdapter.add(new AlbumAdapter(context, song.album.artistPath.albums,
                                        tabAdapter, musicPlayerHandler), 2);
                                break;

                            case R.id.go_to_artist_songs:
                                tabAdapter.add(new SongAdapter(context, song.album.artistPath.getSongs(), tabAdapter, musicPlayerHandler), 1);
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
