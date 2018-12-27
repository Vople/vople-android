package com.mobile.vople.vople.server;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
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
        @POST("/rest-auth/login/")
        Single<Token> repoContributors(
                @Field("username") String id,
                @Field("password") String password
        );
    }

    public interface signup
    {
        @FormUrlEncoded
        @POST("rest-auth/registration/vople-registration/")
        Single<Token> repoContributors(
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
        Single<List<RetrofitModel.Script>> repoContributors();
    }

    public interface boards
    {
        @GET("sounds/board/")
        Single<List<RetrofitModel.BoardContributor>> repoContributors();
    }

    public interface create_board {
        @FormUrlEncoded
        @POST("sounds/board/")
        Single<Response<RetrofitModel.BoardContributor>> repoContributors(
                @Field("title") String title,
                @Field("content") String content,
                @Field("mode") int mode,
                @Field("script_id") int script_id
        );
    }

    public interface get_plots{
        @GET("sounds/{board_id}/plots/")
        Single<List<RetrofitModel.Plot>> repoContributors(
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

    public interface quilifyJoinRoom
    {
        @GET("sounds/{board_id}/join/")
        Single<Response<RetrofitModel.Roll_Brief>> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface joinAlreadyRegistedRoom
    {
        @PUT("sounds/{board_id}/join/")
        Single<Response<RetrofitModel.Cast>> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface joinRoom
    {
        @FormUrlEncoded
        @POST("sounds/{board_id}/join/")
        Single<Response<RetrofitModel.Casting>> repoContributors(
                @Path("board_id") int board_id,
                @Field("roll_name") String roll_name
        );
    }

    public interface joinFreeRoom
    {
        @FormUrlEncoded
        @POST("sounds/{board_id}/join/")
        Single<ResponseBody> repoContributors(
                @Path("board_id") int board_id,
                @Field("roll_name") String roll_name
        );
    }

    public interface boardDetail
    {
        @GET("sounds/{board_id}/board/")
        Single<Response<RetrofitModel.BoardDetail>> repoContributors(
                @Path("board_id") int board_id
        );
    }

    public interface getAllScripts{
        @GET("sounds/scripts/")
        Single<List<RetrofitModel.Script>> repoContributors();
    }

    public interface getScriptDetail{
        @GET("sounds/{script_id}/script/")
        Single<RetrofitModel.ScriptDetail> repoContributors(
                @Path("script_id") int script_id
        );
    }

    public interface getEventBoards{
        @GET("sounds/event/")
        Single<List<RetrofitModel.BoardContributor>> repoContributors();
    }

    public class Token{
        public String token;
    }

}
