package com.katholnigs.livingcommunity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.activities.AddBudgetActivity;
import com.katholnigs.livingcommunity.adapter.BudgetListRecyclerViewAdapter;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.BudgetEntry;
import com.katholnigs.livingcommunity.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BudgetFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;

    public static BudgetFragment newInstance() {
        return new BudgetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        fill_with_data();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton addBudget = (ImageButton) getView().findViewById(R.id.budget_add);
        OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddBudgetActivity.class));
            }

        };
        addBudget.setOnClickListener(buttonListener);

        List<BudgetEntry> data = new ArrayList<>();

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        BudgetListRecyclerViewAdapter adapter = new BudgetListRecyclerViewAdapter(data, getActivity().getApplication(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        //callAsynchronousTask();
        fill_with_data();

    }

    public void fill_with_data() {
        try{
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
                public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {

                    List<User> user = response.body();

                    //Toast.makeText(getActivity(), "" + user.size(), Toast.LENGTH_SHORT).show();

                    for (User u : user){

                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl("http://62.75.166.253/lc.app/public/")
                                .addConverterFactory(GsonConverterFactory.create());
                        Retrofit retrofit = builder.build();

                        ApiClient client = retrofit.create(ApiClient.class);
                        Call<List<BudgetEntry>> callList = client.budgetList(u.com_id);

                        callList.enqueue(new Callback<List<BudgetEntry>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<BudgetEntry>> call, @NonNull Response<List<BudgetEntry>> response) {
                                List<BudgetEntry> items = response.body();

                                try{
                                    BudgetListRecyclerViewAdapter adapter = new BudgetListRecyclerViewAdapter(items, getActivity().getApplication(), BudgetFragment.this);
                                    recyclerView.setAdapter(adapter);
                                } catch(Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<BudgetEntry>> call, @NonNull Throwable t) {
                                //Toast.makeText(getActivity(), "error:(", Toast.LENGTH_SHORT).show();
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
                public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                    //Toast.makeText(getActivity(), "error with user!", Toast.LENGTH_SHORT).show();
                    try {
                        throw t;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}