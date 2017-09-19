package com.klmnh.smartbj.base.impl.pager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klmnh.smartbj.MainActivity;
import com.klmnh.smartbj.base.BaseMenuDetailPager;
import com.klmnh.smartbj.base.BasePager;
import com.klmnh.smartbj.base.impl.menuDetail.InteractMenuDetailPager;
import com.klmnh.smartbj.base.impl.menuDetail.NewsMenuDetailPager;
import com.klmnh.smartbj.base.impl.menuDetail.PhotoMenuDetailPager;
import com.klmnh.smartbj.base.impl.menuDetail.TopicMenuDetailPager;
import com.klmnh.smartbj.domain.NewsMenu;
import com.klmnh.smartbj.fragments.LeftMenuFragment;
import com.klmnh.smartbj.utils.CommomUtils;
import com.klmnh.smartbj.utils.ConstantsValues;
import com.klmnh.smartbj.utils.SPUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public class NewsPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mMenuDetailPager;
    private NewsMenu newsLeftMenu;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void InitData() {

        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextSize(22);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);

        tv_title.setText("新闻中心");

        String strJson = SPUtils.getString(mActivity, ConstantsValues.URL_CATEGORY, "");
        if(!TextUtils.isEmpty(strJson)){
            ProcessData(strJson);
        }
        getDataFromServer();
    }

    public void getDataFromServer(){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantsValues.URL_CATEGORY, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String strJson = responseInfo.result;
                SPUtils.putString(mActivity, ConstantsValues.URL_CATEGORY, strJson);

                ProcessData(strJson);

            }

            @Override
            public void onFailure(HttpException e, String msg) {
                CommomUtils.showToastInfo(mActivity, msg);
            }
        });
    }

    private void ProcessData(String strJson) {
        Gson gson = new Gson();
        newsLeftMenu = gson.fromJson(strJson, NewsMenu.class);

        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.SetMenuData(newsLeftMenu.data);

        mMenuDetailPager = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPager.add(new NewsMenuDetailPager(mActivity, newsLeftMenu.data.get(0).children));
        mMenuDetailPager.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPager.add(new PhotoMenuDetailPager(mActivity, btn_photo));
        mMenuDetailPager.add(new InteractMenuDetailPager(mActivity));

        setMenuDetailPager(0);
    }


    public void setMenuDetailPager(int position) {
        BaseMenuDetailPager baseMenuDetailPager = mMenuDetailPager.get(position);
        if (baseMenuDetailPager instanceof PhotoMenuDetailPager){
            btn_photo.setVisibility(View.VISIBLE);
        }else {
            btn_photo.setVisibility(View.GONE);
        }
        fl_content.removeAllViews();
        fl_content.addView(baseMenuDetailPager.mViewRoot);
        baseMenuDetailPager.InitData();

        tv_title.setText(newsLeftMenu.data.get(position).title);
    }
}
