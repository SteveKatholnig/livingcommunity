package com.katholnigs.livingcommunity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCommunityActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText nameDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        ImageButton addItem = (ImageButton) findViewById(R.id.create_com_comfirmed);
        nameDescription = (EditText) findViewById(R.id.editTextItemDescription);

        OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        Toast.makeText(CreateCommunityActivity.this, "" + user.size(), Toast.LENGTH_SHORT).show();

                        for (User u : user){
                            sendNetworkRequest(u);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(CreateCommunityActivity.this, "error with user!", Toast.LENGTH_SHORT).show();
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });


            }

        };
        addItem.setOnClickListener(buttonListener);
    }

    private void sendNetworkRequest(final User user) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);

        Community community = new Community(nameDescription.getText().toString());
        Call<Community> callCommunity = client.saveCommunity(community);

        callCommunity.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://62.75.166.253/lc.app/public/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                Community comCreated = response.body();

                user.com_id = comCreated.id;

                ApiClient client = retrofit.create(ApiClient.class);
                Call<Void> callUser = client.updateUser(user.id, user);

                callUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(CreateCommunityActivity.this, "User updated was added!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CreateCommunityActivity.this, "error:(", Toast.LENGTH_SHORT).show();
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Toast.makeText(CreateCommunityActivity.this, "error:(", Toast.LENGTH_SHORT).show();
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });


    }
}
