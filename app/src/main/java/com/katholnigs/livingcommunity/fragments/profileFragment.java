package com.katholnigs.livingcommunity.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.katholnigs.livingcommunity.CreateCommunityActivity;
import com.katholnigs.livingcommunity.InviteCommunityActivity;
import com.katholnigs.livingcommunity.LoginActivity;
import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //view objects
    private TextView textViewUserEmail;
    private TextView textViewUserFirstname;
    private TextView textViewUserLastname;
    private TextView textViewCommunityName;
    private Button buttonLogout;
    private Button buttonLeaveCommunity;
    private Button buttonInviteToCommunity;

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
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        fill_with_data();
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
        buttonInviteToCommunity = (Button) getView().findViewById(R.id.buttonInviteCommunity);

        //displaying logged in user name
        textViewUserEmail.setText(user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonLeaveCommunity.setOnClickListener(this);
        buttonInviteToCommunity.setOnClickListener(this);

        callAsynchronousTask();
        fill_with_data();
    }

    @Override
    public void onClick(final View view) {
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (view == buttonInviteToCommunity && buttonInviteToCommunity.getText().equals("Invite")){
            startActivity(new Intent(getActivity(), InviteCommunityActivity.class));
        } else if (view == buttonLeaveCommunity && buttonLeaveCommunity.getText().equals("Create community")){

            startActivity(new Intent(getActivity(), CreateCommunityActivity.class));

        } else if (view == buttonLeaveCommunity ||  buttonInviteToCommunity.getText().equals("Decline")) {
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

                    for (User u : user){
                        if(buttonLeaveCommunity.getText().equals("Join community") && (view == buttonLeaveCommunity)){
                            u.com_id = u.recently_invited;
                            u.recently_invited = 0;
                            buttonInviteToCommunity.setText("Invite");
                        }
                        // TODO: check if correct button was pressed
                        else if(buttonInviteToCommunity.getText().equals("Decline") && !(view == buttonLeaveCommunity)){
                            Log.v("myApp", "was declined");
                            buttonInviteToCommunity.setText("Invite");
                            u.recently_invited = 0;
                        } else{
                            u.com_id = 1;
                        }

                        sendNetworkRequest(u);

                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                    try {
                        throw t;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        }
    }

    public void fill_with_data() {

        firebaseAuth = FirebaseAuth.getInstance();


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

                    if (u.recently_invited > 0){
                        buttonLeaveCommunity.setText("Join community");
                        buttonInviteToCommunity.setText("Decline");
                        getCommunityForInvite(u);
                    }else{
                        getCommunity(u);
                    }

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

    private void getCommunity(final User u) {
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

                if (community.id == 1){
                    buttonLeaveCommunity.setText("Create community");
                    buttonInviteToCommunity.setVisibility(View.GONE);
                } else if(community.id > 1){
                    buttonLeaveCommunity.setText("Leave community");
                    buttonInviteToCommunity.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                //Toast.makeText(getActivity(), "error with community!", Toast.LENGTH_SHORT).show();
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    private void getCommunityForInvite(final User u) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Log.v("myApp", currentFirebaseUser.getUid());
        Call<Community> callCommunity = client.communityByID(u.recently_invited);

        callCommunity.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                Community community = response.body();
                //Toast.makeText(getActivity(), community.size(), Toast.LENGTH_SHORT).show();
                textViewCommunityName.setText("Invited to: " + community.name);
                buttonInviteToCommunity.setVisibility(View.VISIBLE);

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

    private void sendNetworkRequest(User user) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<Void> call = client.updateUser(user.id, user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                fill_with_data();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new AsyncTask<String, Void, String>() {
                                @Override
                                protected String doInBackground(String... strings) {
                                    fill_with_data();
                                    return null;
                                }

                            }.execute();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
    }

}