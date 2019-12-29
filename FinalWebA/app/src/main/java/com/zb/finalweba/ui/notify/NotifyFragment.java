package com.zb.finalweba.ui.notify;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zb.finalweba.R;
import com.zb.finalweba.adapter.NotifyAdapter;
import com.zb.finalweba.data.Notify;
import com.zb.finalweba.ui.login.LoginActivity;
import com.zb.finalweba.utils.ServerRequest;

//import org.json.JSONException;
//import org.json.JSONObject;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyFragment extends Fragment{

    private static final String TAG = "NotifyFragment";
    private int apiStatus = -1;
    private ArrayList<Notify> notifyArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotifyAdapter notifyAdapter;

    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notify, container, false);
        recyclerView = root.findViewById(R.id.recyclerView_notifyFrg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        notifyAdapter = new NotifyAdapter(getActivity(), notifyArrayList);
        recyclerView.setAdapter(notifyAdapter);

        //设置item间距
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),3);
                outRect.bottom = UIUtil.dip2px(view.getContext(),3);
                outRect.left = UIUtil.dip2px(view.getContext(),3);
                outRect.right = UIUtil.dip2px(view.getContext(),3);
            }
        });

        //设置下拉刷新
        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        //设置样式，经典款
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        //initialData();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AuthData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "NULL");
        if(token.equals("null")||token.equals(null)){
//            跳转到登录
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "请重新登录", Toast.LENGTH_SHORT).show();

        }

        //使用okHttp
        OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://45.63.22.196:8082/notify")
                    .addHeader("Authorization",token)
                    .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    Log.d("postForm", "onFailure: " + e.getMessage());
                }

                public void onResponse(Call call, Response response) throws IOException {
                Log.d("getNotify", response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("getFormHeader", headers.name(i) + ":" + headers.value(i));
                }

                String responseBody = response.body().string();
                Log.d("getForm", "onResponseBody: " + responseBody);

                //获取通知json数据
                try {
                    Gson g = new Gson();
                    JsonObject jsonObject = g.fromJson(responseBody,JsonObject.class);
                    apiStatus = jsonObject.get("status").getAsInt();
                    Log.d("NotifyActivity", "test1"+apiStatus);
                    if(apiStatus!=200){
                        Log.d("NotifyActivity", "未登录");
                        return;
                    }
                    //获取的data，其属性值仍为Json
                    JsonObject JsonData = jsonObject.getAsJsonObject("data");
                    JsonArray JsonNotifies = JsonData.getAsJsonArray("notifies");
                    Log.d("NotifyActivity", "获取JsonData"+JsonData);
                    Log.d("NotifyActivity", "JsonNotifies"+JsonNotifies);
                    //遍历JsonArray
                    Iterator it = JsonNotifies.iterator();
                    while (it.hasNext())
                    {
                        JsonElement e = (JsonElement)it.next();
                        //JsonElement转换为JavaBean
                        Notify notify = new Notify();
                        notify = g.fromJson(e,Notify.class);
                        notifyArrayList.add(notify);
                        Log.d("NotifyActivity", "notify.getContent : "+notify.getContent());
                        Log.d("NotifyActivity", "notify.getAddress : "+notify.getAddress());
                    }
                    Log.d(TAG, "test data: "+notifyArrayList.size());
                    if(getActivity()==null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyAdapter.notifyDataSetChanged();
                        }
                    });


                } catch (JsonIOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(apiStatus!=200){
            Toast.makeText(getActivity(), "请重新登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        // Inflate the layout for this fragment

        return root;
    }
        
    private void initialData() {
        //测试数据
//        for (int i = 0; i < 50; i++) {
//            Notify notify = new Notify();
//            notify.setAddress("测" + i);
//            notifyArrayList.add(notify);
//        }
    }

}
