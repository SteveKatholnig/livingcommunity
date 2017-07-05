package com.katholnigs.livingcommunity.model;

public class BudgetEntry {
    public int id;
    public String credit;
    public String owe;
    public String date;
    public int user_id;
    public int com_id;
    public String description;

    public BudgetEntry(String credit, String owe, String date, int user_id, int com_id, String description){
        this.credit = credit;
        this.owe = owe;
        this.date = date;
        this.user_id = user_id;
        this.com_id = com_id;
        this.description = description;
    }

    public BudgetEntry(int id, String credit, String owe, String date, int user_id, int com_id, String description){
        this.id = id;
        this.credit = credit;
        this.owe = owe;
        this.date = date;
        this.user_id = user_id;
        this.com_id = com_id;
        this.description = description;
    }
}
