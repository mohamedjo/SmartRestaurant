package com.jo.android.smartrestaurant.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.R;
import com.jo.android.smartrestaurant.data.ManagerData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageOffersFragment extends Fragment {
    RecyclerView recyclerViewOffers;

    private DatabaseReference offersRef;

    public ManageOffersFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_manage_offers, container, false);
        recyclerViewOffers=view.findViewById(R.id.rv_offers);
        recyclerViewOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewOffers.setHasFixedSize(true);
        offersRef= FirebaseDatabase.getInstance().getReference().child("menus").child(ManagerData.RESTAURANT_PHONE).child("offers");






        return view;
    }

}
