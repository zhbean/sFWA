package com.zb.finalweba.utils;

import android.accounts.AuthenticatorException;
import android.app.AuthenticationRequiredException;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_PROXY_AUTH;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class InterceptResponse implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException, AuthenticationRequiredException {
        // 拦截请求，获取到该次请求的request
        Request request = chain.request();
        // 执行本次网络请求操作，返回response信息
        Response response = chain.proceed(request);
        String resBody = response.body().toString();
        JSONObject jsonObject = null;
        int status = -1;
        try {
            jsonObject = new JSONObject(resBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            status = jsonObject.getInt("status");
            switch(status) {
                case -1:
                    throw new IllegalStateException();
                case HTTP_UNAUTHORIZED://401  没有认证
                    throw new IllegalStateException();
                case 403://403  认证失效
                    throw new IllegalStateException();
                default:
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 注意，这样写，等于重新创建Request，获取新的Response，避免在执行以上代码时，
        // 调用了responseBody.string()而不能在返回体中再次调用。
        return response.newBuilder().build();
    }

}
