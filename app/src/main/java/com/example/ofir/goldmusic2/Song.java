package com.example.ofir.goldmusic2;

/**
 * Created by ofir on 20-Jul-18.
 */

public class Song
{
    public String title;
    public String path;
    public String albumName;
    //    public String albumArtistName;
    public String artistName;
    public String durationS;
    public long duration;
    public long id;
    public long albumId;
    public Album album;

    public Song(String title, String albumName, String artistName, long duration, long id, String path, long albumId)
    {
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.duration = duration;
        this.id = id;
        this.path = path;
        this.albumId = albumId;

        //duration as string
        long temp = duration / 1000;
        long h = temp / 3600;
        long m = (temp - h * 3600) / 60;
        long s = temp - (h * 3600 + m * 60);
        String seconds;
        if (s < 10)
            seconds = "0" + s;
        else
            seconds = "" + s;

        if (h == 0)
            this.durationS = m + ":" + seconds;
        else
            this.durationS = h + ":" + m + ":" + seconds;
    }

    public String toString()
    {
        return title + "\n" +
                albumName + " | " + artistName;
    }

    public String info()
    {
        return albumName + " | " + artistName;
    }
}
