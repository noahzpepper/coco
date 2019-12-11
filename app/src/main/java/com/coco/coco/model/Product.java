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
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class Product implements Serializable {

    public enum Sort {
        RELEVANCE,
        PRICE_LOW_TO_HIGH,
        PRICE_HIGH_TO_LOW,
        OVERALL_RATING,
        SKINTONE_RATING
    }

    private String id;
    private String category; //none, face, eyes, lips, brows, skincare
    private String company;
    private String description;
    private String productImage;
    private ArrayList<String> ingredients;
    private boolean ecoCertified;
    private String name;
    private ArrayList<Review> reviews;
    private Map<String, Price> prices;

    public Product() {}

    public Product(String id, String category, String company, String description, String productImage, ArrayList<String> ingredients, boolean ecoCertified, String name, ArrayList<Review> reviews, Map<String, Price> prices) {
        this.id = id;
        this.category = category;
        this.company = company;
        this.description = description;
        this.productImage = productImage;
        this.ingredients = ingredients;
        this.ecoCertified = ecoCertified;
        this.name = name;
        this.reviews = reviews;
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

    public String calcOverallRating() {
        if (reviews == null || reviews.size() == 0) { return "---"; }
        double sum = 0.0;
        for (Review r : reviews) {
            sum += r.getOverallRating();
        }
        double rating = sum / reviews.size();
        return String.format(Locale.US, "%.2f", rating);
    }

    public String calcSkintoneRating() {
        if (reviews == null || reviews.size() == 0) { return "---"; }
        double sum = 0;
        for (Review r : reviews) {
            sum += r.getSkinToneRating();
        }
        double rating = sum / reviews.size();
        return String.format(Locale.US, "%.2f", rating);
    }

    public ArrayList<Review> getReviews() {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public Map<String, Price> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Price> prices) {
        this.prices = prices;
    }

    public ArrayList<String> gatherCommunityImageUrls() {
        ArrayList<String> urls = new ArrayList<>();
        if (reviews != null) {
            for (Review r : reviews) {
                ArrayList<String> rUrls = r.getImageUrls();
                if (rUrls != null) {
                    urls.addAll(rUrls);
                }
            }
        }
        return urls;
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
        ArrayList<String> communityImageUrls = gatherCommunityImageUrls();
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        int i;
        final Uri[] uriS = new Uri[1];
        for (i = 0; communityImageUrls != null && i < communityImageUrls.size(); i++) {
            if (i >= imageViews.length) { return; }
            final int j = i;
            ref.child(communityImageUrls.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageViews[j]);
                    uriS[0] = uri;
                }
            });
        }
        for (; i < imageViews.length; i++) {
            Picasso.get().load(uriS[0]).into(imageViews[i]);
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

    public boolean reviewedByUser(String uid) {
        for (Review review : getReviews()) {
            if (review.getUserId().equals(uid)) {
                return true;
            }
        }
        return false;
    }

    // Comparators

    private static final Comparator<Product> SORT_RELEVANCE = new Comparator<Product>() {
        @Override
        public int compare(Product product, Product t1) {
            return 0;
        }
    };

    private static final Comparator<Product> SORT_PRICE_LOW_TO_HIGH = new Comparator<Product>() {
        @Override
        public int compare(Product product, Product t1) {
            return (int) (100 * (product.priceLowest().getPrice() - t1.priceLowest().getPrice()));
        }
    };


    private static final Comparator<Product> SORT_PRICE_HIGH_TO_LOW = new Comparator<Product>() {
        @Override
        public int compare(Product product, Product t1) {
            return (int) (100 * (t1.priceLowest().getPrice() - product.priceLowest().getPrice()));
        }
    };


    private static final Comparator<Product> SORT_OVERALL_RATING = new Comparator<Product>() {
        @Override
        public int compare(Product product, Product t1) {
            double rating = product.calcOverallRating().equals("---") ? -1.0 : Double.valueOf(product.calcOverallRating());
            double t1rating = t1.calcOverallRating().equals("---") ? -1.0 : Double.valueOf(t1.calcOverallRating());
            return (int) (100 * (t1rating - rating));
        }
    };


    private static final Comparator<Product> SORT_SKINTONE_RATING = new Comparator<Product>() {
        @Override
        public int compare(Product product, Product t1) {
            double rating = product.calcSkintoneRating().equals("---") ? -1.0 : Double.valueOf(product.calcSkintoneRating());
            double t1rating = t1.calcSkintoneRating().equals("---") ? -1.0 : Double.valueOf(t1.calcSkintoneRating());
            return (int) (100 * (t1rating - rating));
        }
    };

    public static Comparator<Product> comparatorOf(Sort sort) {
        switch (sort) {
            case RELEVANCE:
                return Product.SORT_RELEVANCE;
            case PRICE_LOW_TO_HIGH:
                return Product.SORT_PRICE_LOW_TO_HIGH;
            case PRICE_HIGH_TO_LOW:
                return Product.SORT_PRICE_HIGH_TO_LOW;
            case OVERALL_RATING:
                return Product.SORT_OVERALL_RATING;
            case SKINTONE_RATING:
                return Product.SORT_SKINTONE_RATING;
        }
        return Product.SORT_RELEVANCE;
    }

}

