package com.mobile.vople.vople;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.mobile.vople.vople.server.RetrofitModel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by parkjaemin on 27/12/2018.
 */

public abstract class BaseEventActivity extends AppCompatActivity {

    protected Handler mergeHandler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mergeHandler != null) mergeHandler = null;
    }

    protected abstract void settingBoardDetail(RetrofitModel.BoardDetail response);
}
