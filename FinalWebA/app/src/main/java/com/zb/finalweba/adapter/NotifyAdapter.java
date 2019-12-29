package com.zb.finalweba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zb.finalweba.R;
import com.zb.finalweba.data.Notify;

import java.util.ArrayList;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyHolder> {
    private Context mContext;
    private ArrayList<Notify> notifiesArrayList=new ArrayList<>();


    public NotifyAdapter(Context context,ArrayList<Notify> arrayList){
        notifiesArrayList=arrayList;
        mContext=context;
    }
    static class  NotifyHolder extends RecyclerView.ViewHolder{
        View notifyView;
        TextView notifyUrlTv;
        TextView notifyContent;
        TextView notifyTime;

        public NotifyHolder(@NonNull View itemView) {
            super(itemView);
            notifyUrlTv=itemView.findViewById(R.id.notify_url_address);
            notifyContent=itemView.findViewById(R.id.notify_content);
            notifyTime=itemView.findViewById(R.id.notify_time);
        }
    }

    @NonNull
    @Override
    public NotifyAdapter.NotifyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate((R.layout.notify_item),parent,false);
        NotifyHolder notifyHolder=new NotifyHolder(view);
        return notifyHolder;
    }

    //GUN DONG TIAN SHU JU
    @Override
    public void onBindViewHolder(@NonNull NotifyHolder holder, int position) {
        Notify notify =notifiesArrayList.get(position);
        holder.notifyUrlTv.setText(notify.getAddress());
        holder.notifyContent.setText(notify.getContent());
        holder.notifyTime.setText(notify.getNtime());
    }


    @Override
    public int getItemCount() {
        return notifiesArrayList.size();
    }
}
