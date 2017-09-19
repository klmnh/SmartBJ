package com.klmnh.smartbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public abstract class BaseMenuDetailPager {

    public final Activity mActivity;
    public View mViewRoot;

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;

        mViewRoot = InitView();
    }

    public abstract View InitView();

    public void InitData(){

    }

}
