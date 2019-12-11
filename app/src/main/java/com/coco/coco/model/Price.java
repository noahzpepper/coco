package com.coco.coco.model;

import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Price implements Serializable  {

    private static final String[] LOGO_URLS = new String[]{"amazon_logo.png", "walmart_logo.png", "target_logo.png"};

    private String logoUrl;
    private String title;
    private double price;
    private String urlString;

    public Price() {}

    public Price(String logoUrl, String title, double price, String urlString) {
        this.logoUrl = logoUrl;
        this.title = title;
        this.price = price;
        this.urlString = urlString;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    //Caching logos
    private static transient Map<String, Uri> logoCache;

    //Must run at startup to use logo caching!
    public static void initializeLogos() {
        logoCache = new HashMap<>();
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        for (final String logoUrl : LOGO_URLS) {
            ref.child(logoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    logoCache.put(logoUrl, uri);
                }
            });
        }
    }

    public void loadLogoIntoImageView(ImageView imageView) {
        Uri logoUri = null;
        if (logoCache != null) {
            logoUri = logoCache.get(logoUrl);
        }
        Picasso.get().load(logoUri).into(imageView);
    }

}
