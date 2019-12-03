package com.coco.coco.model;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Product implements Serializable {

    private String id;
    private String category; //none, face, eyes, lips, brows, skincare
    private String company;
    private String description;
    private String productImage;
    private ArrayList<String> communityImageUrls;
    private ArrayList<String> ingredients;
    private boolean ecoCertified;
    private String name;
    private double overallRating;
    private double skinToneRating;
    private ArrayList<String> reviewIds;
    private Map<String, Price> prices;

    public Product() {}

    public Product(String id, String category, String company, String description, String productImage, ArrayList<String> communityImageUrls, ArrayList<String> ingredients, boolean ecoCertified, String name, double overallRating, double skinToneRating, ArrayList<String> reviewIds, Map<String, Price> prices) {
        this.id = id;
        this.category = category;
        this.company = company;
        this.description = description;
        this.productImage = productImage;
        this.communityImageUrls = communityImageUrls;
        this.ingredients = ingredients;
        this.ecoCertified = ecoCertified;
        this.name = name;
        this.overallRating = overallRating;
        this.skinToneRating = skinToneRating;
        this.reviewIds = reviewIds;
        this.prices = prices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public ArrayList<String> getCommunityImageUrls() {
        return communityImageUrls;
    }

    public void setCommunityImageUrls(ArrayList<String> communityImageUrls) {
        this.communityImageUrls = communityImageUrls;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isEcoCertified() {
        return ecoCertified;
    }

    public void setEcoCertified(boolean ecoCertified) {
        this.ecoCertified = ecoCertified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public double getSkinToneRating() {
        return skinToneRating;
    }

    public void setSkinToneRating(double skinToneRating) {
        this.skinToneRating = skinToneRating;
    }

    public ArrayList<String> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(ArrayList<String> reviewIds) {
        this.reviewIds = reviewIds;
    }

    public Map<String, Price> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Price> prices) {
        this.prices = prices;
    }

    public void loadProductImageIntoImageView(final ImageView imageView) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        String image = (productImage == null) ? "placeholder_image.png" : productImage;
        ref.child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
    }

    public void loadCommunityImagesIntoImageViews(final ImageView[] imageViews) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        int i;
        for (i = 0; communityImageUrls != null && i < communityImageUrls.size(); i++) {
            if (i >= imageViews.length) { return; }
            final int j = i;
            ref.child(communityImageUrls.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageViews[j]);
                }
            });
        }
        for (; i < imageViews.length; i++) {
            imageViews[i].setVisibility(View.GONE);
        }
    }

    public Price priceLowest() {
        Price lowestPrice = null;
        for (String k : prices.keySet()) {
            Price price = prices.get(k);
            if (price != null && (lowestPrice == null || price.getPrice() < lowestPrice.getPrice())) {
                lowestPrice = price;
            }
        }
        return lowestPrice;
    }

}

