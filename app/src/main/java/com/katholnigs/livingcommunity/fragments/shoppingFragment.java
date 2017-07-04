package com.katholnigs.livingcommunity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.katholnigs.livingcommunity.AddItemActivity;
import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.adapter.RecyclerViewAdapter;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.ShoppingItem;
import com.katholnigs.livingcommunity.model.User;

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
    private FirebaseAuth firebaseAuth;

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
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        fill_with_data();
    }


    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        ImageButton addItem = (ImageButton) getView().findViewById(R.id.shopping_add);

        OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddItemActivity.class));
            }

        };
        addItem.setOnClickListener(buttonListener);

        Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        List<ShoppingItem> data = new ArrayList<>();
        data.add(new ShoppingItem("Grünkohl", sqlDate, 0, 1, 3));

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

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Log.v("myApp", currentFirebaseUser.getUid());
        Call<List<User>> call = client.userByUID(currentFirebaseUser.getUid());

        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                List<User> user = response.body();

                Toast.makeText(getActivity(), "" + user.size(), Toast.LENGTH_SHORT).show();

                for (User u : user){

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd hh:mm:ss")
                            .create();

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("http://62.75.166.253/lc.app/public/")
                            .addConverterFactory(GsonConverterFactory.create(gson));
                    Retrofit retrofit = builder.build();

                    ApiClient client = retrofit.create(ApiClient.class);
                    Call<List<ShoppingItem>> callList = client.shoppingList(u.com_id);

                    callList.enqueue(new Callback<List<ShoppingItem>>() {
                        @Override
                        public void onResponse(Call<List<ShoppingItem>> call, Response<List<ShoppingItem>> response) {
                            List<ShoppingItem> items = response.body();

                            SystemClock.sleep(100);
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
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), "error with user!", Toast.LENGTH_SHORT).show();
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