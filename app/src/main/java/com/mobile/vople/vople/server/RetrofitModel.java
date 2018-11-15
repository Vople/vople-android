package com.mobile.vople.vople.server;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by parkjaemin on 09/11/2018.
 */

public class RetrofitModel {


    public class BoardContributor
    {
        public int id;
        public String title;
        public int mode;
        public ScriptBrief script;
        //public  int board_likes;

    }

    public class CreateBoardContributor{
        public String title;
        public String content;
    }

    public class CommentContributor
    {
        public String nickname;
        public String title;
    }

    public class Script {
        public int id;
        public int member_restriction;
        public String title;
    }

    public class ScriptBrief{
        public String title;
    }


    public class Plot {
        public int id;
        public String content;
        public int order;
    }

    public class Cast {
        public int id;
        public String roll_name;
        public List<Plot> plots_by_cast;
        public int member;  // user_id
    }

    public class UserBrief{
        public String name;
    }

    public class CommentBrief{
        public UserBrief owner;
        public String sound;
        public String created_at;
    }

    public class BoardDetail{
        public String title;
        public List<CommentBrief> comments;
        public int script;
    }


    public class Roll_Brief {
        public List<String> rolls;
    }

}
