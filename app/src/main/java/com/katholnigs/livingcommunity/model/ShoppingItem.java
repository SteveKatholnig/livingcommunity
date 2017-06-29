package com.katholnigs.livingcommunity.model;


public class ShoppingItem {
    public String description;
    public  String createdBy;
    public boolean done;
    public boolean isSelected = false;

    public ShoppingItem(String description, String createdBy, boolean done){
        this.description = description;
        this.createdBy = createdBy;
        this.done = done;
    }

}
