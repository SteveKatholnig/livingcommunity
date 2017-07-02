package com.katholnigs.livingcommunity.api;

import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.ShoppingItem;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    //@GET("shoppingList")
    //Call<List<ShoppingItem>> shoppingList();

    @GET("shoppingList/{com_id}")
    Call<List<ShoppingItem>> shoppingList(@Path("com_id") int com_id);

    @GET("community")
    Call<List<Community>> communityList();

    @GET("user")
    Call<List<User>> userByName();
}
