package com.coco.coco;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailedViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REVIEW_CODE = 1;
    private Product product;

    private TextView descriptionTV, ingredientsTV, imagesTV, reviewText, reviewOverallRating, reviewSkintoneRating;
    private ImageView detailProductImage;
    private ImageView detailSellerImage;
    private ImageView detailSellerImage1;
    private ImageView detailSellerImage2;
    private ImageView detailSellerImage3;
    private ImageView detailEcoCertified;
    private ImageView detailSellerContainer1;
    private ImageView detailSellerContainer2;
    private ImageView detailSellerContainer3;
    private ImageView reviewOverallStar;
    private ImageView reviewSkintoneStar;
    private TextView detailProductName, detailProductCompanyText, detailRating, detailSkintoneMatch, detailPrice,
            detailSellerText1, detailSellerText2, detailSellerText3;
    private HorizontalScrollView imageScrollView;
    private Button detailAddReviewButton;
    private ArrayList<View> reviewInfo;
    private LinearLayout shapeLayout;

    private RecyclerView reviewRecycler;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        //init views
        detailProductImage = findViewById(R.id.detailProductImage);
        detailSellerImage = findViewById(R.id.detailSellerImage);
        detailSellerImage1 = findViewById(R.id.detailSellerImage1);
        detailSellerImage2 = findViewById(R.id.detailSellerImage2);
        detailSellerImage3 = findViewById(R.id.detailSellerImage3);
        detailSellerContainer1 = findViewById(R.id.detailSellerContainer1);
        detailSellerContainer2 = findViewById(R.id.detailSellerContainer2);
        detailSellerContainer3 = findViewById(R.id.detailSellerContainer3);
        detailProductName = findViewById(R.id.detailProductName);
        detailProductCompanyText = findViewById(R.id.detailProductCompanyText);
        detailRating = findViewById(R.id.detailRating);
        detailPrice = findViewById(R.id.detailPrice);
        detailSkintoneMatch = findViewById(R.id.detailSkintoneMatch);
        detailEcoCertified = findViewById(R.id.detailEcoCertified);
        detailSellerText1 = findViewById(R.id.detailSellerText1);
        detailSellerText2 = findViewById(R.id.detailSellerText2);
        detailSellerText3 = findViewById(R.id.detailSellerText3);
        ingredientsTV = findViewById(R.id.ingredients);
        descriptionTV = findViewById(R.id.description);
        imagesTV = findViewById(R.id.images_text);
        imageScrollView = findViewById(R.id.images_scroll);
        shapeLayout = findViewById(R.id.shapeLayout);
        detailAddReviewButton = findViewById(R.id.detailAddReviewButton);
        reviewText = findViewById(R.id.detailReviewText);
        reviewSkintoneRating = findViewById(R.id.reviewSkintoneRating);
        reviewOverallRating = findViewById(R.id.reviewOverallRating);
        reviewOverallStar = findViewById(R.id.reviewOverallStar);
        reviewSkintoneStar = findViewById(R.id.reviewSkintoneStar);
        reviewRecycler = findViewById(R.id.reviewRecycler);
        reviewInfo = createReviewInfoList();

        //setup review recyclerview
        reviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reviewAdapter = new ReviewAdapter();
        reviewRecycler.setAdapter(reviewAdapter);

        //on click listeners
        findViewById(R.id.review_button).setOnClickListener(this);
        findViewById(R.id.ingredients_button).setOnClickListener(this);
        findViewById(R.id.description_button).setOnClickListener(this);
        findViewById(R.id.detailAccountButton).setOnClickListener(this);
        findViewById(R.id.detailAddReviewButton).setOnClickListener(this);
        findViewById(R.id.detailedViewBackButton).setOnClickListener(this);

        //product
        product = (Product) getIntent().getSerializableExtra("product");
        loadProduct();
        loadReviews();
    }

    private ArrayList<View> createReviewInfoList() {
        return new ArrayList<>(Arrays.asList(
                imagesTV, imageScrollView, reviewOverallRating, detailAddReviewButton, reviewText,
                reviewSkintoneRating, reviewOverallStar, reviewSkintoneStar, reviewRecycler
        ));
    }

    private void loadProduct() {

        detailEcoCertified.setVisibility(product.isEcoCertified() ? View.VISIBLE : View.INVISIBLE);
        product.loadProductImageIntoImageView(detailProductImage);
        product.priceLowest().loadLogoIntoImageView(detailSellerImage);
        detailProductName.setText(product.getName());
        detailProductCompanyText.setText(String.format(Locale.US, "by %s", product.getCompany()));
        detailPrice.setText(String.format(Locale.US, "$%.2f", product.priceLowest().getPrice()));
        detailRating.setText(String.format(Locale.US, "%s     overall", product.calcOverallRating()));
        detailSkintoneMatch.setText(String.format(Locale.US, "%s     skin tone match", product.calcSkintoneRating()));
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
        int numImages = product.gatherCommunityImageUrls().size();
        ImageView[] detailCommunityImages = new ImageView[numImages];
        for (int j = 0; j < detailCommunityImages.length; j++) {
            detailCommunityImages[j] = new ImageView(this);
            int size = Utils.dpToPx(this, 100);
            detailCommunityImages[j].setLayoutParams(new LinearLayout.LayoutParams(size, size));
            shapeLayout.addView(detailCommunityImages[j]);
        }
        product.loadCommunityImagesIntoImageViews(detailCommunityImages);
    }

    private void loadReviews() {
        int numReviews = (product.getReviews() == null) ? 0 : product.getReviews().size();
        reviewText.setText(String.format(Locale.US, "Reviews (%d)", numReviews));
        reviewOverallRating.setText(String.format(Locale.US, "%s     overall", product.calcOverallRating()));
        reviewSkintoneRating.setText(String.format(Locale.US, "%s     skin tone match", product.calcSkintoneRating()));
        reviewAdapter.setData(product.getReviews());

        //has user reviewed this product already? if so, hide review button
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) { throw new IllegalStateException(); }
        if (product.reviewedByUser(user.getUid())) {
            detailAddReviewButton.setEnabled(false);
            detailAddReviewButton.setAlpha(0);
        }
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
                for (View v : reviewInfo) {
                    toggleVisibility(v);
                }
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
            case R.id.detailAddReviewButton:
                Intent intent = new Intent(DetailedViewActivity.this, AddReviewActivity.class);
                intent.putExtra("product", product);
                startActivityForResult(intent, REVIEW_CODE);
                break;
            case R.id.detailedViewBackButton:
                onBackPressed();
                break;
        }
    }

    //when the user submits a review, return them here to refresh the reviews on the product page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REVIEW_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("YESYES", "NO");
                DatabaseReference productRoot = FirebaseDatabase.getInstance().getReference("products");
                productRoot.child(product.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        product = dataSnapshot.getValue(Product.class);
                        loadProduct();
                        loadReviews();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        }
    }

}
