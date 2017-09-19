package com.klmnh.smartbj.base.impl.menuDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klmnh.smartbj.NewsDetailActivity;
import com.klmnh.smartbj.R;
import com.klmnh.smartbj.base.BaseMenuDetailPager;
import com.klmnh.smartbj.base.CustomViewPager;
import com.klmnh.smartbj.domain.NewsMenu;
import com.klmnh.smartbj.domain.NewsTab;
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
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 2017/9/17 017.
 */

public class NewsTabDetailPager extends BaseMenuDetailPager {

    public NewsMenu.NewsData item;

    @ViewInject(R.id.vp_new_tab)
    public CustomViewPager vp_new_tab;
    @ViewInject(R.id.tv_new_tab_topnews_title)
    public TextView tv_new_tab_topnews_title;
    @ViewInject(R.id.cpi_tab_topnews_indicator)
    public CirclePageIndicator cpi_tab_topnews_indicator;
    @ViewInject(R.id.lv_tab_news)
    public ListView lv_tab_news;
    @ViewInject(R.id.tv_tab_news_refresh_title)
    public TextView tv_tab_news_refresh_title;
    @ViewInject(R.id.tv_tab_news_refresh_time)
    public TextView tv_tab_news_refresh_time;
    @ViewInject(R.id.iv_tab_news_refresh_icon)
    public ImageView iv_tab_news_refresh_icon;
    @ViewInject(R.id.pb_tab_news_refresh_progressBar)
    public ProgressBar pb_tab_news_refresh_progressBar;

    private NewsTab newsTab;
    private BitmapUtils mBitmapUtils;
    private int refreshMeasuredHeight;
    private int startY = -1;
    private int endY;
    private int REFRESH_STATE_PULL = 1;
    private int REFRESH_STATE_RELEASE = 2;
    private int REFRESH_STATE_REFESHING = 3;
    private int currentRefreshState = 0;
    private RotateAnimation rotateAnimationDown;
    private RotateAnimation rotateAnimationUp;
    private View viewRefreshHeader;
    private View viewRefreshFooter;
    private int refreshMeasuredFooterHeight;

    private boolean isLoadMore = false;
    private MyListViewAdapter myListViewAdapter;
    private boolean isStopThread = false;
    private boolean isStartMove = true;

    public NewsTabDetailPager(Activity activity, NewsMenu.NewsData item) {
        super(activity);
        this.item = item;
    }

    @Override
    public View InitView() {
        final View view = View.inflate(mActivity, R.layout.pager_menudetail_tab, null);

        viewRefreshHeader = View.inflate(mActivity, R.layout.refresh_pull_tab_news, null);

        View viewHeader = View.inflate(mActivity, R.layout.listview_header_tab_news, null);

        viewRefreshFooter = View.inflate(mActivity, R.layout.listview_footer_tab_news, null);

        ViewUtils.inject(this, viewRefreshHeader);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, viewHeader);

        InitAnimation();

