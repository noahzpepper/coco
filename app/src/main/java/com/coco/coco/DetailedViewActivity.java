package com.coco.coco;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;

public class DetailedViewActivity extends AppCompatActivity {

    private Button descriptionButton;
    private boolean showDescription = false;
    private TextView descriptionTV;

    private Button ingredientsButton;
    private boolean showIngredients = false;
    private TextView ingredientsTV;

    private Button reviewInfoButton;
    private boolean showReviewInfo = false;
    private TextView imagesTV;
    private HorizontalScrollView imageScrollView;

    private  TextView reviewContentTV;
    private  ImageView reviewerPic;
    private  TextView username1;
    private TextView rating3;
    private TextView rating5;
    private TextView rating6;
    private TextView rating7;
    private Button addReviewButton;
    private TextView reviewsTV;
    private ImageView star;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;

    private ArrayList<View> reviewInfo = new ArrayList<>();

    private ImageButton backButton;
    private ImageButton accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        // Find the description Button and description TextView
        descriptionButton = (Button) findViewById(R.id.description_button);
        descriptionTV = (TextView) findViewById(R.id.description);

        // Find the ingredient Button and ingredient TextView
        ingredientsButton = (Button) findViewById(R.id.ingredients_button);
        ingredientsTV = (TextView) findViewById(R.id.ingredients);

        // Find the review info Button and review info TextView
        reviewInfoButton = (Button) findViewById(R.id.review_button);

        imagesTV = (TextView) findViewById(R.id.images_text);
        imageScrollView = (HorizontalScrollView) findViewById(R.id.images_scroll);
        reviewContentTV = (TextView) findViewById(R.id.review_content);
        reviewerPic = (ImageView) findViewById(R.id.reviewer_pic);
        username1 = (TextView) findViewById(R.id.user1);
        rating3 = (TextView) findViewById(R.id.rating3);
        rating5 = (TextView) findViewById(R.id.rating5);
        rating6 = (TextView) findViewById(R.id.rating6);
        rating7 = (TextView) findViewById(R.id.rating7);
        addReviewButton = (Button) findViewById(R.id.add_review_button);
        reviewsTV = (TextView) findViewById(R.id.reviews_text);
        star = (ImageView) findViewById(R.id.star);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        reviewInfo.add(imagesTV);
        reviewInfo.add(imageScrollView);
        reviewInfo.add(reviewContentTV);
        reviewInfo.add(reviewerPic);
        reviewInfo.add(username1);
        reviewInfo.add(rating3);
        reviewInfo.add(rating5);
        reviewInfo.add(rating6);
        reviewInfo.add(rating7);
        reviewInfo.add(addReviewButton);
        reviewInfo.add(reviewsTV);
        reviewInfo.add(star);
        reviewInfo.add(star3);
        reviewInfo.add(star4);
        reviewInfo.add(star5);

        backButton = (ImageButton) findViewById(R.id.back_button);
        accountButton = (ImageButton) findViewById(R.id.account_button);

        // Press description button to drop down product description
        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescription = !showDescription;
                if (showDescription) {
                    descriptionTV.setVisibility(View.VISIBLE);
                } else {
                    descriptionTV.setVisibility(View.GONE);
                }
            }
        }); //closing the setOnClickListener method

        // Press ingredients button to drop down ingredients list
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredients = !showIngredients;
                if (showIngredients) {
                    ingredientsTV.setVisibility(View.VISIBLE);
                } else {
                    ingredientsTV.setVisibility(View.GONE);
                }
            }
        }); //closing the setOnClickListener method

        // Press ingredients button to drop down ingredients list
        reviewInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewInfo = !showReviewInfo;
                if (showReviewInfo) {
                    for (View view : reviewInfo) {
                        view.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (View view : reviewInfo) {
                        view.setVisibility(View.GONE);
                    }
                }
            }
        }); //closing the setOnClickListener method

        findViewById(R.id.detailedViewBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Press add review button to start add review activity
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailedViewActivity.this, AddReviewActivity.class));
            }
        }); //closing the setOnClickListener method

        // Press back  button to finish this activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //closing the setOnClickListener method

        // Press account  button to start account activity
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailedViewActivity.this, UserAccountActivity.class));
            }
        }); //closing the setOnClickListener method
    }
}
