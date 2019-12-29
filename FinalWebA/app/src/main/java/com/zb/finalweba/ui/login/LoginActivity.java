package com.zb.finalweba.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zb.finalweba.MainActivity;
import com.zb.finalweba.R;
import com.zb.finalweba.utils.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        clearToken();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //使用okHttp
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", usernameEditText.getText().toString())
                        .add("password", passwordEditText.getText().toString())
                        .build();
                ServerRequest.postAsyn("http://45.63.22.196:8082/authentication/form", requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("postForm", "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("postForm", response.protocol() + " " + response.code() + " " + response.message());
                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++) {
                            Log.d("postForm", headers.name(i) + ":" + headers.value(i));
                        }

                        String responseBody = response.body().string();
                        Log.d("postForm", "onResponseBody: " + responseBody);

                        //实现token存储
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            String token = jsonObject.getString("data");
                            //
                            SharedPreferences sharedPreferences = getSharedPreferences("AuthData", Context.MODE_PRIVATE);
                            //实例化.Editor对象
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (token.equals("null")) {
                                Log.d("LoginActivity", "登录失败");
//                                子程序不能使用toast
//                                Toast.makeText(LoginActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                editor.putString("token", token);
                                if (editor.commit()) {
                                    Log.d("LoginActivity", "登录成功 ");

                                    //跳转到主页
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("LoginActivity", "登录存储失败");
//                                    Toast.makeText(getApplicationContext(), "请重新登录", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private boolean clearToken() {
        Toast.makeText(getApplicationContext(), "请登录", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("AuthData", Context.MODE_PRIVATE);
        //实例化.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", null);
        if (editor.commit()) {
            Log.d("LoginActivity", "清除成功 ");
            return true;
        } else {
            return false;
        }
    }
}
