package com.klmnh.smartbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 2017/9/15 015.
 */

public class SPUtils {
    private static SharedPreferences sharedPreferences = null;

    /**
     * 存储Boolean类型的值
     * @param context 上下文环境
     * @param key 键值
     * @param value 存储的值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取Boolean类型的值
     * @param context 上下文环境
     * @param key 键值
     * @param defvalue 默认值
     * @return Boolean类型的值
     */
    public static boolean getBoolean(Context context, String key, boolean defvalue) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sharedPreferences.getBoolean(key, defvalue);
    }

    /**
     * 存储String类型的值
     * @param context 上下文环境
     * @param key 键值
     * @param value 存储的值
     */
    public static void putString(Context context, String key, String value) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 获取BString类型的值
     * @param context 上下文环境
     * @param key 键值
     * @param defvalue 默认值
     * @return Boolean类型的值
     */
    public static String getString(Context context, String key, String defvalue) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sharedPreferences.getString(key, defvalue);
    }
}
