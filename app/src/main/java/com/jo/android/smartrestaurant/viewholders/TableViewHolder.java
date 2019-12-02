package com.jo.android.smartrestaurant.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jo.android.smartrestaurant.R;

public class TableViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewTableNumber;

    public TableViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTableNumber=itemView.findViewById(R.id.tv_table_number);


    }
}
