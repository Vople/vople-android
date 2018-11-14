package com.mobile.vople.vople.server;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by parkjaemin on 09/11/2018.
 */

public class RetrofitModel {


    public class BoardContributor
    {
        public int id;
        public String title;
        public int mode;
        //public  String script;
        //public  int board_likes;

    }

    public class CreateBoardContributor{
        public String title;
        String content;
        Date due;
        int present_id;
    }
}
