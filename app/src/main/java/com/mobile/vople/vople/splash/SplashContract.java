package com.mobile.vople.vople.splash;

import com.mobile.vople.vople.BasePresenter;
import com.mobile.vople.vople.BaseView;

public interface SplashContract {

    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter<View> {}
}
