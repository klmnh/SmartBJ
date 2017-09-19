package com.klmnh.smartbj.base.impl.menuDetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.klmnh.smartbj.MainActivity;
import com.klmnh.smartbj.R;
import com.klmnh.smartbj.base.BaseMenuDetailPager;
import com.klmnh.smartbj.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/9/17 017.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ArrayList<NewsMenu.NewsData> children;

    @ViewInject(R.id.tpi_newsMenuDetail)
    private TabPageIndicator tpi_newsMenuDetail;
    @ViewInject(R.id.ib_tabNext)
    private ImageButton ib_tabNext;
    @ViewInject(R.id.vp_newsMenuDetail)
    private ViewPager vp_newsMenuDetail;
    private ArrayList<NewsTabDetailPager> mNewsTabDetailPager;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsData> children) {
        super(activity);
        this.children = children;
    }

    @Override
    public View InitView() {
        View view = View.inflate(mActivity, R.layout.pager_menudetail_news, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void InitData() {

        mNewsTabDetailPager = new ArrayList<NewsTabDetailPager>();

        for (NewsMenu.NewsData newsData :
             children) {
            mNewsTabDetailPager.add(new NewsTabDetailPager(mActivity, newsData));
        }

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
        vp_newsMenuDetail.setAdapter(myPagerAdapter);

        tpi_newsMenuDetail.setViewPager(vp_newsMenuDetail);
        ib_tabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int currentIndex = vp_newsMenuDetail.getCurrentItem();
                vp_newsMenuDetail.setCurrentItem(++currentIndex, false);
            }
        });

        tpi_newsMenuDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity activity = (MainActivity)mActivity;
                if (position == 0){
                    activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mNewsTabDetailPager.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).title;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsTabDetailPager newsTabDetailPager = mNewsTabDetailPager.get(position);
            newsTabDetailPager.InitData();

            container.addView(newsTabDetailPager.mViewRoot);

            return newsTabDetailPager.mViewRoot;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
