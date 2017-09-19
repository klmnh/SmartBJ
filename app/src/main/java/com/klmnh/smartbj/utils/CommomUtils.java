package com.klmnh.smartbj.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lenovo on 2017/9/16 016.
 */

public class CommomUtils {

    public static void showToastInfo(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
