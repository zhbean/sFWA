package com.zb.finalweba.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zb.finalweba.ui.notify.NotifyFragment;
import com.zb.finalweba.ui.profile.ProfileFragment;
import com.zb.finalweba.ui.subscription.SubscriptionFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {


    //1
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //2
        switch (position){
            case 0: NotifyFragment fragment1=new NotifyFragment();
            return fragment1;
            case 1:
                SubscriptionFragment fragment2=new SubscriptionFragment();
            return  fragment2;
            case 2:
                ProfileFragment fragment3=new ProfileFragment();
            return  fragment3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "notify";
            case 1:return  "subscribe";
            case 2:return "profie";
            default:return "error";
        }
    }
}
