package com.mobile.vople.vople.server;

import android.media.session.MediaSession;

import com.mobile.vople.vople.server.model.User;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

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

    public class Token{
        public String token;
    }

}
