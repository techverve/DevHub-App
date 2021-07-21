package com.example.devhub.AdaptersClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devhub.Activities.TextCommentPage;
import com.example.devhub.AppClasses.AddNotifications;
import com.example.devhub.GlideApp;
import com.example.devhub.ModelClasses.Model_TechStatus;
import com.example.devhub.MyAppGlideModule;
import com.example.devhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.devhub.R.string.no_user_online;
import static com.example.devhub.R.string.tech_thoughts;

public class TechStatusAdapterClass extends FirestoreRecyclerAdapter<Model_TechStatus, TechStatusAdapterClass.TechStatusViewHolder> {

    public TechStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_TechStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TechStatusViewHolder techStatusViewHolder, int i, @NonNull Model_TechStatus model_techStatus) {

        techStatusViewHolder.userStatusIV.setText(model_techStatus.getUseremail());
        techStatusViewHolder.userEmailTV.setText(model_techStatus.getUseremail());
        techStatusViewHolder.dateTimeIV.setText(model_techStatus.getCurrentdatetime());
        techStatusViewHolder.userStatusIV.setText(model_techStatus.getStatus());
        techStatusViewHolder.heartCountIV.setText(Integer.toString(model_techStatus.getNooflove()));
        techStatusViewHolder.hahaCountIV.setText(Integer.toString(model_techStatus.getNoofhaha()));
        techStatusViewHolder.sadCountIV.setText(Integer.toString(model_techStatus.getNoofsad()));
        techStatusViewHolder.commentCountIV.setText(Integer.toString(model_techStatus.getNoofcomments()));

        String linkOfProfileImage = model_techStatus.getProfileurl();
        GlideApp.with(techStatusViewHolder.profileIV.getContext())
                .load(linkOfProfileImage).into(techStatusViewHolder.profileIV);

        AddNotifications objectAddNotifications=new AddNotifications();

        techStatusViewHolder.heartIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();
                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objectDocumentReference = objectFirebaseFirestore.collection
                            ("TextStatus").document(documentID).collection("Emotions")
                            .document(userEmail);

                    objectDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentFlag");
                                objectAddNotifications.generateNotification(userEmail,"love","text status",model_techStatus.getUseremail());

                                if (currentFlag.equals("love")) {
                                    objectDocumentReference.update("currentFlag", "love");

                                } else if (currentFlag.equals("haha")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("nooflove"
                                    );
                                    totalHearts++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objectDocumentReference.update("currentFlag", "love");

                                    Long totalHaha = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofhaha"
                                    );
                                    totalHaha--;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                } else if (currentFlag.equals("sad")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalHearts++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objectDocumentReference.update("currentFlag", "love");

                                    Long totalSad = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofsad");

                                    totalSad--;

                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);

                                }

                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentFlag", "love");

                                objectFirebaseFirestore.collection("TextStatus").document(
                                        documentID).collection("Emotions").document(
                                        userEmail).set(objectMap);

                                Long totalHearts = (Long) getSnapshots().getSnapshot(
                                        techStatusViewHolder.getAdapterPosition()).get("nooflove"
                                );
                                totalHearts++;
                                getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                        .getReference().update("nooflove", totalHearts);

                                objectDocumentReference.update("currentFlag", "love");
                                objectAddNotifications.generateNotification(userEmail,"love","text status",model_techStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(techStatusViewHolder.heartIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(techStatusViewHolder.heartIV.getContext(), R.string.no_user_online, Toast.LENGTH_SHORT).show();
                }
            }
        });

        techStatusViewHolder.hahaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();
                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objectDocumentReference = objectFirebaseFirestore.collection
                            ("TextStatus").document(documentID).collection("Emotions")
                            .document(userEmail);

                    objectDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentFlag");
                                objectAddNotifications.generateNotification(userEmail,"haha","text status",model_techStatus.getUseremail());

                                if (currentFlag.equals("haha")) {
                                    objectDocumentReference.update("currentFlag", "haha");

                                } else if (currentFlag.equals("love")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objectDocumentReference.update("currentFlag", "haha");

                                    Long totalLove = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);

                                } else if (currentFlag.equals("sad")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objectDocumentReference.update("currentFlag", "haha");

                                    Long totalSad = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad--;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);

                                }

                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentFlag", "haha");

                                objectFirebaseFirestore.collection("TextStatus").document(
                                        documentID).collection("Emotions").document(
                                        userEmail).set(objectMap);

                                Long totalHaha = (Long) getSnapshots().getSnapshot(
                                        techStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                totalHaha++;
                                getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofhaha", totalHaha);

                                objectDocumentReference.update("currentFlag", "haha");
                                objectAddNotifications.generateNotification(userEmail,"haha","text status",model_techStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(techStatusViewHolder.hahaIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(techStatusViewHolder.hahaIV.getContext(), R.string.no_user_online, Toast.LENGTH_SHORT).show();
                }
            }
        });

        techStatusViewHolder.sadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();
                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objectDocumentReference = objectFirebaseFirestore.collection
                            ("TextStatus").document(documentID).collection("Emotions")
                            .document(userEmail);

                    objectDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentFlag");
                                objectAddNotifications.generateNotification(userEmail,"sad","text status",model_techStatus.getUseremail());

                                if (currentFlag.equals("sad")) {
                                    objectDocumentReference.update("currentFlag", "sad");

                                } else if (currentFlag.equals("love")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objectDocumentReference.update("currentFlag", "sad");

                                    Long totalLove = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);

                                } else if (currentFlag.equals("haha")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad++;
                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objectDocumentReference.update("currentFlag", "sad");

                                    Long totalHaha = (Long) getSnapshots().getSnapshot(
                                            techStatusViewHolder.getAdapterPosition()).get("noofhaha");

                                    totalHaha--;

                                    getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofhaha", totalHaha);

                                }

                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentFlag", "sad");

                                objectFirebaseFirestore.collection("TextStatus").document(
                                        documentID).collection("Emotions").document(
                                        userEmail).set(objectMap);

                                Long totalSad = (Long) getSnapshots().getSnapshot(
                                        techStatusViewHolder.getAdapterPosition()).get("noofsad"
                                );
                                totalSad++;
                                getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofsad", totalSad);

                                objectDocumentReference.update("currentFlag", "sad");
                                objectAddNotifications.generateNotification(userEmail,"sad","text status",model_techStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(techStatusViewHolder.sadIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(techStatusViewHolder.sadIV.getContext(), R.string.no_user_online, Toast.LENGTH_SHORT).show();
                }
            }
        });

        techStatusViewHolder.commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documentID = getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId();
                Context objectContext = techStatusViewHolder.commentIV.getContext();

                Intent objectIntent = new Intent(objectContext, TextCommentPage.class);
                objectIntent.putExtra("documentId", documentID);

                objectIntent.putExtra("userEmailID",model_techStatus.getUseremail());
                objectContext.startActivity(objectIntent);

            }
        });

        techStatusViewHolder.favoriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documentId = getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId();
                FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

                DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("TextStatus").document(documentId);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userEmail = documentSnapshot.getString("useremail");
                        String status = documentSnapshot.getString("status");
                        String profileUrl = documentSnapshot.getString("profileurl");
                        String statusDate = documentSnapshot.getString("currentdatetime");

                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("useremail", userEmail);
                        objectMap.put("status", status);
                        objectMap.put("profileurl", profileUrl);
                        objectMap.put("currentdatetime", statusDate);

                        FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();

                        if(objectFirebaseAuth != null){
                            String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                            objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                    .collection("FavoriteTechStatus")
                                    .document(documentId).set(objectMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(techStatusViewHolder.favoriteIV.getContext(), R.string.status_added_to_favorites, Toast.LENGTH_SHORT).show();
                                            objectAddNotifications.generateNotification(userEmail,"favorite","text status",model_techStatus.getUseremail());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(techStatusViewHolder.favoriteIV.getContext(), R.string.failed_to_add_status_to_favorites, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(techStatusViewHolder.favoriteIV.getContext(), no_user_online, Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        techStatusViewHolder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if (objectFirebaseAuth.getCurrentUser().getEmail().equals(model_techStatus.getUseremail()))
                {
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("TextStatus")
                            .document(getSnapshots().getSnapshot(techStatusViewHolder.getAdapterPosition()).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(techStatusViewHolder.deleteIV.getContext(), R.string.status_deleted, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(techStatusViewHolder.deleteIV.getContext(), R.string.failed_to_delete_status, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(techStatusViewHolder.deleteIV.getContext(), R.string.cant_delete_this_status, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @NonNull
    @Override
    public TechStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TechStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_tech_status, parent, false
        ));
    }

    public class TechStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIV;
        ImageView heartIV, hahaIV, sadIV, deleteIV, commentIV, favoriteIV;
        TextView userEmailTV, dateTimeIV, userStatusIV, sadCountIV, heartCountIV, hahaCountIV, commentCountIV;

        public TechStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIV = itemView.findViewById(R.id.model_techStatus_profileIV);
            heartIV = itemView.findViewById(R.id.model_techStatus_heartIV);
            hahaIV = itemView.findViewById(R.id.model_techStatus_hahaIV);
            sadIV = itemView.findViewById(R.id.model_techStatus_sadIV);
            deleteIV = itemView.findViewById(R.id.model_techStatus_deleteIV);
            commentIV = itemView.findViewById(R.id.model_techStatus_commentIV);
            favoriteIV = itemView.findViewById(R.id.model_techStatus_favouriteTechStatus);
            userEmailTV = itemView.findViewById(R.id.model_techStatus_emailTV);
            dateTimeIV = itemView.findViewById(R.id.model_techStatus_dateIV);
            userStatusIV = itemView.findViewById(R.id.model_techStatus_techStatusTV);
            heartCountIV = itemView.findViewById(R.id.model_techStatus_heartCountTV);
            sadCountIV = itemView.findViewById(R.id.model_techStatus_sadCountTV);
            hahaCountIV = itemView.findViewById(R.id.model_techStatus_hahaCountTV);
            commentCountIV = itemView.findViewById(R.id.model_techStatus_commentCount);

        }

    }
}
