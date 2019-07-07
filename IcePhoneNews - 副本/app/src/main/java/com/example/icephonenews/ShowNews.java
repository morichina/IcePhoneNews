package com.example.icephonenews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowNews extends AppCompatActivity {
    //private List<News> newsList[] = new ArrayList[3];
    private List<News> techNewsList = new ArrayList<News>();
    private List<News> financeNewsList = new ArrayList<News>();
    private List<News> sportsNewsList = new ArrayList<News>();
    private RecyclerView[] recyclerViews = new RecyclerView[3];
    private LinearLayoutManager[] managers = new LinearLayoutManager[3];
    private NewsAdapter[] adapters = new NewsAdapter[3];
    private View[] views = new View[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_news);
        TextView createTime = (TextView)findViewById(R.id.createTime);
        Intent intent = getIntent();
        createTime.setText(intent.getStringExtra("time"));
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        // 添加标签
        tabLayout.addTab(tabLayout.newTab().setText("科技"));
        tabLayout.addTab(tabLayout.newTab().setText("财经"));
        tabLayout.addTab(tabLayout.newTab().setText("体育"));
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider));
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        views[0] = View.inflate(this, R.layout.technology_layout, null);
        views[1] = View.inflate(this, R.layout.finance_layout, null);
        views[2] = View.inflate(this, R.layout.sports_layout, null);
        viewPager.setAdapter(new Adapter());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        recyclerViews[0] = (RecyclerView)views[0].findViewById(R.id.technologyRecyclerView);
        recyclerViews[1] = (RecyclerView)views[1].findViewById(R.id.financeRecyclerView);
        recyclerViews[2] = (RecyclerView)views[2].findViewById(R.id.sportsRecyclerView);
        parseJSONData();

        // 绑定tab点击事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){

            }
        });
    }

    private void parseJSONData(){
        String serverPath = "https://www.apiopen.top/journalismApi";
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(serverPath).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String title, digest, resource, releaseTime, newsUrl;
                    Bitmap imageBitmap;
                    JSONArray category = null, pic = null;
                    JSONObject value, image;
                    for (int i = 0; i < 3; i++) {
                        switch (i) {
                            case 0: category =  data.getJSONArray("tech");
                                break;
                            case 1: category =  data.getJSONArray("money");
                                break;
                            case 2: category = data.getJSONArray("sports");
                        }
                        for(int j = 0;j < category.length(); j++) {
                            value = category.getJSONObject(j);
                            title = value.getString("title");
                            resource = value.getString("source");
                            digest = value.getString("digest");
                            releaseTime = value.getString("ptime");
                            newsUrl = value.getString("link");
                            pic = value.getJSONArray("picInfo");
                            image = pic.getJSONObject(0);
                            imageBitmap = getHttpBitmap(image.getString("url"));
                            switch (i) {
                                case 0: techNewsList.add(new News(title, imageBitmap, releaseTime, resource, digest, newsUrl));
                                    break;
                                case 1: financeNewsList.add(new News(title, imageBitmap, releaseTime, resource, digest, newsUrl));
                                    break;
                                case 2: sportsNewsList.add(new News(title, imageBitmap, releaseTime, resource, digest, newsUrl));
                                    break;
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 3; i++) {
                                managers[i] = new LinearLayoutManager(recyclerViews[i].getContext());
                                recyclerViews[i].setLayoutManager(managers[i]);
                                switch (i) {
                                    case 0: adapters[i] = new NewsAdapter(techNewsList, recyclerViews[i].getContext());
                                    break;
                                    case 1: adapters[i] = new NewsAdapter(financeNewsList, recyclerViews[i].getContext());
                                    break;
                                    case 2: adapters[i] = new NewsAdapter(sportsNewsList, recyclerViews[i].getContext());
                                    break;
                                }
                                recyclerViews[i].setAdapter(adapters[i]);
                            }
                        }
                    });
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private   class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views[position]);
        }
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);;
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
