package com.katholnigs.livingcommunity.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.katholnigs.livingcommunity.R;
import com.katholnigs.livingcommunity.api.ApiClient;
import com.katholnigs.livingcommunity.fragments.shoppingFragment;
import com.katholnigs.livingcommunity.model.ShoppingItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<ShoppingItem> list = Collections.emptyList();
    private Context context;
    private shoppingFragment fragment;

    public RecyclerViewAdapter(List<ShoppingItem> list, Context context, shoppingFragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_row_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).description);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(list.get(position).date);
            SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss");
            String dateAdded = df2.format(date);
            holder.description.setText(dateAdded);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).done = 1;
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://62.75.166.253/lc.app/public/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                ApiClient client = retrofit.create(ApiClient.class);
                Log.v("myApp", "" + list.get(position).id);
                Call<Void> call = client.updateShoppingList(list.get(position).id, list.get(position));

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.v("myApp", "Item update successfull");
                        fragment.fill_with_data();
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
        });
        //holder.imageView.setImageResource(list.get(position).imageId);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, ShoppingItem data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(ShoppingItem data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}