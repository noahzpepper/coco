package com.coco.coco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.coco.coco.model.Product;
import com.coco.coco.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;

    private ArrayList<Uri> uploadedImages = new ArrayList<>();
    private TextView addReviewName, addReviewUploadText;
    private EditText addReviewText;
    private ImageView[] addReviewOverallStars, addReviewSkintoneStars;

    private String uid;
    private Review review;

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
        addReviewUploadText = findViewById(R.id.addReviewUploadText);
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
        setUploadText(0);
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException();
        }
        uid = user.getUid();
    }

    private void setUploadText(int numUploaded) {
        addReviewUploadText.setText(String.format(Locale.US, "%d Uploaded Image%s",
                numUploaded, (numUploaded == 1) ? "" : "s"));
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
        if (addReviewText.getText().toString().equals("")) {
            Toast.makeText(this, "Review can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.addReviewSubmit).setEnabled(false);
        findViewById(R.id.addReviewUpload).setEnabled(false);

        review = new Review(
                uid,
                addReviewText.getText().toString(),
                overallRating,
                skintoneRating,
                imageUrls
        );
        product.getReviews().add(review);
        uploadImages(0);
    }

    private void uploadImages(final int num) {
        if (uploadedImages == null || uploadedImages.isEmpty()) {
            uploadReview();
            return;
        }
        Toast.makeText(this, "Uploading image " + (num+1), Toast.LENGTH_SHORT).show();
        Uri uri = uploadedImages.remove(0);
        final String storageFilename = uid + num + ".png";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child(storageFilename);
        fileRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        review.getImageUrls().add(storageFilename);
                        uploadImages(num + 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload image(s)", Toast.LENGTH_SHORT).show();
                        uploadReview();
                    }
                });
    }

    private void uploadReview() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef = rootRef.child("products").child(product.getId());
        productRef.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddReviewActivity.this, "Failed to create review", Toast.LENGTH_SHORT).show();
                findViewById(R.id.addReviewSubmit).setEnabled(true);
                findViewById(R.id.addReviewUpload).setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getData() == null) { return; }
            uploadedImages.add(data.getData());
            setUploadText(uploadedImages.size());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
        }
    }

}
