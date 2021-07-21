package com.example.devhub.AdaptersClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteStatusTabAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> objectListFragment = new ArrayList<>();
    public FavoriteStatusTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return objectListFragment.get(position);
    }

    @Override
    public int getCount() {
        return objectListFragment.size();
    }

    public void addFragment(Fragment objectFragment){
        objectListFragment.add(objectFragment);
    }
}
