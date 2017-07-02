package com.katholnigs.livingcommunity.api;

import com.katholnigs.livingcommunity.model.ShoppingItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    @GET("shoppingList")
    Call<List<ShoppingItem>> shoppingList();
}
