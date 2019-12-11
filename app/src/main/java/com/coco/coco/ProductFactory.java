package com.coco.coco;

import android.os.Bundle;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.coco.coco.model.Review;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TEMPORARY CLASS -- for generating DB entries
 * currently runs at end of {@link MainViewActivity#onCreate(Bundle)}
 */
public class ProductFactory implements Runnable {

    public void run() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String id = ref.push().getKey();
        Product product = createProduct(id);
        ref.child("products").child(id).setValue(product);
    }

    private static Product createProduct(String id) {

        // ***************************************************************** //

        // The name of the product.
        String name = "";

        // The product category.
        // Must be one of: "All", "Face", "Eyes", "Lips", "Brows", "Skincare"
        // If it doesn't fit into any category, use "All".
        String category = "";

        // The company.
        String company = "";

        // The description.
        String description = "";

        // The product image. Upload the image of the product at the following link:
        // https://console.firebase.google.com/u/1/project/coco-c2b4c/storage/coco-c2b4c.appspot.com/files
        // Then enter the exact title of the image you uploaded here.
        // Images must be .png or .jpg.
        String productImage = "";

        // The list of ingredients.
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(
                "", ""
        ));

        // Is it Eco Certified?
        boolean ecoCertified = false;

        // The price information. At least one price is required.
        String targetUrl = "";
        double targetPrice = 0.0;
        String amazonUrl = "";
        double amazonPrice = 0.0;
        String walmartUrl = "";
        double walmartPrice = 0.0;

        // ***************************************************************** //




        //prices
        Map<String, Price> prices = new HashMap<>();
        if (targetPrice > 0 && notNullOrEmpty(targetUrl)) {
            prices.put("target", new Price("target_logo.png", String.format(Locale.US, "$%.2f at Target", targetPrice), targetPrice, targetUrl));
        }
        if (walmartPrice > 0 && notNullOrEmpty(walmartUrl)) {
            prices.put("walmart", new Price("walmart_logo.png", String.format(Locale.US, "$%.2f at Walmart", walmartPrice), walmartPrice, walmartUrl));
        }
        if (amazonPrice > 0 && notNullOrEmpty(amazonUrl)) {
            prices.put("amazon", new Price("amazon_logo.png", String.format(Locale.US, "$%.2f at Amazon", amazonPrice), amazonPrice, amazonUrl));
        }

        //verifications
        assertTrue(notNullOrEmpty(name));
        assertTrue(notNullOrEmpty(category));
        assertTrue(new ArrayList<>(Arrays.asList(MainViewActivity.CATEGORIES)).contains(category), "category invalid");
        assertTrue(notNullOrEmpty(company));
        assertTrue(notNullOrEmpty(description));
        assertTrue(notNullOrEmpty(productImage));
        assertTrue(productImage.contains(".png") || productImage.contains(".jpg"), "productImage must be .jpg or .png");
        assertTrue(prices.size() > 0, "at least one price needed");

        return new Product(id, category, company, description, productImage, ingredients,
                ecoCertified, name, new ArrayList<Review>(), prices);
    }

    private static void assertTrue(boolean val) {
        if (!val) {
            throw new AssertionError("value missing");
        }
    }

    private static void assertTrue(boolean val, String message) {
        if (!val) {
            throw new AssertionError(message);
        }
    }

    private static boolean notNullOrEmpty(String s) {
        return s != null && !s.equals("");
    }

    private static Product createRandomProduct(String id) {

        ArrayList<String> imageUrls = new ArrayList<>(Arrays.asList("nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg", "nyx_eyeshadow.jpg"));
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(
                "Water", "Tocopherol", "Dimethicone", "Parabens", "Titanium dioxide"));

        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(new Review("DjNTeadNL0U3EXn9oDzuqPF1b9h2", "This is the review part!", 5, 4, null));
        reviews.add(new Review("DjNTeadNL0U3EXn9oDzuqPF1b9h2", "This is the review part!", 5, 4, new ArrayList<>(Arrays.asList("nyx_eyeshadow.jpg"))));

        //prices
        Map<String, Price> prices = new HashMap<>();
        double price = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        double price2 = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        double price3 = Math.round(100.0 * (Math.random() * 100)) / 100.0;
        prices.put("price1", new Price("target_logo.png", String.format(Locale.US, "$%.2f In-Store", price), price, "https://www.target.com/"));
        prices.put("price2", new Price("walmart_logo.png", String.format(Locale.US, "$%.2f + Free Shipping", price2), price2, "https://www.walmart.com/"));
        prices.put("price3", new Price("amazon_logo.png", String.format(Locale.US, "$%.2f w/Amazon Prime", price3), price3, "https://www.amazon.com/"));


        String category = MainViewActivity.CATEGORIES[(int) (Math.random() * MainViewActivity.CATEGORIES.length)];
        return new Product(
                id,
                category,
                "Chanel",
                "This product really works!",
                "placeholder_image.png",
                ingredients,
                Math.random() > .5,
                String.format(Locale.US, "Product %.4f (%s)", Math.random(), category),
                reviews,
                prices);
    }
}
