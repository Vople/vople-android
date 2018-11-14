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
        Script script;
        //public  int board_likes;

    }

    public class CreateBoardContributor{
        public String title;
        int present_id;
        public String content;
    }

    public class CommentContributor
    {
        public String nickname;
        public String title;
    }

    public class Script {
        int id;
        int owner;
        int member_restriction;
        Cast cast;
    }

    public class Cast {
        int id;
        String roll_name;
        Plots_By_Cast plots_by_cast;
    }

    public class Plots_By_Cast {
        int id;
        String content;
        int order;
    }

}
