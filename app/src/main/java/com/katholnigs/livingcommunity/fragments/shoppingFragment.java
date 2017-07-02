package com.katholnigs.livingcommunity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.adapter.RecyclerViewAdapter;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.ShoppingItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class shoppingFragment extends Fragment {

    private RecyclerView recyclerView;

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

        Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        List<ShoppingItem> data = new ArrayList<>();
        data.add(new ShoppingItem("Grünkohl", sqlDate, false, 1, 3));

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(data, getActivity().getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fill_with_data();

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    public void fill_with_data() {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<List<ShoppingItem>> call = client.shoppingList(3);

        call.enqueue(new Callback<List<ShoppingItem>>() {
            @Override
            public void onResponse(Call<List<ShoppingItem>> call, Response<List<ShoppingItem>> response) {
                List<ShoppingItem> items = response.body();

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, getActivity().getApplication());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ShoppingItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "error:(", Toast.LENGTH_SHORT).show();
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        //List<ShoppingItem> data = new ArrayList<>();

        //data.add(new ShoppingItem("Grünkohl", Date., false));
        //data.add(new ShoppingItem("Apples", "Steve", false));
        //data.add(new ShoppingItem("Salami", "Marcel", false));
        //data.add(new ShoppingItem("Döner", "Konsti", false));
        //data.add(new ShoppingItem("Zigaretten", "Steve", false));
        //data.add(new ShoppingItem("Wodka", "Ricarda", false));

    }
}