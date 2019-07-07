package com.example.icephonenews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public  class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;
    private LayoutInflater inflater;

    // NewsAdapter中的一个内部类
    static class  ViewHolder extends RecyclerView.ViewHolder {
        View newsView;
        ImageView newsImage;
        TextView newsTitle;
        TextView newsReleaseTime;
        TextView newsResource;
        TextView newsDigest;

        // 构造函数，传入RecyclerView子项的最外层布局
        ViewHolder(View view){
            super(view);
            newsView = view;
            // 获取布局中的控件实例
            newsImage = view.findViewById(R.id.newsImage);
            newsReleaseTime = view.findViewById(R.id.releaseTime);
            newsTitle = view.findViewById(R.id.newsTitle);
            newsResource = view.findViewById(R.id.newsResource);
            newsDigest = view.findViewById(R.id.newsDigest);
        }
    }

    // NewsAdapter的构造函数，传入展示的数据源和上下文
    NewsAdapter(List<News> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    // 将news_item布局传递进去，创建ViewHolder实例，并把加载出来布局传入到构造函数中，最后将viewHolder实例返回
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = View.inflate(context,R.layout.news_item,null);
        final ViewHolder holder = new ViewHolder(view);
        // 设置布局的监听器
        holder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                News news = newsList.get(position);// 得到News实例
                Intent intent = new Intent(context, NewsContent.class);// 设置intent的上下文
                intent.putExtra("key", news.getNewsUrl());// 通过intent传递News实例
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 获取News实例并通过其get方法获取属性设置布局的控件中内容
        News news = newsList.get(position);
        //holder.newsImage.setImageBitmap(news.getImageUrl());
        holder.newsImage.setImageBitmap(news.getImageBitmap());
        holder.newsTitle.setText(news.getTitle());
        holder.newsReleaseTime.setText(news.getReleaseTime());
        holder.newsResource.setText(news.getResource());
        holder.newsDigest.setText(news.getDigest());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
