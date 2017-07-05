package com.katholnigs.livingcommunity.api;

import com.katholnigs.livingcommunity.model.BudgetEntry;
import com.katholnigs.livingcommunity.model.Community;
import com.katholnigs.livingcommunity.model.ShoppingItem;
import com.katholnigs.livingcommunity.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("shoppingList/{id}")
    Call<List<ShoppingItem>> shoppingList(@Path("id") int id);

    @POST("saveShoppingList")
    Call<Void> saveShoppingList(@Body ShoppingItem item);

    @PUT("updateShoppingList/{id}")
    Call<Void> updateShoppingList(@Path("id") int id, @Body ShoppingItem item);

    @GET("community/{id}")
    Call<Community> communityByID(@Path("id")int id);

    @POST("saveCommunity")
    Call<Community> saveCommunity(@Body Community com);

    //@PUT("updateCommunity/{id}")
    //Call<Void> updateCommunity(@Path("id") int id, @Body Community community);

    @GET("user/{id}")
    Call<List<User>> userByUID(@Path("id") String id);

    @GET("userByCommunity/{id}")
    Call<List<User>> userByComId(@Path("id") int id);

    @GET("userByEmail")
    Call<List<User>> userByEmail(@Query("email") String email);

    @POST("saveUser/{id}")
    Call<Void> saveUser(@Path("id") String id, @Body User user);

    @PUT("updateUser/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body User user);

    @GET("budget/{id}")
    Call<List<BudgetEntry>> budgetList(@Path("id") int id);

    @POST("saveBudget")
    Call<Void> saveBudgetEntry(@Body BudgetEntry entry);

    @PUT("updateBudget/{id}")
    Call<Void> updateBudget(@Path("id") int id, @Body BudgetEntry entry);

    @DELETE("deleteBudget/{id}")
    Call<Void> deleteBudgetEntry(@Path("id") int id);

}
