package com.jo.android.smartrestaurant.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jo.android.smartrestaurant.R;

public class CartItemViewHolder  extends RecyclerView.ViewHolder {
    public TextView textViewItemName,textViewItemDescription,textViewQuantity,textViewItemPrice,textViewTotalPrice;
    public Button buttonIncrease,buttonDecrease;
    public CartItemViewHolder(@NonNull View itemView) {


        super(itemView);

        textViewItemName=itemView.findViewById(R.id.tv_item_name_in_cart);
        textViewItemDescription=itemView.findViewById(R.id.tv_item_description_in_cart);
        textViewQuantity=itemView.findViewById(R.id.tv_quantity_in_cart);
        textViewItemPrice=itemView.findViewById(R.id.tv_item_price_in_cart);
        textViewTotalPrice=itemView.findViewById(R.id.tv_total_price_in_cart);
        buttonIncrease=itemView.findViewById(R.id.button_increase_in_cart);
        buttonDecrease=itemView.findViewById(R.id.button_decrease_in_cart);

    }
}
