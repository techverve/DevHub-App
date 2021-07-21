package com.example.devhub.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.devhub.Fragments.Favorites;
import com.example.devhub.Fragments.ImageThoughts;
import com.example.devhub.Fragments.TechThoughts;
import com.example.devhub.MainActivity;
import com.example.devhub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment objects
    private TechThoughts objectTechThoughts;
    private ImageThoughts objectsImageThoughts;
    private Favorites objectsFavorite;

    //Java objects for XML object
    private Toolbar objectToolbar; //okay
    private NavigationView objectNavigationView; //okay
    private DrawerLayout objectDrawerLayout; //ok

    private ImageView header_backgroundProfile;
    private CircleImageView header_profilePic;
    private TextView header_userName,header_userEmail;
    private ProgressBar header_progressBar;
    private BottomNavigationView objectBottomNavigationView;

    private FloatingActionButton objectFloatingActionButton;
    private TextView notificationsTextView;

    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;
    private CollectionReference objectCollectionReference;

    private FirebaseFirestore notificationsFirebaseFirestore;

    //Class variable
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        objectTechThoughts=new TechThoughts();
        objectsImageThoughts=new ImageThoughts();
        objectsFavorite=new Favorites();

        changeFragment(objectTechThoughts);

        objectFirebaseAuth=FirebaseAuth.getInstance();
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        notificationsFirebaseFirestore=FirebaseFirestore.getInstance();
        objectCollectionReference=notificationsFirebaseFirestore.collection("UserProfileData")
                .document(objectFirebaseAuth.getCurrentUser().getEmail().toString())
                .collection("Notifications");

        setContentView(R.layout.activity_main_page);
        attachJavaObjectToXMLObjects();
        setUpDrawerMenu();
        getCurrentUserDetails();

        objectNavigationView.setNavigationItemSelectedListener(this);
        objectBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try
                {
                    switch (item.getItemId())
                    {
                        case R.id.item_techThoughts:
                            changeFragment(objectTechThoughts);
                            return true;
                        case R.id.item_imageThoughts:
                            changeFragment(objectsImageThoughts);
                            return true;
                        case R.id.item_favThoughts:
                            changeFragment(objectsFavorite);
                            return true;
                        default:
                            return false;
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainContentPage.this, "MainContentPage" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        objectFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainContentPage.this,AddThoughtsPage.class));
            }
        });
    }

    private void changeFragment(Fragment objectFragment)
    {
        try{
            FragmentTransaction objectFragmentTransaction=getSupportFragmentManager().beginTransaction();
            objectFragmentTransaction.replace(R.id.container,objectFragment);
            objectFragmentTransaction.commit();

        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void attachJavaObjectToXMLObjects()
    {
        try {
            objectToolbar=findViewById(R.id.toolBar); //ok
            objectDrawerLayout=findViewById(R.id.drawerLayout); //
            objectNavigationView=findViewById(R.id.navigationView); //

            View headerXMLFile=objectNavigationView.getHeaderView(0);

            header_backgroundProfile=headerXMLFile.findViewById(R.id.header_profilePicBack);
            header_profilePic=headerXMLFile.findViewById(R.id.header_profilePic);
            header_userName=headerXMLFile.findViewById(R.id.header_userName);
            header_userEmail=headerXMLFile.findViewById(R.id.header_userEmail);
            header_progressBar=headerXMLFile.findViewById(R.id.header_progressBar);
            objectBottomNavigationView=findViewById(R.id.bottomNavigationViewBar);
            objectFloatingActionButton=findViewById(R.id.mainContentPage_addStatusFloatingBtn);
            objectToolbar.inflateMenu(R.menu.main_content_menu_bar);
            objectToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.maincontentpage_item_notificationicon:
                            startActivity(new Intent(MainContentPage.this,AllNotifications.class));
                            return true;
                    }
                    return false;
                }
            });
            notificationsTextView= (TextView) MenuItemCompat.getActionView(objectNavigationView.getMenu().findItem(R.id.item_notifications));
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void getCurrentUserDetails()
    {
        try {
            currentUserEmail=getCurrentLoggedInUser();
            if(currentUserEmail.equals("No user logged in"))
            {
                Toast.makeText(this,"No user logged in",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginPage.class)); //get login page
            }

            else
            {
                header_progressBar.setVisibility(View.VISIBLE);
                objectDocumentReference=objectFirebaseFirestore.collection("UserProfileData").document(currentUserEmail);

                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        header_userName.setText(documentSnapshot.getString("username"));
                        header_userName.setAllCaps(true);

                        header_userEmail.setText(currentUserEmail);
                        String linkOfProfileImage=documentSnapshot.getString("profileimageurl");
                        Glide.with(MainContentPage.this).load(linkOfProfileImage).into(header_profilePic);
                        Glide.with(MainContentPage.this).load(linkOfProfileImage).into(header_backgroundProfile);
                        header_progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainContentPage.this,"Loading User Details"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    //Gets the current user
    private String getCurrentLoggedInUser()
    {
        try{
            if(objectFirebaseAuth!=null)
            {
                return Objects.requireNonNull(Objects.requireNonNull(objectFirebaseAuth.getCurrentUser()).getEmail()).toString();
            }
            else
            {
                return "No user logged in";
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void setUpDrawerMenu()
    {
        try{
            ActionBarDrawerToggle objectActionBarDrawerToggle=new ActionBarDrawerToggle(this,objectDrawerLayout,objectToolbar,
                    R.string.open,R.string.close); //

            objectDrawerLayout.addDrawerListener(objectActionBarDrawerToggle);
            objectActionBarDrawerToggle.syncState();
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    //
    private void OpenDrawer()
    {
        try {
            objectDrawerLayout.openDrawer(GravityCompat.START);
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //
    private void closeDrawer()
    {
        try{
            objectDrawerLayout.openDrawer(GravityCompat.START);
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try{

            switch(item.getItemId() )
            {
                case R.id.item_profile:
                    startActivity(new Intent(this,ProfilePage.class));
                    closeDrawer();
                    return true;
                case R.id.item_notifications:
                    Toast.makeText(this,"Notification is clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_settings:
                    Toast.makeText(this,"Setting is clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_favourite:
                    Toast.makeText(this,"Fav is clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_techstatus:
                    Toast.makeText(this,"TechStatus is clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_signout:
                    Toast.makeText(this,"You have successfully been logged out",Toast.LENGTH_SHORT).show();
                    signOutUser();
                    return true;
                default:
                    return false;
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void signOutUser()
    {
        try {
            if(objectFirebaseAuth!=null)
            {
                objectFirebaseAuth.signOut();
                startActivity(new Intent(this,LoginPage.class));
                closeDrawer();
                finish();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private ListenerRegistration objectListenerRegistration;
    @Override
    protected void onStart() {
        super.onStart();
        objectListenerRegistration=objectCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(MainContentPage.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else if(!value.isEmpty()){
                    notificationsTextView.setGravity(Gravity.CENTER_VERTICAL);
                    notificationsTextView.setTypeface(null, Typeface.BOLD);
                    notificationsTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    int size=value.size();
                    notificationsTextView.setText(Integer.toString(size)+"     ");
                    Toast.makeText(MainContentPage.this,"You have :"+Integer.toString(size)+" notifications",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectListenerRegistration.remove();
    }
}