package com.katholnigs.livingcommunity.model;

public class User {

    public int id;
    public String email;
    public String firstname;
    public String lastname;
    public int recently_invited;
    public int com_id;
    public String uid;

    public User( String email, String firstname, String lastname, int recently_invited, int com_id, String uid){
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.recently_invited = recently_invited;
        this.com_id = com_id;
        this.uid = uid;
    }

    public User(int id, String email, String firstname, String lastname, int recently_invited, int com_id, String uid){
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.recently_invited = recently_invited;
        this.com_id = com_id;
        this.uid = uid;
    }

    public String toString(){
        return this.email + " " + this.firstname + " " + this.lastname + " " + this.recently_invited + " " + this.com_id + " " + this.uid;
    }
}
