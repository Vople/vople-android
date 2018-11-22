package com.mobile.vople.vople;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.google.gson.Gson;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.mobile.vople.vople.server.RetrofitInstance;
import com.mobile.vople.vople.server.RetrofitModel;
import com.mobile.vople.vople.server.VopleServiceApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ListView listView_role_play;
    private boolean bool_owner;
    private int i_big_heart;
    private Button btn_back;
    private Button btn_big_heart;
    private Button btn_record;
    private Button btn_cancel;
    private Button btn_send;
    private Button btn_gather;
    private Date date;

    private String allPlots = "";

    private List<RetrofitModel.Plot> plotList;

    SimpleDateFormat mFormat = new SimpleDateFormat("MM월 dd일 hh:mm");

    private Retrofit retrofit;


    private ListViewAdapter adapter;
    private RolePlayListViewAdapter adapter_role_play;

    private TextView tv_script, tv_room_title;

    public static int roomId;

    public static String roomTitle;

    private RetrofitModel.Cast cast;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initialize();

        Intent intent = getIntent();

        roomId = intent.getIntExtra("RoomID", -1);
        roomTitle = intent.getStringExtra("RoomTitle");

        tv_room_title.setText(roomTitle);

        if(roomId == -1)
        {
            Toast.makeText(EventActivity.this, "유효하지 않는 방입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        occurDataChange();

        i_big_heart = 0;
        bool_owner = false;
        btn_back = findViewById(R.id.btn_back);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        btn_gather = findViewById(R.id.btn_gather);

        VopleServiceApi.get_plots getPlotService = retrofit.create(VopleServiceApi.get_plots.class);
        Call<List<RetrofitModel.Plot>> getPlotRepos = getPlotService.repoContributors(roomId);

        getPlotRepos.enqueue(new Callback<List<RetrofitModel.Plot>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel.Plot>> call, Response<List<RetrofitModel.Plot>> response) {
                if(response.code() == 200)
                {
                    List<RetrofitModel.Plot> result = response.body();

                    for(RetrofitModel.Plot plot : result)
                    {
                        adapter_role_play.addItem(null, plot.content, plot.id);
                    }
                    adapter_role_play.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<RetrofitModel.Plot>> call, Throwable t) {

            }
        });

    }

    private String getTime(String now) {
        date = new Date(now);
        return mFormat.format(date);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btn_back.getId()) {
            finish();
        }

        else if (v.getId() == btn_big_heart.getId()) {
            i_big_heart++;
            if (i_big_heart % 2 == 0) {
                btn_big_heart.setBackgroundResource(R.drawable.event_unpress_heart);

            }
            else if (i_big_heart % 2 != 0) {
                btn_big_heart.setBackgroundResource(R.drawable.event_press_heart);

            }
        }
        else if(v.getId() == btn_gather.getId())
        {
            new Thread() {
                public void run() {
                    combineAllComments();

                    Bundle bun = new Bundle();

                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);

                }
            }.start();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(EventActivity.this, "Success to Merge your file", Toast.LENGTH_SHORT).show();
        }
    };


    private void initialize()
    {

        listView = (ListView) findViewById(R.id.lv_event);
        listView_role_play = (ListView) findViewById(R.id.lv_event_role);

        adapter = new ListViewAdapter();
        adapter_role_play = new RolePlayListViewAdapter();

        listView.setAdapter(adapter);
        listView_role_play.setAdapter(adapter_role_play);


        i_big_heart = 0;
        bool_owner = false;
        btn_back = findViewById(R.id.btn_back);
        btn_big_heart = findViewById(R.id.btn_big_heart);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        btn_gather = findViewById(R.id.btn_gather);
        tv_script = findViewById(R.id.tv_script);
        tv_room_title = findViewById(R.id.tv_room_title);

        btn_back.setOnClickListener(this);
        btn_big_heart.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_gather.setOnClickListener(this);

        retrofit = RetrofitInstance.getInstance(this);

        plotList = new ArrayList<RetrofitModel.Plot>();
    }

    public void occurDataChange()
    {
        adapter.clear();

        allPlots = "";

        VopleServiceApi.boardDetail service = retrofit.create(VopleServiceApi.boardDetail.class);

        final Call<RetrofitModel.BoardDetail> repos = service.repoContributors(roomId);

        repos.enqueue(new Callback<RetrofitModel.BoardDetail>() {
            @Override
            public void onResponse(Call<RetrofitModel.BoardDetail> call, Response<RetrofitModel.BoardDetail> response) {
                if(response.code() == 200)
                {

                    if(response.body().script == null) return;

                    boolean is_duplicated = false;

                    for(RetrofitModel.Cast cast : response.body().script.casts)
                    {
                        for(RetrofitModel.Plot plot : cast.plots_by_cast) {
                            for (RetrofitModel.Plot _plot : plotList)
                                if ( _plot.commenting.board.id == plot.commenting.board.id &&
                                        _plot.order == plot.order &&
                                        _plot.id == plot.id) is_duplicated = true;
                            if ( is_duplicated == false && plot.commenting != null && plot.commenting.board != null && plot.commenting.board.id == roomId)
                                plotList.add(plot);
                        }
                        is_duplicated = false;
                    }

                    Collections.sort(plotList, new Comparator<RetrofitModel.Plot>() {
                        @Override
                        public int compare(RetrofitModel.Plot o1, RetrofitModel.Plot o2) {
                            if(o1.order < o2.order) return -1;
                            else if(o1.order > o2.order) return 1;
                            return 0;
                        }
                    });

                    for(RetrofitModel.Plot plot : plotList)
                    {
                        if(plot.commenting.comment == null) continue;

                        RetrofitModel.CommentBrief comment = plot.commenting.comment;
                        String[] temp1 = comment.created_at.split("T");
                        String[] temp2 = temp1[0].split("-");
                        String[] temp3 = temp1[1].split(":");

                        String nowtime = temp2[1] + "월 " + temp2[2] + "일 " + temp3[0] + ":" + temp3[1];

                        adapter.addItem(null, comment.owner.name, nowtime, comment.sound);
                    }

                    adapter.notifyDataSetChanged();

                    String[] arrPlot = new String[plotList.size()];

                    for(RetrofitModel.Plot plot : plotList)
                        arrPlot[plot.order-1] = plot.content;

                    for(String s : arrPlot)
                    {
                        if(s != null)
                            allPlots += (s + "\n");
                    }

                    // 배경에 넣어주기
                    tv_script.setText(allPlots);

                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel.BoardDetail> call, Throwable t) {
                Toast.makeText(EventActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void combineAllComments()
    {
        List<String> soundPathList = new ArrayList<>();

        int count = 0;

        Collections.sort(plotList, new Comparator<RetrofitModel.Plot>() {
            @Override
            public int compare(RetrofitModel.Plot o1, RetrofitModel.Plot o2) {
                if(o1.order < o2.order) return -1;
                else if(o1.order > o2.order) return 1;
                return 0;
            }
        });

        RetrofitModel.CommentBrief[] arrComment = new RetrofitModel.CommentBrief[plotList.size()];

        for(RetrofitModel.Plot plot : plotList)
            arrComment[plot.order-1] = plot.commenting.comment;

        for(RetrofitModel.CommentBrief comment : arrComment)
        {
            if(comment == null || comment.sound == null) continue;

            try {
                URLConnection conn = new URL(comment.sound).openConnection();

                InputStream is = conn.getInputStream();

                OutputStream outstream = new FileOutputStream(new File(
                        Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + "sound" + count + ".mp3"));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();

                soundPathList.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "sound" + count++ + ".mp3");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        count = 0;

        String merge_filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/merge";

        mergeMediaFiles(true, soundPathList, merge_filename + ".mp3");

    }
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
}

