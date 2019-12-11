package com.coco.coco;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coco.coco.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerFragment extends Fragment {

    private List<Product> databaseProductList = new ArrayList<>();
    private List<Product> shownProductList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private static final String TAG = "RECYCLER";

    private String category;
    private ProductFilter filter;

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
        mAdapter = new ProductAdapter(shownProductList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getDataFromDatabase();
        return rootView;
    }

    public void getDataFromDatabase() {
        DatabaseReference productRoot = FirebaseDatabase.getInstance().getReference("products");
        productRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseProductList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Product product = childSnapshot.getValue(Product.class);
                    if (product != null && checkProductCategory(product.getCategory())) {
                        databaseProductList.add(product);
                    }
                }
                updateShownProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void updateShownProducts() {
        if (filter == null) {
            filter = ProductFilter.FILTER_DEFAULT;
        }
        shownProductList.clear();
        for (Product p : databaseProductList) {
            if (filter.allowsProduct(p)) {
                shownProductList.add(p);
            }
        }
        Collections.sort(shownProductList, Product.comparatorOf(filter.getSort()));
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkProductCategory(String productCategory) {
        return productCategory.toLowerCase().equals(category.toLowerCase()) ||
                category.toLowerCase().equals("all");
    }

    public void setFilter(ProductFilter filter) {
        this.filter = filter;
    }
}
