package com.jo.android.smartrestaurant.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jo.android.smartrestaurant.R;

public class OrderItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewQuantity,textViewCategory,textViewName;

    public OrderItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewQuantity=itemView.findViewById(R.id.tv_order_quantity);
        textViewCategory=itemView.findViewById(R.id.tv_order_category);
        textViewName=itemView.findViewById(R.id.tv_order_name);

    }
}
