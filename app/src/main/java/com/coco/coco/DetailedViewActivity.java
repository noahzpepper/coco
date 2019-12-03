package com.coco.coco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class DetailedViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;

    private Button descriptionButton, reviewInfoButton, ingredientsButton;
    private TextView descriptionTV, ingredientsTV, imagesTV;
    private ImageView detailProductImage, detailSellerImage, detailSellerImage1, detailSellerImage2,
            detailSellerImage3, detailEcoCertified, detailSellerContainer1, detailSellerContainer2,
            detailSellerContainer3;
    private ImageView detailCommunityImage1, detailCommunityImage2, detailCommunityImage3,
            detailCommunityImage4, detailCommunityImage5;
    private TextView detailProductName, detailRating, detailSkintoneMatch, detailPrice,
            detailSellerText1, detailSellerText2, detailSellerText3;
    private HorizontalScrollView imageScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        product = (Product) getIntent().getSerializableExtra("product");

        detailProductImage = findViewById(R.id.detailProductImage);
        detailSellerImage = findViewById(R.id.detailSellerImage);
        detailSellerImage1 = findViewById(R.id.detailSellerImage1);
        detailSellerImage2 = findViewById(R.id.detailSellerImage2);
        detailSellerImage3 = findViewById(R.id.detailSellerImage3);
        detailCommunityImage1 = findViewById(R.id.detailCommunityImage1);
        detailCommunityImage2 = findViewById(R.id.detailCommunityImage2);
        detailCommunityImage3 = findViewById(R.id.detailCommunityImage3);
        detailCommunityImage4 = findViewById(R.id.detailCommunityImage4);
        detailCommunityImage5 = findViewById(R.id.detailCommunityImage5);
        detailSellerContainer1 = findViewById(R.id.detailSellerContainer1);
        detailSellerContainer2 = findViewById(R.id.detailSellerContainer2);
        detailSellerContainer3 = findViewById(R.id.detailSellerContainer3);
        detailProductName = findViewById(R.id.detailProductName);
        detailRating = findViewById(R.id.detailRating);
        detailPrice = findViewById(R.id.detailPrice);
        detailSkintoneMatch = findViewById(R.id.detailSkintoneMatch);
        detailEcoCertified = findViewById(R.id.detailEcoCertified);
        detailSellerText1 = findViewById(R.id.detailSellerText1);
        detailSellerText2 = findViewById(R.id.detailSellerText2);
        detailSellerText3 = findViewById(R.id.detailSellerText3);
        descriptionButton = findViewById(R.id.description_button);
        ingredientsButton = findViewById(R.id.ingredients_button);
        reviewInfoButton = findViewById(R.id.review_button);
        ingredientsTV = findViewById(R.id.ingredients);
        descriptionTV = findViewById(R.id.description);
        imagesTV = findViewById(R.id.images_text);
        imageScrollView = findViewById(R.id.images_scroll);

        descriptionButton.setOnClickListener(this);
        ingredientsButton.setOnClickListener(this);
        reviewInfoButton.setOnClickListener(this);
        findViewById(R.id.detailAccountButton).setOnClickListener(this);
        findViewById(R.id.detailedViewBackButton).setOnClickListener(this);

        loadProduct();
    }

    private void loadProduct() {

        detailEcoCertified.setVisibility(product.isEcoCertified() ? View.VISIBLE : View.INVISIBLE);
        product.loadProductImageIntoImageView(detailProductImage);
        product.priceLowest().loadLogoIntoImageView(detailSellerImage);
        detailProductName.setText(product.getName());
        detailPrice.setText(String.format(Locale.US, "$%.2f", product.priceLowest().getPrice()));
        detailRating.setText(String.format(Locale.US, "%.2f     overall", product.getOverallRating()));
        detailSkintoneMatch.setText(String.format(Locale.US, "%.2f     skin tone match", product.getSkinToneRating()));
        descriptionTV.setText(product.getDescription());

        //ingredients list
        if (product.getIngredients() != null) {
            StringBuilder sb = new StringBuilder();
            for (String ingredient : product.getIngredients()) {
                sb.append(ingredient).append(", ");
            }
            ingredientsTV.setText(sb.toString().substring(0, sb.lastIndexOf(",")));
        }

        //prices
        ImageView[] detailSellerContainers = new ImageView[]{detailSellerContainer1, detailSellerContainer2, detailSellerContainer3};
        TextView[] detailSellerTexts = new TextView[]{detailSellerText1, detailSellerText2, detailSellerText3};
        ImageView[] detailSellerImages = new ImageView[]{detailSellerImage1, detailSellerImage2, detailSellerImage3};
        int i = 0;
        for (String key : product.getPrices().keySet()) {
            final Price price = product.getPrices().get(key);
            if (price != null) {
                detailSellerTexts[i].setText(price.getTitle());
                price.loadLogoIntoImageView(detailSellerImages[i]);
                detailSellerContainers[i].setVisibility(View.VISIBLE);
                detailSellerContainers[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(price.getUrlString()));
                        startActivity(intent);
                    }
                });
                i++;
            }
        }

        //community images
        ImageView[] detailCommunityImages = new ImageView[]{detailCommunityImage1, detailCommunityImage2,
                detailCommunityImage3, detailCommunityImage4, detailCommunityImage5};
        product.loadCommunityImagesIntoImageViews(detailCommunityImages);
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.review_button:
                toggleVisibility(imagesTV);
                toggleVisibility(imageScrollView);
                break;
            case R.id.ingredients_button:
                toggleVisibility(ingredientsTV);
                break;
            case R.id.description_button:
                toggleVisibility(descriptionTV);
                break;
            case R.id.detailAccountButton:
                startActivity(new Intent(DetailedViewActivity.this, UserAccountActivity.class));
                break;
            case R.id.detailedViewBackButton:
                onBackPressed();
                break;
        }
    }
}
