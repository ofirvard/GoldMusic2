package com.example.ofir.goldmusic2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ofir on 05-Oct-18.
 * This will handle the playlist, all requests and inserts will be handled by it
 */

public class PlaylistHandler
{
    MusicPlayer musicPlayer;
    PlaylistAdapter playlistAdapter;
    /*private */ ArrayList<Song> playlist = new ArrayList<>();
    int current = -1;

    void setMusicPlayer(MusicPlayer musicPlayer)
    {
        this.musicPlayer = musicPlayer;
    }

    void setPlaylistAdapter(PlaylistAdapter playlistAdapter)
    {
        this.playlistAdapter = playlistAdapter;
    }

    Song get(int i)
    {
        current = i;
        playlistAdapter.notifyDataSetChanged();
        return playlist.get(i);
    }

    Song getNext()
    {
        if (current + 1 < playlist.size())
            return playlist.get(++current);
        return null;
    }

    boolean hasNext()
    {
        return current < playlist.size() - 1;
    }

    Song getPrevious()
    {
        if (current > 0)
            return playlist.get(--current);
        return null;
    }

    boolean hasPrevious()
    {
        return current > 0;
    }

    private void wasEmpty()
    {
        current = 0;
        musicPlayer.play(playlist.get(0));
    }

    void addNext(Song song)
    {
        boolean empty = playlist.isEmpty();

        playlist.add(current + 1, song);
        playlistAdapter.notifyDataSetChanged();

        if (empty)
            wasEmpty();
    }

    void addAtEnd(Song song)
    {
        boolean empty = playlist.isEmpty();

        playlist.add(song);
        playlistAdapter.notifyDataSetChanged();

        if (empty)
            wasEmpty();
    }

    void addSongs(ArrayList<Song> songs, boolean asNext)
    {
        boolean empty = playlist.isEmpty();

        if (asNext)
            playlist.addAll(current + 1, songs);
        else
            playlist.addAll(songs);

        if (empty)
            wasEmpty();

        playlistAdapter.notifyDataSetChanged();
    }

    void addAlbum(Album album, boolean asNext)
    {
        boolean empty = playlist.isEmpty();

        if (asNext)
            playlist.addAll(current + 1, album.songs);
        else
            playlist.addAll(album.songs);

        if (empty)
            wasEmpty();

        playlistAdapter.notifyDataSetChanged();
    }

    void addArtist(Artist artist, boolean asNext)
    {
        boolean empty = playlist.isEmpty();

        if (asNext)
            playlist.addAll(current + 1, artist.getSongs());
        else
            playlist.addAll(artist.getSongs());

        if (empty)
            wasEmpty();

        playlistAdapter.notifyDataSetChanged();
    }

    void remove(int i)
    {
        if (current < i)
        {
            playlist.remove(i);
        }
        else if (current > i)
        {
            playlist.remove(i);
            current--;
        }
        else if (current == i)
        {
            if (hasNext())
                musicPlayer.play(getNext());
            else
            {
                musicPlayer.reset();
                current = -1;
            }

            remove(i);
        }

        playlistAdapter.notifyDataSetChanged();
    }

    void removeAll()
    {
        playlist.clear();
        musicPlayer.reset();
        current = -1;
        playlistAdapter.notifyDataSetChanged();
    }

    void randomize()
    {
        Song song = playlist.get(current);
        playlist.remove(current);
        Collections.shuffle(playlist);
        playlist.add(0, song);
        current = 0;

        playlistAdapter.notifyDataSetChanged();
    }

    int size()
    {
        return playlist.size();
    }
}
