package com.jo.android.smartrestaurant.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.AddOfferActivity;
import com.jo.android.smartrestaurant.R;
import com.jo.android.smartrestaurant.data.ManagerData;
import com.jo.android.smartrestaurant.model.OfferItem;
import com.jo.android.smartrestaurant.viewholders.ItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageOffersFragment extends Fragment {
    RecyclerView recyclerViewOffers;
    private Button buttonOPenAddOffer;
    private DatabaseReference offersRef;

    public ManageOffersFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_manage_offers, container, false);
        recyclerViewOffers=view.findViewById(R.id.rv_offers);
        buttonOPenAddOffer =view.findViewById(R.id.btn_open_add_offer);
        recyclerViewOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewOffers.setHasFixedSize(true);
        offersRef= FirebaseDatabase.getInstance().getReference().child("menus").child(ManagerData.RESTAURANT_PHONE).child("offers");

        FirebaseRecyclerOptions<OfferItem> options= new FirebaseRecyclerOptions.Builder<OfferItem>()
                .setQuery(offersRef, OfferItem.class)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<OfferItem, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(getContext()).inflate(R.layout.menu_item_list,parent,false);
                ItemViewHolder viewHolder=new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull OfferItem model) {

                holder.textViewNAme.setText(model.getName());
                holder.textViewDescription.setText(model.getDescription());
                holder.buttonPrice.setText("EGP "+model.getPrice());



            }
        };
        recyclerViewOffers.setAdapter(adapter);
        adapter.startListening();




        buttonOPenAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddOffer();
            }
        });

        return view;
    }

    private void openAddOffer() {
        Intent intent=new Intent(getActivity(), AddOfferActivity.class);

        startActivity(intent);
    }

}
