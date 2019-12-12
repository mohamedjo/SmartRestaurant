package com.jo.android.smartrestaurant.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.R;
import com.jo.android.smartrestaurant.TableDetailsActivity;
import com.jo.android.smartrestaurant.data.ManagerData;
import com.jo.android.smartrestaurant.viewholders.TableViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TablesFragment extends Fragment {
    public static final String TABLE_ID_KEY="table_id";

    private List<Long> tablesNumbersList;


    private DatabaseReference tablesRef;


    private RecyclerView recyclerViewTables;

    public TablesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tables, container, false);
        recyclerViewTables = view.findViewById(R.id.recycler_view_tables);
        recyclerViewTables.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTables.setHasFixedSize(true);

        tablesRef = FirebaseDatabase.getInstance().getReference().child("tables").child(ManagerData.RESTAURANT_PHONE);
        tablesNumbersList = new ArrayList<>();
        loadTablesNumbersHaveOrders();


        return view;
    }

    private void loadTablesNumbersHaveOrders() {

        tablesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    tablesNumbersList.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (dataSnapshot1.exists()) {

                            long tableNum = dataSnapshot1.child("table_num").getValue(Long.class);

                            tablesNumbersList.add(tableNum);
                        }
                    }
                    TableAdapter adapter = new TableAdapter(tablesNumbersList);
                    recyclerViewTables.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {


        List<Long> mTableNumbersList;

        public TableAdapter(List<Long> tableNumbersName) {
            this.mTableNumbersList = tableNumbersName;
        }

        @NonNull
        @Override
        public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.tables_list_item, parent, false);
            TableViewHolder tableViewHolder = new TableViewHolder(view);
            return tableViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TableViewHolder holder, final int position) {
            Long tableNumber = mTableNumbersList.get(position);

            holder.textViewTableNumber.setText("" + tableNumber);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTableDetailsActivity(mTableNumbersList.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return tablesNumbersList.size();
        }
    }

    private void openTableDetailsActivity(long idkey) {

        Intent intent = new Intent(getActivity(), TableDetailsActivity.class);
        intent.putExtra(TABLE_ID_KEY,idkey);
        startActivity(intent);
    }


}
