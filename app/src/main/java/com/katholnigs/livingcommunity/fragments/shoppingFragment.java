package com.katholnigs.livingcommunity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.adapter.RecyclerViewAdapter;
import com.katholnigs.livingcommunity.model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class shoppingFragment extends Fragment {
    public static shoppingFragment newInstance() {
        shoppingFragment fragment = new shoppingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        List<ShoppingItem> data = fill_with_data();

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(data, getActivity().getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    public List<ShoppingItem> fill_with_data() {

        List<ShoppingItem> data = new ArrayList<>();

        data.add(new ShoppingItem("Grünkohl", "Steve", false));
        data.add(new ShoppingItem("Apples", "Steve", false));
        data.add(new ShoppingItem("Salami", "Marcel", false));
        data.add(new ShoppingItem("Döner", "Konsti", false));
        data.add(new ShoppingItem("Zigaretten", "Steve", false));
        data.add(new ShoppingItem("Wodka", "Ricarda", false));

        return data;
    }
}