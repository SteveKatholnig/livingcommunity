package com.katholnigs.livingcommunity.model;

public class Community {

    public int id;
    public String name;

    public Community(String name){
        this.name = name;
    }

    public Community(int id, String name){
        this.id = id;
        this.name = name;
    }
}
