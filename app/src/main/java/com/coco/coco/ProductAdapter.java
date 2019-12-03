package com.coco.coco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coco.coco.model.Product;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;

    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        final Product product = productList.get(i);

        holder.mainProductRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedViewActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });

        holder.cardName.setText(product.getName());
        holder.cardEcoCertifiedImage.setVisibility(product.isEcoCertified() ? View.VISIBLE : View.GONE);
        holder.cardOverallRating.setText(String.valueOf(product.getOverallRating()));
        holder.cardSkintoneRating.setText(String.valueOf(product.getSkinToneRating()));
        holder.cardPrice.setText(String.format(Locale.US, "$%.2f", product.priceLowest().getPrice()));
        product.priceLowest().loadLogoIntoImageView(holder.cardSellerImage);
        product.loadProductImageIntoImageView(holder.cardImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mainProductRoot;
        ImageView cardImage, cardSellerImage, cardEcoCertifiedImage;
        TextView cardName, cardOverallRating, cardSkintoneRating, cardPrice;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainProductRoot = itemView.findViewById(R.id.mainProductRoot);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardSellerImage = itemView.findViewById(R.id.cardSellerImage);
            cardEcoCertifiedImage = itemView.findViewById(R.id.cardEcoCertifiedImage);
            cardName = itemView.findViewById(R.id.cardName);
            cardOverallRating = itemView.findViewById(R.id.cardOverallRating);
            cardSkintoneRating = itemView.findViewById(R.id.cardSkintoneRating);
            cardPrice = itemView.findViewById(R.id.cardPrice);
        }
    }
}
