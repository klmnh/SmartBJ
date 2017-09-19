package com.klmnh.smartbj.domain;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public class NewsMenu {

    public int retcode;

    public ArrayList<NewsMenuData> data;

    public ArrayList<Integer> extend;

    public class NewsMenuData {
        public int id;
        public String title;
        public int type;
        public ArrayList<NewsData> children;

    }

    public class NewsData {
        public int id;
        public String title;
        public int type;
        public String url;

    }
}
