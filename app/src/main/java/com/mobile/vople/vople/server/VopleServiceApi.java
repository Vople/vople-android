package com.mobile.vople.vople.server;

import android.media.session.MediaSession;

import com.mobile.vople.vople.server.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by parkjaemin on 07/11/2018.
 */

public class VopleServiceApi {

    public interface login
    {
        @FormUrlEncoded
        @POST("rest-auth/login/")
        Call<VopleServiceApi.Token> repoContributors(
                @Field("username") String id,
                @Field("password") String password
        );
    }

    public interface signup
    {
        @FormUrlEncoded
        @POST("rest-auth/registration/vople-registration/")
        Call<VopleServiceApi.Token> repoContributors(
                @Field("username") String id,
                @Field("password1") String password1,
                @Field("password2") String password2,
                @Field("email") String email,
                @Field("name") String nickname,
                @Field("bio") String bio,
                @Field("gender") String gender
        );
    }

    public interface listAllScripts
    {
        @GET("sounds/scripts/")
        Call<List<RetrofitModel.Script>> repoContributors();
    }

    public interface boards
    {
        @GET("sounds/board/")
        Call<List<RetrofitModel.BoardContributor>> repoContributors();
    }

    public interface create_board {
        @FormUrlEncoded
        @POST("sounds/board/")
        Call<RetrofitModel.BoardContributor> repoContributors(
                @Field("title") String title,
                @Field("content") String content,
                @Field("mode") int mode,
                @Field("script_id") int script_id
        );
    }

    public interface get_plots{
        @GET("sounds/{board_id}/plots/")
        Call<List<RetrofitModel.Plot>> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface commentOnBoard {
        @Multipart
        @POST("sounds/{board_id}/comment/")
        Call<ResponseBody> upload(
                @Path("board_id") int board_id,
                @Part("sound\"; filename=\"recorder.mp3\"") RequestBody sound,
                @Part("plot_id") int plot_id
        );
    }

    public interface upload{
        @Multipart
        @POST("sounds/comment/")
        Call<ResponseBody> uploadTrack(
                @Part("track") RequestBody file,
                @Part("plot_id") int plot_id
        );
    }

    public interface comments
    {
        @GET("sounds/{board_id}/comments/")
        Call<List<RetrofitModel.CommentContributor>> repoContributors();
    }

    public interface quilifyJoinRoom
    {
        @GET("sounds/{board_id}/join/")
        Call<RetrofitModel.Roll_Brief> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface joinAlreadyRegistedRoom
    {
        @PUT("sounds/{board_id}/join/")
        Call<RetrofitModel.Cast> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface joinRoom
    {
        @FormUrlEncoded
        @POST("sounds/{board_id}/join/")
        Call<RetrofitModel.Cast> repoContributors(
                @Path("board_id") int board_id,
                @Field("roll_name") String roll_name
        );
    }

    public interface boardDetail
    {
        @GET("sounds/{board_id}/board/")
        Call<RetrofitModel.BoardDetail> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface fcm_register{
        @FormUrlEncoded
        @POST("fcm/devices/")
        Call<ResponseBody> repoContributors(
                @Field("dev_id") String device_id,
                @Field("reg_id") String token,
                @Field("is_active") boolean is_active
        );
    }


    public interface getAllScripts{
        @GET("sounds/scripts/")
        Call<List<RetrofitModel.ScriptBrief>> repoContributors();
    }

    public interface getScriptDetail{
        @GET("sounds/{script_id}/script/")
        Call<RetrofitModel.ScriptDetail> repoContributors(
                @Path("script_id") int script_id
        );
    }

    public class Token{
        public String token;
    }

    public class Empty{

    }

}
