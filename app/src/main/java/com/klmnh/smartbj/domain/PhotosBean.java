package com.klmnh.smartbj.domain;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/19 019.
 */

public class PhotosBean {

    public PhotoData data;

    public class PhotoData {
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews {
        public String title;
        public String listimage;
    }
}
