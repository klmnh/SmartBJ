package com.klmnh.smartbj.domain;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/17 017.
 */

public class NewsTab {

    public NewsTabData data;

    public class NewsTabData {
        public String more;
        public ArrayList<TopNews> topnews;
        public ArrayList<News> news;
    }

    public class TopNews {
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String url;
    }

    public class News {
        public String id;
        public String pubdate;
        public String title;
        public String listimage;
        public String url;
    }
}
