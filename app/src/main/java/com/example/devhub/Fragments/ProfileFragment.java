package com.example.devhub.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.devhub.Activities.MainContentPage;
import com.example.devhub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public interface GetBioInfo {
        public void getBioInfo(String bio);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    // Class Variables

    private View parent;
    private Typeface objectTypeface;
    private String currentLoggedInUser;
    private Dialog objectDialogWait;
    private Dialog userBioDialog;
    private String extractedBio;

    //Firebase Variables

    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private DocumentReference objectDocumentReference;

    //Java objects for XML views

    private CircleImageView profilePicIV;
    private ImageView backgroundPicIV;
    private TextView userNameTV, userEmailTV, userEmotionsTV, textStatusCountTV, imageStatusCountTV,
            genderTV, userCityTV, userCountryTV, userEmailCardTV;
    private Button goBackBtn, bioBtn;
    private TextView crossTV, bioTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_profile, container, false);

        setCustomFont();

        objectDialogWait = new Dialog(getContext());
        objectDialogWait.setContentView(R.layout.please_wait_layout);
        userBioDialog = new Dialog(getContext());
        userBioDialog.setContentView(R.layout.user_bio_layout);

        initializeObjects();
        loadUserProfileData(new GetBioInfo() {
            @Override
            public void getBioInfo(String bio) {
                extractedBio = bio;
            }
        });

        return parent;
    }

    private void loadUserProfileData(GetBioInfo objectGetBioInfo) {
        try {
            if (objectFirebaseAuth != null) {
                objectDialogWait.show();
                currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();

                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedInUser);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String linkOfProfileURl = documentSnapshot.getString("profileimageurl");

                        Glide.with(getContext()).load(linkOfProfileURl).into(backgroundPicIV);
                        Glide.with(getContext()).load(linkOfProfileURl).into(profilePicIV);

                        objectGetBioInfo.getBioInfo(documentSnapshot.getString("userbio"));

                        userEmailCardTV.setText(currentLoggedInUser);
                        userEmailTV.setText(currentLoggedInUser);

                        userNameTV.setText(documentSnapshot.getString("username"));
                        userNameTV.setAllCaps(true);
                        userNameTV.setTypeface(objectTypeface);

                        long textStatusCount = documentSnapshot.getLong("nooftextstatus");
                        long imageStatusCount = documentSnapshot.getLong("noofimagestatus");
                        long emotions = textStatusCount + imageStatusCount;

                        String userBio = documentSnapshot.getString("userbio");

                        if (emotions < 10) {
                            userEmotionsTV.setText("0" + Long.toString(emotions));
                        } else {
                            userEmotionsTV.setText(Long.toString(emotions));
                        }


                        if (textStatusCount < 10) {
                            textStatusCountTV.setText("0" + Long.toString(textStatusCount));
                        } else {
                            textStatusCountTV.setText(Long.toString(textStatusCount));
                        }


                        if (imageStatusCount < 10) {
                            imageStatusCountTV.setText("0" + Long.toString(imageStatusCount));
                        } else {
                            imageStatusCountTV.setText(Long.toString(imageStatusCount));
                        }

                        genderTV.setText(documentSnapshot.getString("gender"));
                        userCityTV.setText(documentSnapshot.getString("usercity"));
                        userCountryTV.setText(documentSnapshot.getString("usercountry"));

                        objectDialogWait.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), R.string.no_user_online, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setCustomFont() {
        objectTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/nauvo.ttf");
    }

    private void initializeObjects() {

        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            profilePicIV = parent.findViewById(R.id.profileFrag_miniProfilePicIV);
            backgroundPicIV = parent.findViewById(R.id.profileFrag_backgroundPicIV);

            userNameTV = parent.findViewById(R.id.profileFrag_userName);
            userEmailTV = parent.findViewById(R.id.profileFrag_userEmail);

            userEmotionsTV = parent.findViewById(R.id.profileFrag_emotionsTV);
            textStatusCountTV = parent.findViewById(R.id.profileFrag_textStatusTV);

            imageStatusCountTV = parent.findViewById(R.id.profileFrag_imagesStatusTV);
            genderTV = parent.findViewById(R.id.profileFrag_genderCardTV);

            userCityTV = parent.findViewById(R.id.profileFrag_addressCardTV);
            userCountryTV = parent.findViewById(R.id.profileFrag_countryCardTV);

            userEmailCardTV = parent.findViewById(R.id.profileFrag_emailCardTV);
            goBackBtn = parent.findViewById(R.id.profileFrag_goBackBtn);

            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), MainContentPage.class));
                }
            });

            crossTV = userBioDialog.findViewById(R.id.userBio_crossTV);
            crossTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userBioDialog.dismiss();
                }
            });

            bioTV = userBioDialog.findViewById(R.id.userBio_bioTV);
            bioBtn = parent.findViewById(R.id.profileFrag_bioButton);

            bioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userBioDialog.show();
                    bioTV.setText(extractedBio);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}