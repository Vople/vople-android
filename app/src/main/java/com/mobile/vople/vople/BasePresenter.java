package com.mobile.vople.vople;

/**
 * Created by parkjaemin on 22/12/2018.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();
}
