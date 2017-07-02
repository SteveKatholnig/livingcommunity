package com.katholnigs.livingcommunity.model;

import java.sql.Date;

public class BudgetEntry {
    public int id;
    public double credit;
    public double owe;
    public Date date;
    public int user_id;
    public int com_id;

    public BudgetEntry(int id, double credit, double owe, Date date, int user_id, int com_id){
        this.id = id;
        this.credit = credit;
        this.owe = owe;
        this.date = date;
        this.user_id = user_id;
        this.com_id = com_id;
    }
}
