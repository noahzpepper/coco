package com.coco.coco;

public class Product {

    private String title, overall_rating, skintone_match;


    public Product() {
    }


    public Product(String title, String overall_rating, String skintone_match) {
        this.title = title;
        this.overall_rating = overall_rating;
        this.skintone_match = skintone_match;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(String overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getSkintone_match() {
        return skintone_match;
    }

    public void setSkintone_match(String skintone_match) {
        this.skintone_match = skintone_match;
    }


}

