package com.klmnh.smartbj.base.impl.menuDetail;

import android.app.Activity;
import android.view.View;

import com.klmnh.smartbj.R;
import com.klmnh.smartbj.base.BaseMenuDetailPager;

/**
 * Created by Lenovo on 2017/9/17 017.
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager {

    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View InitView() {
        View view = View.inflate(mActivity, R.layout.pager_menudetail_topic, null);

        return view;
    }

    @Override
    public void InitData() {

    }
}
