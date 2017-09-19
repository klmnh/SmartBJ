package com.klmnh.smartbj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.klmnh.smartbj.fragments.ContentFragment;
import com.klmnh.smartbj.fragments.LeftMenuFragment;

/**
 * Created by Lenovo on 2017/9/15 015.
 */

public class MainActivity extends SlidingFragmentActivity {

    public static final String FRAGMENT_CONTENT = "fragment_content";
    public static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private FragmentManager fragmentMgr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.menu_mainleft);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width * 3 / 5);

        InitFragment();

    }

    private void InitFragment() {
        fragmentMgr = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentMgr.beginTransaction();
        fragmentTransaction.replace(R.id.ll_main, new ContentFragment(), FRAGMENT_CONTENT);
        fragmentTransaction.replace(R.id.fl_leftmenu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);
        fragmentTransaction.commit();
    }

    public LeftMenuFragment getLeftMenuFragment(){
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentMgr.findFragmentByTag(FRAGMENT_LEFT_MENU);

        return leftMenuFragment;
    }

    public ContentFragment getContentFragment(){
        ContentFragment contentFragment = (ContentFragment) fragmentMgr.findFragmentByTag(FRAGMENT_CONTENT);

        return contentFragment;
    }
}
