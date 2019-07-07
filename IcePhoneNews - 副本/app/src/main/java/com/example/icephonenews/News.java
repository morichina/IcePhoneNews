package com.example.icephonenews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class News implements Serializable {

    private String title;// 新闻标题

    private Bitmap imageBitmap;// 新闻图像的链接

    private String releaseTime;// 新闻发布时间

    private String resource;// 新闻发布单位

    private String digest;

    private String newsUrl;

    // News的构造函数
    public News(String title, Bitmap imageBitmap, String releaseTime, String resource, String digest, String url) {
        this.imageBitmap = imageBitmap;
        this.title = title;
        this.releaseTime = releaseTime;
        this.resource = resource;
        this.digest = digest;
        this.newsUrl = url;
    }

    /*public Bitmap getImageUrl() {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) { this.resource = resource; }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
