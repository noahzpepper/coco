package com.coco.coco;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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

    private ArrayList<View> reviewInfo = new ArrayList<>();

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

        reviewInfo.add(imagesTV);
        reviewInfo.add(imageScrollView);

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
    }
}
