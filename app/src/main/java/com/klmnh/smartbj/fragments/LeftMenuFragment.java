package com.klmnh.smartbj.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.klmnh.smartbj.MainActivity;
import com.klmnh.smartbj.R;
import com.klmnh.smartbj.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/15 015.
 */

public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_leftMenu)
    public ListView lv_leftMenu;

    private ArrayList<NewsMenu.NewsMenuData> data;
    private int mCurrentPosition = 0;
    private MyAdapter myAdapter;

    @Override
    public View InitView() {

        View view = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
        ViewUtils.inject(this, view);

        return view;
    }

    public void SetMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        this.data = data;
        if (myAdapter == null) {
            myAdapter = new MyAdapter();
            lv_leftMenu.setAdapter(myAdapter);
        } else {
            myAdapter.notifyDataSetChanged();
        }
        mCurrentPosition = 0;
        lv_leftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position;
                myAdapter.notifyDataSetChanged();

                MainActivity mainActivity = (MainActivity) mActivity;
                mainActivity.getSlidingMenu().toggle();

                mainActivity.getContentFragment().getNewsPager().setMenuDetailPager(position);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = View.inflate(mActivity, R.layout.adapter_left_menu, null);
            } else {
                view = convertView;
            }

            TextView tv_left_menu_title = (TextView) view.findViewById(R.id.tv_left_menu_title);
            if (mCurrentPosition == position) {
                tv_left_menu_title.setEnabled(true);
            } else {
                tv_left_menu_title.setEnabled(false);
            }
            tv_left_menu_title.setText(data.get(position).title);

            return view;
        }
    }
}
