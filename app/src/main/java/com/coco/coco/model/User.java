package com.coco.coco.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name;
    private String photoUrl;
    private ArrayList<String> blockedCompanies;
    private ArrayList<String> blockedIngredients;

    public User() {}

    public User(String name, String photoUrl, ArrayList<String> blockedCompanies, ArrayList<String> blockedIngredients) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.blockedCompanies = blockedCompanies;
        this.blockedIngredients = blockedIngredients;
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

}