        lv_tab_news.addHeaderView(viewRefreshHeader);//先添加的在上边
        viewRefreshHeader.measure(0, 0);
        refreshMeasuredHeight = viewRefreshHeader.getMeasuredHeight();
        viewRefreshHeader.setPadding(0, -refreshMeasuredHeight, 0, 0);
        iv_tab_news_refresh_icon.setVisibility(View.VISIBLE);
        pb_tab_news_refresh_progressBar.setVisibility(View.INVISIBLE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_tab_news_refresh_time.setText(simpleDateFormat.format(new Date()));

        lv_tab_news.addHeaderView(viewHeader);

        lv_tab_news.addFooterView(viewRefreshFooter);
        viewRefreshFooter.measure(0, 0);
        refreshMeasuredFooterHeight = viewRefreshFooter.getMeasuredHeight();
        viewRefreshFooter.setPadding(0, -refreshMeasuredFooterHeight, 0, 0);

        lv_tab_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lv_tab_news.getHeaderViewsCount();
                NewsTab.News news = newsTab.data.news.get(position);

                String readIds = SPUtils.getString(mActivity, ConstantsValues.READ_IDS, "");
                if(!readIds.contains(news.id)) {
                    readIds += news.id + ",";
                    SPUtils.putString(mActivity, ConstantsValues.READ_IDS, readIds);

                    TextView textView = (TextView) view.findViewById(R.id.tv_tab_news_title);
                    textView.setTextColor(Color.GRAY);
                }
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);
            }
        });

        lv_tab_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (lv_tab_news.getLastVisiblePosition() == lv_tab_news.getAdapter().getCount() - 1) {
                        if (!isLoadMore && !TextUtils.isEmpty(newsTab.data.more)) {
                            isLoadMore = true;
                            viewRefreshFooter.setPadding(0, 0, 0, 0);
                            lv_tab_news.setSelection(lv_tab_news.getLastVisiblePosition());

                            getMoreDataFromServer();
                        } else {
                            CommomUtils.showToastInfo(mActivity, "没有更多数据啦");
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lv_tab_news.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                        isStartMove = true;
                        break;
                    case MotionEvent.ACTION_DOWN:
                        isStartMove = false;
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (currentRefreshState == REFRESH_STATE_REFESHING) {
                            break;
                        }

                        if (startY == -1) {
                            startY = (int) event.getY();
                        }
                        endY = (int) event.getY();

                        int dy = endY - startY;
                        int firstVisiblePosition = lv_tab_news.getFirstVisiblePosition();
                        if (dy > 0 && firstVisiblePosition == 0) {
                            int paddingTop = -refreshMeasuredHeight + dy;
                            if (paddingTop > 0 && currentRefreshState != REFRESH_STATE_RELEASE) {
                                currentRefreshState = REFRESH_STATE_RELEASE;
                                RefreshState();
                            } else if (paddingTop <= 0 && currentRefreshState != REFRESH_STATE_PULL) {
                                currentRefreshState = REFRESH_STATE_PULL;
                                RefreshState();
                            }

                            viewRefreshHeader.setPadding(0, paddingTop, 0, 0);

                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isStartMove = true;
                        startY = 0;
                        if (currentRefreshState == REFRESH_STATE_RELEASE) {
                            currentRefreshState = REFRESH_STATE_REFESHING;
                            viewRefreshHeader.setPadding(0, 0, 0, 0);
                            RefreshState();
                            getDataFromServer();
                        } else if (currentRefreshState == REFRESH_STATE_PULL) {
                            viewRefreshHeader.setPadding(0, -refreshMeasuredHeight, 0, 0);
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void RefreshState() {
        if (currentRefreshState == REFRESH_STATE_PULL) {
            tv_tab_news_refresh_title.setText("下拉刷新");
            pb_tab_news_refresh_progressBar.setVisibility(View.INVISIBLE);
            iv_tab_news_refresh_icon.setVisibility(View.VISIBLE);
            iv_tab_news_refresh_icon.startAnimation(rotateAnimationDown);
        } else if (currentRefreshState == REFRESH_STATE_RELEASE) {
            tv_tab_news_refresh_title.setText("松开刷新");
            pb_tab_news_refresh_progressBar.setVisibility(View.INVISIBLE);
            iv_tab_news_refresh_icon.setVisibility(View.VISIBLE);
            iv_tab_news_refresh_icon.startAnimation(rotateAnimationUp);
        } else if (currentRefreshState == REFRESH_STATE_REFESHING) {
            iv_tab_news_refresh_icon.clearAnimation();
            tv_tab_news_refresh_title.setText("正在刷新");
            pb_tab_news_refresh_progressBar.setVisibility(View.VISIBLE);
            iv_tab_news_refresh_icon.setVisibility(View.INVISIBLE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tv_tab_news_refresh_time.setText(simpleDateFormat.format(new Date()));
        }
    }

    private void InitAnimation() {
        rotateAnimationUp = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationUp.setDuration(300);
        rotateAnimationUp.setFillAfter(true);

        rotateAnimationDown = new RotateAnimation(-18, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationDown.setDuration(300);
        rotateAnimationDown.setFillAfter(true);
    }

    @Override
    public void InitData() {

        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantsValues.HTTP_10_0_2_2_8080_ZHBJ + item.url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String strJson = responseInfo.result;

                ProcessData(strJson, false);
                viewRefreshHeader.setPadding(0, -refreshMeasuredHeight, 0, 0);
                currentRefreshState = 0;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                CommomUtils.showToastInfo(mActivity, s);
                viewRefreshHeader.setPadding(0, -refreshMeasuredHeight, 0, 0);
                currentRefreshState = 0;
            }
        });
    }

    public void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantsValues.HTTP_10_0_2_2_8080_ZHBJ + newsTab.data.more, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String strJson = responseInfo.result;

                ProcessData(strJson, true);
                viewRefreshFooter.setPadding(0, -refreshMeasuredFooterHeight, 0, 0);
                isLoadMore = false;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                CommomUtils.showToastInfo(mActivity, s);
                viewRefreshHeader.setPadding(0, -refreshMeasuredFooterHeight, 0, 0);
                isLoadMore = false;
            }
        });
    }

    private void ProcessData(String strJson, boolean isLoadMore) {
        Gson gSon = new Gson();

        if (!isLoadMore) {
            newsTab = gSon.fromJson(strJson, NewsTab.class);
            mBitmapUtils = new BitmapUtils(mActivity);

            MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
            vp_new_tab.setAdapter(myPagerAdapter);

            cpi_tab_topnews_indicator.setViewPager(vp_new_tab);
            cpi_tab_topnews_indicator.setSnap(true);
            cpi_tab_topnews_indicator.onPageSelected(0);

            tv_new_tab_topnews_title.setText(newsTab.data.topnews.get(0).title);
            cpi_tab_topnews_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tv_new_tab_topnews_title.setText(newsTab.data.topnews.get(position).title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            myListViewAdapter = new MyListViewAdapter();
            lv_tab_news.setAdapter(myListViewAdapter);

            new Thread(){
                @Override
                public void run() {
                    try {
                        while (!isStopThread) {
                            sleep(2000);
                            if(isStartMove) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        vp_new_tab.setCurrentItem((vp_new_tab.getCurrentItem() + 1) % vp_new_tab.getAdapter().getCount());
                                    }
                                });
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            NewsTab newsTabMore = gSon.fromJson(strJson, NewsTab.class);
            newsTab.data.news.addAll(newsTabMore.data.news);
            newsTab.data.more = newsTabMore.data.more;
            myListViewAdapter.notifyDataSetChanged();
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        public MyPagerAdapter() {
            super();
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return newsTab.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = newsTab.data.topnews.get(position).topimage;
            mBitmapUtils.display(imageView, url);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyListViewAdapter extends BaseAdapter {

        public MyListViewAdapter() {
            super();
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return newsTab.data.news.size();
        }

        @Override
        public Object getItem(int position) {
            return newsTab.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.adapter_tab_news, null);
                viewHodler = new ViewHodler();
                viewHodler.iv_icon = (ImageView) convertView.findViewById(R.id.iv_tab_news_icon);
                viewHodler.tv_title = (TextView) convertView.findViewById(R.id.tv_tab_news_title);
                viewHodler.tv_time = (TextView) convertView.findViewById(R.id.tv_tab_news_time);

                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            mBitmapUtils.display(viewHodler.iv_icon, newsTab.data.news.get(position).listimage);
            viewHodler.tv_title.setText(newsTab.data.news.get(position).title);
            viewHodler.tv_time.setText(newsTab.data.news.get(position).pubdate);
            String readIds = SPUtils.getString(mActivity, ConstantsValues.READ_IDS, "");
            if (readIds.contains(newsTab.data.news.get(position).id)){
                viewHodler.tv_title.setTextColor(Color.GRAY);
            }

            return convertView;
        }
    }

    class ViewHodler {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    public interface OnRefreshListener {
        public void OnRefresh();
    }
}
