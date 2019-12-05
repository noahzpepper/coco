package com.coco.coco.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Review implements Serializable {

    private String userId;
    private String reviewText;
    private int overallRating;
    private int skinToneRating;
    private ArrayList<String> imageUrls;

    public Review() {}

    public Review(String userId, String reviewText, int overallRating, int skinToneRating, ArrayList<String> imageUrls) {
        this.userId = userId;
        this.reviewText = reviewText;
        this.overallRating = overallRating;
        this.skinToneRating = skinToneRating;
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getSkinToneRating() {
        return skinToneRating;
    }

    public void setSkinToneRating(int skinToneRating) {
        this.skinToneRating = skinToneRating;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
