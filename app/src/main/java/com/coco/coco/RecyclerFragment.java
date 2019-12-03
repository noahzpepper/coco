package com.coco.coco;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private static final String TAG = "RECYCLER";

    private String category;

    public RecyclerFragment(String category) {
        this.category = category;
    }

    public static Fragment newInstance(String category) {
        return new RecyclerFragment(category);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        mAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareProductData();
        return rootView;

    }

    private void prepareProductData() {
        DatabaseReference productRoot = FirebaseDatabase.getInstance().getReference("products");
        productRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Product product = childSnapshot.getValue(Product.class);
                    if (product != null && checkProductCategory(product.getCategory())) {
                        productList.add(product);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean checkProductCategory(String productCategory) {
        return productCategory.toLowerCase().equals(category.toLowerCase()) ||
                category.toLowerCase().equals("all");
    }

}
