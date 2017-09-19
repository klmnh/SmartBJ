package com.klmnh.smartbj.fragments;

import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.klmnh.smartbj.MainActivity;
import com.klmnh.smartbj.NoScrollViewPager;
import com.klmnh.smartbj.R;
import com.klmnh.smartbj.base.impl.pager.GovPager;
import com.klmnh.smartbj.base.impl.pager.HomePager;
import com.klmnh.smartbj.base.impl.pager.NewsPager;
import com.klmnh.smartbj.base.impl.pager.SettingPager;
import com.klmnh.smartbj.base.impl.pager.SmartServicePager;
import com.klmnh.smartbj.base.BasePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/9/15 015.
 */

public class ContentFragment extends BaseFragment {

    private List<BasePager> mViewList;
    @ViewInject(R.id.vp_content)
    private NoScrollViewPager mViewPager;
    private MyPagerAdapter myPagerAdapter;
    @ViewInject(R.id.rg_group)
    private RadioGroup rg_group;

    @Override
    public View InitView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void InitData() {
        mViewList = new ArrayList<BasePager>();
        mViewList.add(new HomePager(mActivity));
        mViewList.add(new NewsPager(mActivity));
        mViewList.add(new SmartServicePager(mActivity));
        mViewList.add(new GovPager(mActivity));
        mViewList.add(new SettingPager(mActivity));

        myPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(myPagerAdapter);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager BasePager = mViewList.get(position);
                BasePager.InitData();
                MainActivity activity = (MainActivity) mActivity;
                SlidingMenu slidingMenu = activity.getSlidingMenu();
                if (position == 0 || position == mViewList.size() - 1) {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                } else {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    public NewsPager getNewsPager(){
        return (NewsPager) mViewList.get(1);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager BasePager = mViewList.get(position);
            container.addView(BasePager.mViewRoot);

            return BasePager.mViewRoot;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
