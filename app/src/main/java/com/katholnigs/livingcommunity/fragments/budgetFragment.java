package com.katholnigs.livingcommunity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katholnigs.livingcommunity.R;

public class budgetFragment extends Fragment {
    public static budgetFragment newInstance() {
        budgetFragment fragment = new budgetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }
}