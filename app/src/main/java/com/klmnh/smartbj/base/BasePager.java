package com.klmnh.smartbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.klmnh.smartbj.MainActivity;
import com.klmnh.smartbj.R;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public class BasePager {

    public final Activity mActivity;
    public View mViewRoot;
    public TextView tv_title;
    public ImageButton btn_title_menu;
    public FrameLayout fl_content;
    public ImageButton btn_photo;

    public BasePager(Activity activity){
        mActivity = activity;

        mViewRoot = InitView();
    }

    public View InitView(){

        View view = View.inflate(mActivity, R.layout.basepager, null);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_title_menu = (ImageButton) view.findViewById(R.id.btn_title_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        btn_photo = (ImageButton) view.findViewById(R.id.btn_photo);

        btn_title_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)mActivity;
                mainActivity.getSlidingMenu().toggle();
            }
        });

        return view;
    }

    public void InitData(){

    }

}
