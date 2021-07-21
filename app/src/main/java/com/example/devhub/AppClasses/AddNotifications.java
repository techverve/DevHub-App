package com.example.devhub.AppClasses;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNotifications {
    public void generateNotification(String currentLoggedInUser,String actionOfUser,String statusType,String ownerOfPostEmail){
        Map<String,Object> objectMap=new HashMap<>();
        FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance(); //added

        objectMap.put("email",currentLoggedInUser);
        objectMap.put("action",actionOfUser);
        objectMap.put("type",statusType);
        objectFirebaseFirestore.collection("UserProfileData")
                .document(ownerOfPostEmail)
                .collection("Notifications")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(objectMap);
    }
}
