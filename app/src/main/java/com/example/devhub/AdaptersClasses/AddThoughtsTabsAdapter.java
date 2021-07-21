package com.example.devhub.AdaptersClasses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class AddThoughtsTabsAdapter extends FragmentStatePagerAdapter{

    private final List<Fragment> objectListFragments=new ArrayList<>();
    private final List<String> objectListFragmentNames=new ArrayList<>();

    public AddThoughtsTabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment objectFragment,String titleFragment)
    {
        objectListFragments.add(objectFragment);
        objectListFragmentNames.add(titleFragment);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return objectListFragmentNames.get(position);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return objectListFragments.get(position);
    }

    @Override
    public int getCount() {
        return objectListFragmentNames.size();
    }
}
