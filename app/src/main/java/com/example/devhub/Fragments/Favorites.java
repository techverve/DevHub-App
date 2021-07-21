package com.example.devhub.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.FavoriteStatusTabAdapter;
import com.example.devhub.R;
import com.google.android.material.tabs.TabLayout;

public class Favorites extends Fragment {

    //XML Variables
    private View parent;
    private TabLayout objectTabLayout;
    private ViewPager objectViewPager;

    //Class Variables
    FavoriteStatusTabAdapter objectFavoriteStatusTabAdapter;
    favoriteImageStatusFragment objectFavoriteImageStatusFragment;
    favoriteTextStatusFragment objectfavoriteTextStatusFragment;

    private int[] tabIcons = {
            R.drawable.ic_tech_thought, R.drawable.ic_image_thought
    };

    public Favorites() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_favorites, container, false);

        initializeVariables();
        addFragmentsToTabLayout();

        return parent;
    }

    private void setUpTabIcons(){
        try {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();;
        }
    }

    private void addFragmentsToTabLayout(){
        try {
            objectFavoriteStatusTabAdapter = new FavoriteStatusTabAdapter(getChildFragmentManager());

            objectFavoriteStatusTabAdapter.addFragment(objectfavoriteTextStatusFragment);
            objectFavoriteStatusTabAdapter.addFragment(objectFavoriteImageStatusFragment);

            objectViewPager.setAdapter(objectFavoriteStatusTabAdapter);
            objectTabLayout.setupWithViewPager(objectViewPager);
            objectViewPager.setSaveEnabled(false);

            setUpTabIcons();

        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();;
        }
    }
    private void initializeVariables(){
        try {
            objectfavoriteTextStatusFragment = new favoriteTextStatusFragment();
            objectFavoriteImageStatusFragment = new favoriteImageStatusFragment();

            objectTabLayout = parent.findViewById(R.id.favoriteFragment_tabLayout);
            objectViewPager = parent.findViewById(R.id.favoriteFragment_viewPager);
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();;
        }
    }
}