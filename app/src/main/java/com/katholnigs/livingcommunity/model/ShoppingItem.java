package com.katholnigs.livingcommunity.model;


import java.util.Date;

public class ShoppingItem {
    public int id;
    public String description;
    public String date;
    public int done;
    public int com_id;
    public int user_id;

    public ShoppingItem(String description, String date, int done, int user_id, int com_id){
        this.description = description;
        this.date = date;
        this.done = done;
        this.user_id = user_id;
        this.com_id = com_id;
    }

}
