package com.example.devhub.Fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.devhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    public interface GetUserInfo {
        public void getUserInfo(String urlOfPP, String userName);
    }

    // Class Variables

    private View parent;
    private boolean checkForChangeDp = false;
    private Uri newProfileURI;
    private static int REQUEST_CODE = 1;
    private Dialog objectDialogWait;
    private String extractedURL, extractedName;

    //Firebase Variables

    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private DocumentReference objectDocumentReference;
    private StorageReference objectStorageReference;
    private FirebaseStorage objectFirebaseStorage;

    //Java objects for XML views

    private CircleImageView profileIV;
    private EditText userNameET, userBioET, userCountryET, userCityET;
    private Button updateUserInfoBtn, updateDpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_settings, container, false);

        objectDialogWait = new Dialog(getContext());
        objectDialogWait.setContentView(R.layout.please_wait_layout);

        initializeObjects();
        loadProfileInformationAtStart(new GetUserInfo() {
            @Override
            public void getUserInfo(String urlOfPP, String userName) {
                extractedName = userName;
                extractedURL = urlOfPP;
            }
        });
        updateDpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        updateUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
        return parent;
    }

    private String getExtension(Uri uri) {
        ContentResolver objectContentResolver = getActivity().getContentResolver();
        MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

        return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
    }

    private void updateUserInfo() {
        try {
            if (checkForChangeDp) {
                objectDialogWait.show();
                objectStorageReference = FirebaseStorage.getInstance().getReference("ImageFolder");
                if (newProfileURI != null) {
                    String imageName = extractedName + "." + getExtension(newProfileURI);
                    StorageReference imgRef = objectStorageReference.child(imageName);

                    UploadTask uploadTask = imgRef.putFile(newProfileURI);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imgRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> objectMap = new HashMap<>();

                                objectMap.put("profileimageurl", task.getResult().toString());
                                objectMap.put("username", userNameET.getText().toString());
                                objectMap.put("userbio", userBioET.getText().toString());
                                objectMap.put("usercity", userCityET.getText().toString());
                                objectMap.put("usercountry", userCountryET.getText().toString());

                                objectFirebaseFirestore.collection("UserProfileData")
                                        .document(objectFirebaseAuth.getCurrentUser().getEmail())
                                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        objectDialogWait.dismiss();
                                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        objectDialogWait.dismiss();
                                        Toast.makeText(getContext(), "Failed to update Profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if (!task.isSuccessful()){
                                objectDialogWait.dismiss();
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Please choose an Image for DP", Toast.LENGTH_SHORT).show();
                }

            } else if (!checkForChangeDp) {
                objectDialogWait.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("username", userNameET.getText().toString());
                objectMap.put("userbio", userBioET.getText().toString());
                objectMap.put("usercity", userCityET.getText().toString());
                objectMap.put("usercountry", userCountryET.getText().toString());

                objectFirebaseFirestore.collection("UserProfileData")
                        .document(objectFirebaseAuth.getCurrentUser().getEmail())
                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        objectDialogWait.dismiss();
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialogWait.dismiss();
                        Toast.makeText(getContext(), "Failed to update Profile", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null && data != null) {
            newProfileURI = data.getData();
            profileIV.setImageURI(newProfileURI);
            checkForChangeDp = true;
        } else {
            Toast.makeText(getContext(), "No image was chosen", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(galleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileInformationAtStart(GetUserInfo objectGetUserInfo) {
        try {
            if (objectFirebaseAuth != null) {
                objectDialogWait.show();
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(objectFirebaseAuth.getCurrentUser().getEmail());

                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userNameET.setText(documentSnapshot.getString("username"));
                        userBioET.setText(documentSnapshot.getString("userbio"));
                        userCityET.setText(documentSnapshot.getString("usercity"));
                        userCountryET.setText(documentSnapshot.getString("usercountry"));

                        Glide.with(getContext()).load(documentSnapshot.getString("profileimageurl"))
                                .into(profileIV);

                        objectGetUserInfo.getUserInfo(documentSnapshot.getString("profileimageurl")
                                , documentSnapshot.getString("username"));
                        objectDialogWait.dismiss();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialogWait.dismiss();
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

    private void initializeObjects() {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseStorage = FirebaseStorage.getInstance();

            profileIV = parent.findViewById(R.id.settingFrag_profilePicIV);
            updateDpBtn = parent.findViewById(R.id.settingFrag_changeDpBtn);
            updateUserInfoBtn = parent.findViewById(R.id.settingFrag_updateInfoBtn);
            userNameET = parent.findViewById(R.id.settingFrag_userNameET);
            userBioET = parent.findViewById(R.id.settingFrag_userBioET);
            userCityET = parent.findViewById(R.id.settingFrag_userCityET);
            userCountryET = parent.findViewById(R.id.settingFrag_userCountryET);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}