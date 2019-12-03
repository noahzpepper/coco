package com.coco.coco;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TEMPORARY CLASS -- for generating DB entries
 */
public class ProductDatabaseUtil implements Runnable {

    public void run() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String id = ref.push().getKey();
        Product product = createRandomProduct(id);
        ref.child("products").child(id).setValue(product);
    }

    private static Product createRandomProduct(String id) {

        ArrayList<String> imageUrls = new ArrayList<>(Arrays.asList("nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg"));
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(
                "Water", "Tocopherol", "Dimethicone", "Parabens", "Titanium dioxide"));

        //prices
        Map<String, Price> prices = new HashMap<>();
        double price = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        double price2 = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        double price3 = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        prices.put("price1", new Price("target_logo.png", String.format(Locale.US, "$%.2f In-Store", price), price, "https://www.target.com/"));
        prices.put("price2", new Price("walmart_logo.png", String.format(Locale.US, "$%.2f + Free Shipping", price2), price2, "https://www.walmart.com/"));
        prices.put("price3", new Price("amazon_logo.png", String.format(Locale.US, "$%.2f w/Amazon Prime", price3), price3, "https://www.amazon.com/"));


        double rating = Math.round(100.0 * (1.0 + Math.random() * 4)) / 100.0;
        double rating2 = Math.round(100.0 * (1.0 + Math.random() * 4)) / 100.0;

        String category = MainViewActivity.CATEGORIES[(int) (Math.random() * MainViewActivity.CATEGORIES.length)];
        return new Product(
                id,
                category,
                "Chanel",
                "This product really works!",
                "placeholder_image.png",
                imageUrls,
                ingredients,
                Math.random() > .5,
                String.format(Locale.US, "Product %.4f (%s)", Math.random(), category),
                rating,
                rating2,
                null,
                prices);
    }

//    private static Product createProduct(String id) {
//
//        //images
//        ArrayList<String> imageUrls = null; // new ArrayList<>(Arrays.asList("nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg"));
//
//        //prices
//        Map<String, Price> prices = new HashMap<>();
//
//        double price = 0.0;
//        double price2 = 0.0;
//        double price3 = 0.0;
//
//        prices.put("price1", new Price("target_logo.png", String.format(Locale.US, "$%.2f In-Store", price), price));
//        prices.put("price2", new Price("walmart_logo.png", String.format(Locale.US, "$%.2f + Free Shipping", price2), price2));
//        prices.put("price3", new Price("amazon_logo.png", String.format(Locale.US, "$%.2f w/Amazon Prime", price3), price3));
//
//
//        double rating = 1.0;
//        double rating2 = 1.0;
//        String category = "face";
//
//        return new Product(
//                id,
//                category,
//                company,
//                description,
//                "image.png",
//                imageUrls,
//                null,
//                Math.random() > .5,
//                String.format(Locale.US, "Product %.4f (%s)", Math.random(), category),
//                rating,
//                rating2,
//                null,
//                prices);
//    }
}
