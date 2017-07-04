package com.katholnigs.livingcommunity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.katholnigs.livingcommunity.LoginActivity;
import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profileFragment extends Fragment implements View.OnClickListener {
    public static profileFragment newInstance() {
        profileFragment fragment = new profileFragment();
        return fragment;
    }

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private TextView textViewUserFirstname;
    private TextView textViewUserLastname;
    private TextView textViewCommunityName;
    private Button buttonLogout;
    private Button buttonLeaveCommunity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) getView().findViewById(R.id.textViewUserEmail);
        textViewUserFirstname = (TextView) getView().findViewById(R.id.textViewFirstname);
        textViewUserLastname = (TextView) getView().findViewById(R.id.textViewLastname);
        textViewCommunityName = (TextView) getView().findViewById(R.id.community_name);
        buttonLogout = (Button) getView().findViewById(R.id.buttonLogout);
        buttonLeaveCommunity = (Button) getView().findViewById(R.id.buttonLeaveCommunity);

        //displaying logged in user name
        textViewUserEmail.setText(user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonLeaveCommunity.setOnClickListener(this);

        fill_with_data();
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (view == buttonLeaveCommunity) {

        }
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

                for (User u : user) {

                    textViewUserFirstname.setText(u.firstname);
                    textViewUserLastname.setText(u.lastname);

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("http://62.75.166.253/lc.app/public/")
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit = builder.build();

                    ApiClient client = retrofit.create(ApiClient.class);
                    Log.v("myApp", currentFirebaseUser.getUid());
                    Call<Community> callCommunity = client.communityByID(u.com_id);

                    callCommunity.enqueue(new Callback<Community>() {
                        @Override
                        public void onResponse(Call<Community> call, Response<Community> response) {
                            Community community = response.body();
                            //Toast.makeText(getActivity(), community.size(), Toast.LENGTH_SHORT).show();
                            textViewCommunityName.setText(community.name);
                        }

                        @Override
                        public void onFailure(Call<Community> call, Throwable t) {
                            Toast.makeText(getActivity(), "error with community!", Toast.LENGTH_SHORT).show();
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

    }

}