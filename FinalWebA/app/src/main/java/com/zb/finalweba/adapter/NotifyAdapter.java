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
        TextView textView;
        public NotifyHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.notify_address);
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
        holder.textView.setText(notify.getAddress());

    }


    @Override
    public int getItemCount() {
        return notifiesArrayList.size();
    }
}
