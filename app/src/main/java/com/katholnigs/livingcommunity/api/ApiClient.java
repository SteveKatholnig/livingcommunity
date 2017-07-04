package com.katholnigs.livingcommunity.api;

import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.ShoppingItem;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiClient {

 //   @GET("shoppingList")
 //   Call<List<ShoppingItem>> shoppingList();

    @GET("shoppingList/{id}")
    Call<List<ShoppingItem>> shoppingList(@Path("id") int id);

    @POST("saveShoppingList")
    Call<Void> saveShoppingList(@Body ShoppingItem item);

    @PUT("updateShoppingList/{id}")
    Call<Void> updateShoppingList(@Path("id") int id, @Body ShoppingItem item);

    @GET("community/{id}")
    Call<Community> communityByID(@Path("id")int id);

    @GET("user/{id}")
    Call<List<User>> userByUID(@Path("id") String id);
}
