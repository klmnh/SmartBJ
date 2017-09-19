package com.klmnh.smartbj.base.impl.menuDetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klmnh.smartbj.R;
import com.klmnh.smartbj.base.BaseMenuDetailPager;
import com.klmnh.smartbj.domain.PhotosBean;
import com.klmnh.smartbj.utils.CommomUtils;
import com.klmnh.smartbj.utils.ConstantsValues;
import com.klmnh.smartbj.utils.SPUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Lenovo on 2017/9/17 017.
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.lv_photo)
    private ListView lv_photo;
    @ViewInject(R.id.gv_photo)
    private GridView gv_photo;
    private PhotosBean photosBean;
    private MyListViewAdapter myListViewAdapter;
    private BitmapUtils bitmapUtils;
    private ImageButton btn_photo;
    private boolean isListView = true;

    public PhotoMenuDetailPager(Activity activity, ImageButton imgButton) {
        super(activity);
        this.btn_photo = imgButton;
        this.btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isListView = !isListView;
                if (isListView){
                    lv_photo.setVisibility(View.VISIBLE);
                    gv_photo.setVisibility(View.GONE);
                    btn_photo.setImageResource(R.drawable.icon_pic_grid_type);
                }else {
                    lv_photo.setVisibility(View.GONE);
                    gv_photo.setVisibility(View.VISIBLE);
                    btn_photo.setImageResource(R.drawable.icon_pic_list_type);
                }
            }
        });
    }

    @Override
    public View InitView() {
        View view = View.inflate(mActivity, R.layout.pager_menudetail_photo, null);

        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void InitData() {
        bitmapUtils = new BitmapUtils(mActivity);

        String strJson = SPUtils.getString(mActivity, ConstantsValues.URL_PHOTO, "");
        if(TextUtils.isEmpty(strJson)) {
            getDataFromServer();
        }else {
            ProcessData(strJson);
        }
    }

    public void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantsValues.URL_PHOTO, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String strJson = responseInfo.result;

                ProcessData(strJson);
                SPUtils.putString(mActivity, ConstantsValues.URL_PHOTO, strJson);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                CommomUtils.showToastInfo(mActivity, s);
            }
        });
    }

    private void ProcessData(String strJson) {
        Gson gSon = new Gson();
        photosBean = gSon.fromJson(strJson, PhotosBean.class);

        if(myListViewAdapter == null) {
            myListViewAdapter = new MyListViewAdapter();
            lv_photo.setAdapter(myListViewAdapter);

            gv_photo.setAdapter(myListViewAdapter);
        }
    }

    class MyListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return photosBean.data.news.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return photosBean.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.adapter_pager_photo, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
                viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo_image);
                viewHolder.tv_photo = (TextView) convertView.findViewById(R.id.tv_photo_title);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            bitmapUtils.display(viewHolder.iv_photo, photosBean.data.news.get(position).listimage);
            viewHolder.tv_photo.setText(photosBean.data.news.get(position).title);

            return convertView;
        }
    }

    class ViewHolder{
        public ImageView iv_photo;
        public TextView tv_photo;
    }
}
