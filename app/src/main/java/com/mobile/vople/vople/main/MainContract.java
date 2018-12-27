package com.mobile.vople.vople.main;

import com.mobile.vople.vople.BasePresenter;
import com.mobile.vople.vople.BaseView;

/**
 * Created by parkjaemin on 22/12/2018.
 */

public class MainContract {

    interface View extends BaseView<Presenter>
    {
        void showEnterDialog(int position);

    }

    interface Presenter extends BasePresenter<View>
    {

    }
}
