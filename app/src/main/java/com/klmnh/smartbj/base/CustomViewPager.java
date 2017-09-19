package com.klmnh.smartbj.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lenovo on 2017/9/18 018.
 */

public class CustomViewPager extends ViewPager {

    private int startX;
    private int startY;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (ev.getX() - startX);
                int dy = (int) (ev.getY() - startY);
                if (dx > 0) {
                    if (getCurrentItem() == 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    if (getCurrentItem() == getAdapter().getCount() - 1) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }
}
