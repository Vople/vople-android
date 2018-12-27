package com.mobile.vople.vople.server;

import android.content.Context;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by parkjaemin on 27/12/2018.
 */

public class MyUtils {

    public static boolean mergeMediaFiles(boolean isAudio, List<String> sourceFiles, String targetFile) {
        try {
            String mediaKey = isAudio ? "soun" : "vide";
            List<Movie> listMovies = new ArrayList<>();
            for (String filename : sourceFiles) {
                listMovies.add(MovieCreator.build(filename));
            }
            List<Track> listTracks = new LinkedList<>();
            for (Movie movie : listMovies) {
                for (Track track : movie.getTracks()) {
                    if (track.getHandler().equals(mediaKey)) {
                        listTracks.add(track);
                    }
                }
            }
            Movie outputMovie = new Movie();
            if (!listTracks.isEmpty()) {
                outputMovie.addTrack(new AppendTrack(listTracks.toArray(new Track[listTracks.size()])));
            }
            Container container = new DefaultMp4Builder().build(outputMovie);
            FileChannel fileChannel = new RandomAccessFile(String.format(targetFile), "rws").getChannel();
            container.writeContainer(fileChannel);
            fileChannel.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void makeNetworkErrorToast(Context context)
    {
        Toast.makeText(context, "네트워크 설정을 확인해 주세요.", Toast.LENGTH_SHORT).show();
    }

}
