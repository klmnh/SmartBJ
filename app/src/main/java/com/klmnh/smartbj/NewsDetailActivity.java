package com.klmnh.smartbj;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by Lenovo on 2017/9/19 019.
 */

public class NewsDetailActivity extends Activity {

    @ViewInject(R.id.btn_title_menu)
    private ImageButton btn_title_menu;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.btn_title_textSize)
    private ImageButton btn_title_textSize;
    @ViewInject(R.id.btn_title_share)
    private ImageButton btn_title_share;
    @ViewInject(R.id.ll_title)
    private LinearLayout ll_title;
    @ViewInject(R.id.wv_newsDetail)
    private WebView wv_newsDetail;
    @ViewInject(R.id.pb_waitLoading)
    private ProgressBar pb_waitLoading;
    private int tempWhich = 2;
    private int currentWhich = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_newsdetail);
        ViewUtils.inject(this);

        InitView();

        wv_newsDetail.loadUrl(getIntent().getStringExtra("url"));
    }

    private void InitView() {
        ll_title.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.INVISIBLE);
        pb_waitLoading.setVisibility(View.VISIBLE);
        btn_title_menu.setImageDrawable(getResources().getDrawable(R.drawable.back));

        WebSettings webSettings = wv_newsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        wv_newsDetail.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(request.toString());
                return true;
            }
        });

        wv_newsDetail.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb_waitLoading.setVisibility(View.GONE);
            }
        });

        btn_title_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_title_textSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetailActivity.this);
                builder.setTitle("字体设置");
                String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
                builder.setSingleChoiceItems(items, currentWhich, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempWhich = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebSettings webSettings = wv_newsDetail.getSettings();
                        currentWhich = tempWhich;
                        switch (currentWhich){
                            case 0:
                                webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                                break;
                            case 1:
                                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                                break;
                            case 2:
                                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                                break;
                            case 3:
                                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                                break;
                            case 4:
                                webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        btn_title_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onBackPressed() {
        if (wv_newsDetail.canGoBack()){
            wv_newsDetail.goBack();
        }else {
            finish();
        }
    }
}
