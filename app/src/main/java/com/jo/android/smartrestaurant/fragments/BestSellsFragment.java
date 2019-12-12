package com.jo.android.smartrestaurant.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.jo.android.smartrestaurant.DayBestSelesActivity;
import com.jo.android.smartrestaurant.R;

import static com.jo.android.smartrestaurant.DayBestSelesActivity.TYPE_OF_QUARY;

/**
 * A simple {@link Fragment} subclass.
 */
public class BestSellsFragment extends Fragment {

    private CardView cardViewDays,cardViewMonths;


    public BestSellsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_best_sells, container, false);
        cardViewDays=view.findViewById(R.id.card_view_days);
        cardViewMonths=view.findViewById(R.id.card_view_months);
        cardViewDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDaysBestSalesActivity("day");
            }
        });
        cardViewMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDaysBestSalesActivity("month");
            }
        });

        return view;

    }

    private void openDaysBestSalesActivity(String type) {
        Intent intent = new Intent(getActivity(), DayBestSelesActivity.class);
        intent.putExtra(TYPE_OF_QUARY,type);
        startActivity(intent);
    }

}
