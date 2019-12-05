package com.coco.coco;

import com.coco.coco.model.Product;
import com.coco.coco.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ProductFilter implements Serializable {

    public static final ProductFilter FILTER_DEFAULT = new ProductFilter();

    private Product.Sort sort;
    private double minPrice;
    private double maxPrice;
    private boolean onlyEcoCertified;
    private ArrayList<String> blockedCompanies;
    private ArrayList<String> blockedIngredients;
    private String searchText;

    public ProductFilter() {
        this(Product.Sort.RELEVANCE, 0, 100, false);
    }

    public ProductFilter(Product.Sort sort, double minPrice, double maxPrice, boolean onlyEcoCertified) {
        this.sort = sort;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.onlyEcoCertified = onlyEcoCertified;
        getBlockedCompaniesAndIngredients();
    }

    private void getBlockedCompaniesAndIngredients() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException();
        }
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    throw new IllegalStateException();
                }
                blockedCompanies = user.getBlockedCompanies();
                blockedIngredients = user.getBlockedIngredients();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public Product.Sort getSort() {
        return sort;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public boolean isOnlyEcoCertified() {
        return onlyEcoCertified;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    // does this filter allow the product to pass through it?
    // if true, then it should be displayed
    public boolean allowsProduct(Product product) {
        double price = product.priceLowest().getPrice();
        if (price > getMaxPrice()) {
            return false;
        } else if (price < getMinPrice()) {
            return false;
        } else if (isOnlyEcoCertified() && !product.isEcoCertified()) {
            return false;
        } else if (containsCompany(product.getCompany())) {
            return false;
        } else if (containsAnyIngredient(product.getIngredients())) {
            return false;
        } else if (!matchesSearchText(product)) {
            return false;
        }
        return true;
    }

    public boolean containsCompany(String company) {
        if (blockedCompanies == null) { return false; }
        for (String blockedCompany : blockedCompanies) {
            if (company.toLowerCase().trim().equals(blockedCompany.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAnyIngredient(ArrayList<String> ingredients) {
        if (blockedIngredients == null) { return false; }
        for (String ingredient : ingredients) {
            for (String blockedIngredient : blockedIngredients) {
                if (ingredient.toLowerCase().trim().equals(blockedIngredient.toLowerCase().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matchesSearchText(Product product) {
        if (searchText == null || searchText.equals("")) {
            return true;
        }
        String string = searchText.toLowerCase().trim();
        String ingredientsString = product.getIngredients().toString().trim();
        return     product.getName().toLowerCase().trim().contains(string)
                || product.getCompany().toLowerCase().trim().startsWith(string)
                || ingredientsString.toLowerCase().trim().contains(string);
    }
}