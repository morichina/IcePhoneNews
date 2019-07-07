package com.example.icephonenews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private EditText phone;
    private EditText pwd;
    private String createTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button postRequest = (Button) findViewById(R.id.registerButton);
        postRequest.setOnClickListener(this);
        Button getRequest = (Button)findViewById(R.id.loginButton);
        getRequest.setOnClickListener(this);
        phone = findViewById(R.id.userName);
        pwd = findViewById(R.id.passWord);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            postRequestWithOkHttp();
        }
        if (v.getId() == R.id.loginButton) {
            getRequestWithOKHttp();
        }

    }

    private void postRequestWithOkHttp() {
        String phone1 = phone.getText().toString().trim();
        String pwd1 = pwd.getText().toString().trim();
        String key = "00d91e8e0cca2b76f515926a36db68f5";
        String serverPath = "https://www.apiopen.top/createUser?";
        String url = serverPath + "key=" + key + "&phone=" + phone1 + "&passwd=" + pwd1;
        if (TextUtils.isEmpty(phone1)||TextUtils.isEmpty(pwd1)) {
            Toast.makeText(MainActivity.this, "用户名或密码不能为空！ ", Toast.LENGTH_SHORT).show();
            return;
        }
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String msg = jsonObject.getString("msg");
                    Looper.prepare();
                    if(msg.equals("成功!")) {
                        Toast.makeText(MainActivity.this, "注册成功",  Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getRequestWithOKHttp() {
        String phone1 = phone.getText().toString().trim();
        String pwd1 = pwd.getText().toString().trim();
        String key = "00d91e8e0cca2b76f515926a36db68f5";
        String serverPath = "https://www.apiopen.top/login?";
        String url = serverPath + "key=" + key + "&phone=" + phone1 + "&passwd=" + pwd1;
        if (TextUtils.isEmpty(phone1)||TextUtils.isEmpty(pwd1)) {
            Toast.makeText(MainActivity.this, "用户名或密码不能为空！ ", Toast.LENGTH_SHORT).show();
            return;
        }
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String msg = jsonObject.getString("msg");
                    JSONObject data = jsonObject.getJSONObject("data");
                    createTime = data.getString("createTime");
                    Looper.prepare();
                    if(msg.equals("成功!")) {
                        Toast.makeText(MainActivity.this, "登陆成功",  Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ShowNews.class);
                        intent.putExtra("time", createTime);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "请输入正确的手机号与密码", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


