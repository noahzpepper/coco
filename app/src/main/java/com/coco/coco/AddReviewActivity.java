package com.coco.coco;

import android.content.Intent;
import android.os.Bundle;

import com.coco.coco.model.Product;
import com.coco.coco.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;

    private TextView addReviewName;
    private EditText addReviewText;
    private ImageView[] addReviewOverallStars, addReviewSkintoneStars;

    private int overallRating;
    private int skintoneRating;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        product = (Product) getIntent().getSerializableExtra("product");

        addReviewName = findViewById(R.id.addReviewName);
        addReviewText = findViewById(R.id.addReviewText);
        addReviewOverallStars = new ImageView[]{
                findViewById(R.id.addReviewOverallStar1),
                findViewById(R.id.addReviewOverallStar2),
                findViewById(R.id.addReviewOverallStar3),
                findViewById(R.id.addReviewOverallStar4),
                findViewById(R.id.addReviewOverallStar5)
        };
        addReviewSkintoneStars = new ImageView[]{
                findViewById(R.id.addReviewSkintoneStar1),
                findViewById(R.id.addReviewSkintoneStar2),
                findViewById(R.id.addReviewSkintoneStar3),
                findViewById(R.id.addReviewSkintoneStar4),
                findViewById(R.id.addReviewSkintoneStar5)
        };

        addReviewName.setText(product.getName());
        selectOverallRating(5);
        selectSkintoneRating(5);
        imageUrls = new ArrayList<>();

        //on click listeners
        findViewById(R.id.addReviewOverallStar1).setOnClickListener(this);
        findViewById(R.id.addReviewOverallStar2).setOnClickListener(this);
        findViewById(R.id.addReviewOverallStar3).setOnClickListener(this);
        findViewById(R.id.addReviewOverallStar4).setOnClickListener(this);
        findViewById(R.id.addReviewOverallStar5).setOnClickListener(this);
        findViewById(R.id.addReviewSkintoneStar1).setOnClickListener(this);
        findViewById(R.id.addReviewSkintoneStar2).setOnClickListener(this);
        findViewById(R.id.addReviewSkintoneStar3).setOnClickListener(this);
        findViewById(R.id.addReviewSkintoneStar4).setOnClickListener(this);
        findViewById(R.id.addReviewSkintoneStar5).setOnClickListener(this);
        findViewById(R.id.addReviewSubmit).setOnClickListener(this);
        findViewById(R.id.addReviewUpload).setOnClickListener(this);
        findViewById(R.id.addReviewAccountButton).setOnClickListener(this);
        findViewById(R.id.addReviewBackButton).setOnClickListener(this);
    }

    private void selectOverallRating(int rating) {
        this.overallRating = rating;
        int i;
        for (i = 0; i < rating; i++) {
            addReviewOverallStars[i].setBackgroundResource(R.drawable.star);
        }
        for (; i < 5; i++) {
            addReviewOverallStars[i].setBackgroundResource(R.drawable.star_gray);
        }
    }

    private void selectSkintoneRating(int rating) {
        this.skintoneRating = rating;
        int i;
        for (i = 0; i < rating; i++) {
            addReviewSkintoneStars[i].setBackgroundResource(R.drawable.star);
        }
        for (; i < 5; i++) {
            addReviewSkintoneStars[i].setBackgroundResource(R.drawable.star_gray);
        }
    }

    private void submit() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException();
        }

        product.getReviews().add(new Review(
                user.getUid(),
                addReviewText.getText().toString(),
                overallRating,
                skintoneRating,
                imageUrls
        ));

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef = rootRef.child("products").child(product.getId());
        productRef.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addReviewBackButton:
                onBackPressed();
                break;
            case R.id.addReviewAccountButton:
                startActivity(new Intent(AddReviewActivity.this, UserAccountActivity.class));
                break;
            case R.id.addReviewOverallStar1:
                selectOverallRating(1);
                break;
            case R.id.addReviewOverallStar2:
                selectOverallRating(2);
                break;
            case R.id.addReviewOverallStar3:
                selectOverallRating(3);
                break;
            case R.id.addReviewOverallStar4:
                selectOverallRating(4);
                break;
            case R.id.addReviewOverallStar5:
                selectOverallRating(5);
                break;
            case R.id.addReviewSkintoneStar1:
                selectSkintoneRating(1);
                break;
            case R.id.addReviewSkintoneStar2:
                selectSkintoneRating(2);
                break;
            case R.id.addReviewSkintoneStar3:
                selectSkintoneRating(3);
                break;
            case R.id.addReviewSkintoneStar4:
                selectSkintoneRating(4);
                break;
            case R.id.addReviewSkintoneStar5:
                selectSkintoneRating(5);
                break;
            case R.id.addReviewSubmit:
                submit();
                break;
            case R.id.addReviewUpload:
                break;
        }
    }

}
