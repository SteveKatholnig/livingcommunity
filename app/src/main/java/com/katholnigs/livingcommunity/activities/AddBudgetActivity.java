package com.katholnigs.livingcommunity.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.model.BudgetEntry;
import com.katholnigs.livingcommunity.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBudgetActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        ImageButton addEntry = (ImageButton) findViewById(R.id.budget_add_comfirmed);
        final EditText value = (EditText) findViewById(R.id.editTextAmount);
        //final Spinner spinner = (Spinner) findViewById(R.id.add_budget_spinner);
        final EditText itemDescription = (EditText) findViewById(R.id.editTextEntryDescription);



        OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String description = itemDescription.getText().toString();
                //final String spinnerSelection = spinner.getSelectedItem().toString();
                final String budgetValue = value.getText().toString();

                if (!TextUtils.isEmpty(description) && !TextUtils.isEmpty(budgetValue)){
                    firebaseAuth = FirebaseAuth.getInstance();
                    final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    Log.v("myApp", description);
                    Log.v("myApp", budgetValue);

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

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = df.format(new Date());
                            Log.v("myApp", date);

                            //Toast.makeText(AddItemActivity.this, "" + user.size(), Toast.LENGTH_SHORT).show();

                            for (User u : user){
                                if(u.com_id != 1){

                                    BudgetEntry entryToAdd = new BudgetEntry(budgetValue, ""+ 0, date, u.id, u.com_id, description);
                                    sendNetworkRequest(entryToAdd, u);

                                }else{
                                    finish();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            //Toast.makeText(AddItemActivity.this, "error with user!", Toast.LENGTH_SHORT).show();
                            try {
                                throw t;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddBudgetActivity.this, "Enter a entry value/description!", Toast.LENGTH_SHORT).show();
                }



            }

        };
        addEntry.setOnClickListener(buttonListener);
    }

    private void sendNetworkRequest(final BudgetEntry entryToAdd, final User user) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<Void> call = client.saveBudgetEntry(entryToAdd);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(AddItemActivity.this, "Item was added!", Toast.LENGTH_SHORT).show();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://62.75.166.253/lc.app/public/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                ApiClient client = retrofit.create(ApiClient.class);
                Call<List<User>> callUsers = client.userByComId(user.com_id);

                callUsers.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        List<User> users = response.body();

                        BudgetEntry entry = null;

                        for(User u : users){
                            if (!(u.id == user.id)){
                                Retrofit.Builder builder = new Retrofit.Builder()
                                        .baseUrl("http://62.75.166.253/lc.app/public/")
                                        .addConverterFactory(GsonConverterFactory.create());
                                Retrofit retrofit = builder.build();

                                double oweAmount = Double.parseDouble(entryToAdd.credit) / users.size();
                                Log.v("myApp", "Owe: " + oweAmount);

                                entry = new BudgetEntry(entryToAdd.owe, String.valueOf(oweAmount), entryToAdd.date, u.id, u.com_id, u.firstname + " owes to: " + user.firstname);

                                ApiClient client = retrofit.create(ApiClient.class);
                                Call<Void> callOwe = client.saveBudgetEntry(entry);

                                callOwe.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        finish();
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

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(AddItemActivity.this, "error:(", Toast.LENGTH_SHORT).show();
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
