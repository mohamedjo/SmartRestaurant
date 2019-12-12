package com.jo.android.smartrestaurant.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jo.android.smartrestaurant.R;


public class BestSalesViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewCatigory,textViewName,textViewCount;

    public BestSalesViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCatigory=itemView.findViewById(R.id.tv_best_category);
        textViewName=itemView.findViewById(R.id.tv_best_name);
        textViewCount=itemView.findViewById(R.id.tv_best_count);

    }
}
