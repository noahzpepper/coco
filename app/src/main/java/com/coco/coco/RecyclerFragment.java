package com.coco.coco;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private static final String TAG = "RECYCLER";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        mAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareProductData();

        return rootView;

    }

    private void prepareProductData() {
        Product product = new Product();
        productList.add(product);
        productList.add(product);
        productList.add(product);
        productList.add(product);
        productList.add(product);
        productList.add(product);
        mAdapter.notifyDataSetChanged();
        }
    }


