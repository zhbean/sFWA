package com.zb.finalweba.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ServerRequest {
    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 15 * 1000;
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000;
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static volatile ServerRequest sInstance;
    private OkHttpClient mOkHttpClient;

    private ServerRequest() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                //FaceBook 网络调试器，可在Chrome调试网络请求，查看SharePreferences,数据库等
                .addNetworkInterceptor(new StethoInterceptor())
                //.addInterceptor(new InterceptResponse())
                //http数据log，日志中打印出HTTP请求&响应数据
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
    }
    public static ServerRequest getInstance() {
        if (sInstance == null) {
            synchronized (ServerRequest.class) {
                if (sInstance == null) {
                    sInstance = new ServerRequest();
                }
            }
        }
        return sInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void setCache(Context appContext) {
        final File baseDir = appContext.getApplicationContext().getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            mOkHttpClient.newBuilder().cache((new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE)));
        }
    }

    //异步get请求
    public static void getAsyn(String url,Callback callBack){
        OkHttpClient okHttpClient = ServerRequest.getInstance().getOkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callBack);
    }
    //异步post请求
    public static void postAsyn(String url, RequestBody body, Callback callBack){
        OkHttpClient okHttpClient = ServerRequest.getInstance().getOkHttpClient();
        Request request = new Request.Builder().url(url).post(body).build();
        okHttpClient.newCall(request).enqueue(callBack);
    }






    /**
     * 发起网络请求，回调在ui线程
     *
     * @param <T>       数据类型
     */
    public static <T> void loginRequest(String username,String password) {
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("http://45.63.22.196:8082/authentication/form")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.d("postForm", "onFailure: " + e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                Log.d("postForm", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("postForm", headers.name(i) + ":" + headers.value(i));
                }

                String responseBody = response.body().string();
                Log.d("postForm", "onResponseBody: " + responseBody);

            }
        });
    }

}
