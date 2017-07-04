package com.katholnigs.livingcommunity.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.katholnigs.livingcommunity.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView description;
    CheckBox itemCheckBox;

    public ViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        itemCheckBox = (CheckBox) itemView.findViewById(R.id.itemCheckBox);
    }
}