package com.coco.coco.model;

import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String id;
    private String name;
    private String photoUrl;
    private ArrayList<String> blockedCompanies;
    private ArrayList<String> blockedIngredients;

    public User() {}

    public User(String id, String name, String photoUrl, ArrayList<String> blockedCompanies, ArrayList<String> blockedIngredients) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.blockedCompanies = blockedCompanies;
        this.blockedIngredients = blockedIngredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getBlockedCompanies() {
        return blockedCompanies;
    }

    public void setBlockedCompanies(ArrayList<String> blockedCompanies) {
        this.blockedCompanies = blockedCompanies;
    }

    public ArrayList<String> getBlockedIngredients() {
        return blockedIngredients;
    }

    public void setBlockedIngredients(ArrayList<String> blockedIngredients) {
        this.blockedIngredients = blockedIngredients;
    }

    public void loadUserPhotoIntoImageView(final ImageView imageView) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        String image = (photoUrl == null) ? "account_button.png" : photoUrl;
        ref.child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
    }

}
