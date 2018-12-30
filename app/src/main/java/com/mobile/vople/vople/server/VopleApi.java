package com.mobile.vople.vople.server;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Retrofit;

public class VopleApi {

    Retrofit mRetrofit;

    @Inject
    public VopleApi(Retrofit retrofit)
    {
        this.mRetrofit = retrofit;
    }

    public Retrofit getRetrofit()
    {
        return mRetrofit;
    }

    public interface getBoards
    {
        Single<getBoards> repoContributers();

    }
}
