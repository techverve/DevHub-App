package com.example.devhub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.devhub.AdaptersClasses.AddThoughtsTabsAdapter;
import com.example.devhub.Fragments.addImageThoughtFragment;
import com.example.devhub.Fragments.addTextThoughtFragment;
import com.example.devhub.R;
import com.google.android.material.tabs.TabLayout;

public class AddThoughtsPage extends AppCompatActivity {

    private AddThoughtsTabsAdapter objectAddThoughtsTabAdapter;
    private TabLayout objectTabLayout;
    private ViewPager objectViewPager;

    private int[] tabIcons= {
            R.drawable.ic_tech_thought,R.drawable.ic_image_thought
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thoughts_page);

        addJavaObjectsToXMLObjects();
        objectAddThoughtsTabAdapter=new AddThoughtsTabsAdapter(getSupportFragmentManager());
        objectAddThoughtsTabAdapter.addFragment(new addTextThoughtFragment(),"Text");
        objectAddThoughtsTabAdapter.addFragment(new addImageThoughtFragment(),"Image");

        objectViewPager.setAdapter(objectAddThoughtsTabAdapter);
        objectTabLayout.setupWithViewPager(objectViewPager);
        setUpIcons();
    }
    private void setUpIcons()
    {
        try {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);

        }
        catch(Exception e)
        {
            Toast.makeText(this,"AddThoughts:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void addJavaObjectsToXMLObjects()
    {
        try{
            objectTabLayout=findViewById(R.id.AddThoughts_tabslayout);
            objectViewPager=findViewById(R.id.AddThoughts_PageViewer);

        }
        catch(Exception e)
        {
            Toast.makeText(this,"AddThoughts:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}