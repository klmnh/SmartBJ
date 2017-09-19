package com.klmnh.smartbj.base.impl.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.klmnh.smartbj.base.BasePager;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);

        btn_title_menu.setVisibility(View.GONE);
    }

    @Override
    public void InitData() {

        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setTextSize(22);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);

        tv_title.setText("智慧北京");
        btn_title_menu.setVisibility(View.GONE);
    }
}
