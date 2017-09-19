package com.klmnh.smartbj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.content.Intent;

import com.klmnh.smartbj.utils.ConstantsValues;
import com.klmnh.smartbj.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/9/14 014.
 */

public class GuideActivity extends Activity{

    private int[] mGuideIds;
    private List<ImageView> mImageViews;
    private float mPointDis;
    private Button btn_startExperience;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        InitUI();
    }

    private void InitUI() {
        ViewPager vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        final LinearLayout ll_guide = (LinearLayout) findViewById(R.id.ll_guide);
        btn_startExperience = (Button) findViewById(R.id.btn_startExperience);
        final ImageView iv_redPoint = (ImageView) findViewById(R.id.iv_redPoint);

        ll_guide.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPointDis = ll_guide.getChildAt(1).getLeft() -
                        ll_guide.getChildAt(0).getLeft();
                ll_guide.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        vp_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = (int) (mPointDis * (positionOffset + position));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)iv_redPoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                iv_redPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {

                if (position == mGuideIds.length - 1){
                    btn_startExperience.setVisibility(View.VISIBLE);
                }else {
                    btn_startExperience.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_startExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);

                SPUtils.putBoolean(getApplicationContext(), ConstantsValues.IS_GUIDE_SHOW, true);

                finish();
            }
        });

        mGuideIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        mImageViews = new ArrayList<>();
        for (int id:
             mGuideIds) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(id);

            mImageViews.add(view);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 10;
            point.setLayoutParams(layoutParams);

            ll_guide.addView(point);
        }

        MyViewPager myViewPager = new MyViewPager();
        vp_guide.setAdapter(myViewPager);

    }

    class MyViewPager extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViews.get(position);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

    }
}
