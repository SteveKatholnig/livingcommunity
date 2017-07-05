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
import com.katholnigs.livingcommunity.model.ShoppingItem;
import com.katholnigs.livingcommunity.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddItemActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ImageButton addItem = (ImageButton) findViewById(R.id.shopping_add_comfirmed);

        OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText itemDescription = (EditText) findViewById(R.id.editTextItemDescription);
                final String description = itemDescription.getText().toString();

                if (!TextUtils.isEmpty(description)){
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

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = df.format(new Date());
                            Log.v("myApp", date);

                            //Toast.makeText(AddItemActivity.this, "" + user.size(), Toast.LENGTH_SHORT).show();

                            for (User u : user){
                                if(u.com_id != 1){
                                    ShoppingItem itemToAdd = new ShoppingItem(description, date, 0, u.id, u.com_id);
                                    sendNetworkRequest(itemToAdd);
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
                    Toast.makeText(AddItemActivity.this, "Enter a item description!", Toast.LENGTH_SHORT).show();
                }



            }

        };
        addItem.setOnClickListener(buttonListener);
    }

    private void sendNetworkRequest(ShoppingItem itemToAdd) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://62.75.166.253/lc.app/public/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<Void> call = client.saveShoppingList(itemToAdd);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(AddItemActivity.this, "Item was added!", Toast.LENGTH_SHORT).show();
                finish();
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
