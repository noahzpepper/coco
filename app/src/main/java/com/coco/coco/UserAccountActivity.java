package com.coco.coco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.coco.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private Uri uploadedImage;
    private ImageView accountInputPicture;
    private TextView accountInputName;
    private TableLayout tableIngredients, tableCompanies;
    private Button logOutButton;

    private enum Action {
        CHANGE_NAME,
        ADD_INGREDIENT,
        ADD_COMPANY,
        REMOVE_INGREDIENT,
        REMOVE_COMPANY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        getUser();

        accountInputName = findViewById(R.id.accountInputName);
        accountInputPicture = findViewById(R.id.accountInputPicture);
        tableIngredients = findViewById(R.id.tableIngredients);
        tableCompanies = findViewById(R.id.tableCompanies);

        findViewById(R.id.accountLogOutButton).setOnClickListener(this);
        findViewById(R.id.editPictureButton).setOnClickListener(this);
        findViewById(R.id.userAccountBackButton).setOnClickListener(this);
        findViewById(R.id.editNameButton).setOnClickListener(this);
        findViewById(R.id.addIngredientButton).setOnClickListener(this);
        findViewById(R.id.addCompanyButton).setOnClickListener(this);
    }

    private TableRow createTableRowItem(String text, final Action action) {
        final TableRow tableRowItemContainer = new TableRow(this);
        final TextView textView = new TextView(this);
        final ImageView imageView = new ImageView(this);

        textView.setText(text);
        imageView.setBackgroundResource(R.drawable.removebutton);

        tableRowItemContainer.addView(imageView);
        tableRowItemContainer.addView(textView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyUser(textView.getText().toString(), action);
                tableRowItemContainer.setVisibility(View.GONE);
            }
        });

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, dpToPx(12), 0, 0);
        tableRowItemContainer.setLayoutParams(lp);

        return tableRowItemContainer;
    }

    private int dpToPx(int dp) {
        Resources r = this.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private void loadUser() {
        if (user == null) { return; }
        accountInputName.setText(user.getName());
        if (user.getBlockedIngredients() != null) {
            tableIngredients.removeAllViews();
            for (String ingredient : user.getBlockedIngredients()) {
                tableIngredients.addView(createTableRowItem(ingredient, Action.REMOVE_INGREDIENT));
            }
        }
        if (user.getBlockedCompanies() != null) {
            tableCompanies.removeAllViews();
            for (String company : user.getBlockedCompanies()) {
                tableCompanies.addView(createTableRowItem(company, Action.REMOVE_COMPANY));
            }
        }
        user.loadUserPhotoIntoImageView(accountInputPicture);
    }

    private void getUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            throw new IllegalStateException();
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                loadUser();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void setUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            throw new IllegalStateException();
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());
        userRef.setValue(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getData() == null) { return; }
            uploadedImage = data.getData();

            final String storageFilename = user.getId() + ".png";
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileRef = storageRef.child(storageFilename);
            fileRef.putFile(uploadedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            accountInputPicture.setImageURI(uploadedImage);
                            user.setPhotoUrl(storageFilename);
                            setUser();
                            loadUser();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accountLogOutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserAccountActivity.this, SplashActivity.class));
                finishAffinity();
                break;
            case R.id.editPictureButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.userAccountBackButton:
                onBackPressed();
                break;
            case R.id.editNameButton:
                showDialog("Set name:", Action.CHANGE_NAME);
                break;
            case R.id.addIngredientButton:
                showDialog("Add ingredient:", Action.ADD_INGREDIENT);
                break;
            case R.id.addCompanyButton:
                showDialog("Add company: ", Action.ADD_COMPANY);
                break;
        }
    }

    private void showDialog(String message, final Action action) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String string = input.getText().toString();
                        if (string.equals("")) { return; }
                        modifyUser(string, action);
                    }
                })
                .setMessage(message)
                .setView(input)
                .show();
    }


    private void modifyUser(String string, Action action) {
        switch (action) {
            case CHANGE_NAME:
                user.setName(string);
                break;
            case ADD_INGREDIENT:
                if (user.getBlockedIngredients() == null) {
                    user.setBlockedIngredients(new ArrayList<String>());
                }
                if (!user.getBlockedIngredients().contains(string)) {
                    user.getBlockedIngredients().add(string);
                }
                break;
            case ADD_COMPANY:
                if (user.getBlockedCompanies() == null) {
                    user.setBlockedCompanies(new ArrayList<String>());
                }
                if (!user.getBlockedCompanies().contains(string)) {
                    user.getBlockedCompanies().add(string);
                }
                break;
            case REMOVE_INGREDIENT:
                user.getBlockedIngredients().remove(string);
                break;
            case REMOVE_COMPANY:
                user.getBlockedCompanies().remove(string);
                Log.d("UHUH", user.getBlockedCompanies().toString());
                break;
        }
        setUser();
        loadUser();
    }
}
