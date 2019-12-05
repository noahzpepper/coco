package com.coco.coco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static com.coco.coco.model.Product.Sort;

public class UserFilterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private RadioGroup filterSortGroup;
    private CheckBox filterEcoCertified;
    private SeekBar filterMinPriceSeekbar, filterMaxPriceSeekbar;
    private TextView filterMinPriceText, filterMaxPriceText;

    private Sort sort = Sort.RELEVANCE;
    private double minPrice = 0.0;
    private double maxPrice = 100.0;
    private boolean onlyEcoCertified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_filter);

        filterSortGroup = findViewById(R.id.filterSortGroup);
        filterEcoCertified = findViewById(R.id.filterEcoCertified);
        filterMinPriceSeekbar = findViewById(R.id.filterMinPriceSeekBar);
        filterMaxPriceSeekbar = findViewById(R.id.filterMaxPriceSeekBar);
        filterMinPriceText = findViewById(R.id.filterMinPriceText);
        filterMaxPriceText = findViewById(R.id.filterMaxPriceText);

        //listeners
        filterSortGroup.setOnCheckedChangeListener(this);
        filterEcoCertified.setOnCheckedChangeListener(this);
        filterMinPriceSeekbar.setOnSeekBarChangeListener(this);
        filterMaxPriceSeekbar.setOnSeekBarChangeListener(this);
        findViewById(R.id.filterSaveButton).setOnClickListener(this);
        findViewById(R.id.userFilterBackButton).setOnClickListener(this);

        ProductFilter filter = (ProductFilter) getIntent().getSerializableExtra("filter");
        if (filter == null) {
            filter = ProductFilter.FILTER_DEFAULT;
        }
        load(filter);
    }

    private void load(ProductFilter filter) {
        filterSortGroup.check(getSortId(filter.getSort()));
        filterMinPriceSeekbar.setProgress((int) (filter.getMinPrice() * 100));
        filterMaxPriceSeekbar.setProgress((int) (filter.getMaxPrice() * 100));
        filterMinPriceText.setText(String.format(Locale.US, "Min: $%.2f", filter.getMinPrice()));
        filterMaxPriceText.setText(String.format(Locale.US, "Max: $%.2f", filter.getMaxPrice()));
        filterEcoCertified.setChecked(filter.isOnlyEcoCertified());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void submitFilter() {
        ProductFilter filter = new ProductFilter(sort, minPrice, maxPrice, onlyEcoCertified);
        Intent intent = new Intent(UserFilterActivity.this, MainViewActivity.class);
        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
        finish();
    }

    //general clicks
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userFilterBackButton:
                onBackPressed();
                break;
            case R.id.filterSaveButton:
                submitFilter();
                break;
        }

    }

    //changing radio group button
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        if (radioGroup.getId() == R.id.filterSortGroup) {
            switch (id) {
                case -1:
                case R.id.filterSortRelevance:
                    sort = Sort.RELEVANCE;
                    break;
                case R.id.filterSortPriceLowToHigh:
                    sort = Sort.PRICE_LOW_TO_HIGH;
                    break;
                case R.id.filterSortPriceHighToLow:
                    sort = Sort.PRICE_HIGH_TO_LOW;
                    break;
                case R.id.filterSortOverallRating:
                    sort = Sort.OVERALL_RATING;
                    break;
                case R.id.filterSortSkintoneRating:
                    sort = Sort.SKINTONE_RATING;
                    break;
            }
        }
    }

    //ecocertified
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.filterEcoCertified) {
            onlyEcoCertified = compoundButton.isChecked();
        }
    }

    //seekbars
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.filterMinPriceSeekBar:
                minPrice = filterMinPriceSeekbar.getProgress() / 100.0;
                filterMinPriceText.setText(String.format(Locale.US, "Min: $%.2f", minPrice));
                break;
            case R.id.filterMaxPriceSeekBar:
                maxPrice = filterMaxPriceSeekbar.getProgress() / 100.0;
                filterMaxPriceText.setText(String.format(Locale.US, "Max: $%.2f", maxPrice));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    private int getSortId(Sort sort) {
        switch (sort) {
            case PRICE_LOW_TO_HIGH:
                return R.id.filterSortPriceLowToHigh;
            case PRICE_HIGH_TO_LOW:
                return R.id.filterSortPriceHighToLow;
            case OVERALL_RATING:
                return R.id.filterSortOverallRating;
            case SKINTONE_RATING:
                return R.id.filterSortSkintoneRating;
            default: //relevance
                return R.id.filterSortRelevance;
        }
    }
}
