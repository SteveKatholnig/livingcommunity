package com.katholnigs.livingcommunity.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.katholnigs.livingcommunity.R;

public class BudgetListViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView description;
    ImageButton budgetEntryDelete;

    public BudgetListViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        budgetEntryDelete = (ImageButton) itemView.findViewById(R.id.budget_delete);
    }
}