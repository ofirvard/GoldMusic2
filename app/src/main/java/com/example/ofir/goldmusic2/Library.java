package com.example.ofir.goldmusic2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ofir on 20-Jul-18.
 */

public class Library
{
    ArrayList<Artist> artists = new ArrayList<>();
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<Song> songs = new ArrayList<>();
    Context context;

    Library(Context context)
    {
        this.context = context;
    }

    void setup()
    {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            //get columns
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do
            {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                long duration = cursor.getLong(durationColumn);
                String path = cursor.getString(dataColumn);
                long albumId = cursor.getLong(albumIdColumn);

                if (artist.equals("<unknown>"))
                    artist = "Unknown";
                if (album.equals("<unknown>"))
                    album = "Unknown";

                songs.add(new Song(title, album, artist, duration, id, path, albumId));
            }
            while (cursor.moveToNext());
        }
        try
        {
            cursor.close();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        sortSongs();
        loadCoverUri();
    }

    private void sortSongs()
    {
        boolean foundArtist;
        for (Song song : songs)
        {
            foundArtist = false;
            for (Artist artist : artists)
            {
                if (song.artistName.equals(artist.name))
                {
                    artist.addSong(song);
                    foundArtist = true;
                    break;
                }
            }
            if (!foundArtist)
                artists.add(new Artist(song));
        }

        // sorts all songs
        sortSongs(songs);

        // sort all artists
        sortArtists(artists);

        // sort artists albums and albums songs
        for (Artist artist : artists)
        {
            //albums
            sortAlbums(artist.albums);

            for (Album album : artist.albums)
            {
                //sort album songs
                sortSongs(album.songs);

                albums.add(album);
            }
        }

        // sort all albums list
        sortAlbums(albums);
    }

    private void loadCoverUri()
    {
        for (Artist artist : artists)
            for (Album album : artist.albums)
                album.coverUri = getAlbumUri(album.albumId);
    }

    private Uri getAlbumUri(long album_id)
    {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri imageUri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (checkUri(imageUri))
            return imageUri;
        else
            return null;
    }

    private boolean checkUri(Uri contentUri)
    {
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(contentUri, projection, null, null, null);
        if (cur != null)
        {
            if (cur.moveToFirst())
            {
                String filePath = cur.getString(0);

                if (new File(filePath).exists())
                {
                    // do something if it exists
                    cur.close();
                    return true;
                }
                else
                {
                    // File was not found
                    cur.close();
                    return false;
                }
            }
            else
            {
                // Uri was ok but no entry found.
                cur.close();
                return false;
            }
        }
        else
        {
            // content Uri was invalid or some other error occurred
            cur.close();
            return false;
        }
    }

    private static Comparator<Album> albumComparator = new Comparator<Album>()
    {
        @Override
        public int compare(Album o1, Album o2)
        {
            return o1.name.compareTo(o2.name);
        }
    };

    private static Comparator<Artist> artistComparator = new Comparator<Artist>()
    {
        @Override
        public int compare(Artist o1, Artist o2)
        {
            return o1.name.compareTo(o2.name);
        }
    };

    private static Comparator<Song> songComparator = new Comparator<Song>()
    {
        @Override
        public int compare(Song o1, Song o2)
        {
            return o1.title.compareTo(o2.title);
        }
    };

    public static void sortArtists(ArrayList<Artist> list)
    {
        Collections.sort(list, artistComparator);
    }

    public static void sortAlbums(ArrayList<Album> list)
    {
        Collections.sort(list, albumComparator);
    }

    public static void sortSongs(ArrayList<Song> list)
    {
        Collections.sort(list, songComparator);
    }
}
