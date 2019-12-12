package com.jo.android.smartrestaurant;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.ManagerData;
import com.jo.android.smartrestaurant.fragments.DatePickerFragment;
import com.jo.android.smartrestaurant.model.Count;
import com.jo.android.smartrestaurant.viewholders.BestSalesViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayBestSelesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TYPE_OF_QUARY = "type_of_quary";

    private DatabaseReference daySalesRef, monthSalesRef;
    String currentDate;

    private Button buttonChoseDate;
    private TextView textViewDisplayDate;

    private RecyclerView recyclerViewBesSales;

    List<Count> itemContslist;
     String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_best_seles);
         type = getIntent().getStringExtra(TYPE_OF_QUARY);
        recyclerViewBesSales = findViewById(R.id.recycler_view_best_sales);
        recyclerViewBesSales.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBesSales.setHasFixedSize(true);
        buttonChoseDate = findViewById(R.id.button_choose_date);
        textViewDisplayDate = findViewById(R.id.tv_display_date);
        itemContslist = new ArrayList<>();
        if (type.equals("day")) {
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            textViewDisplayDate.setText(currentDate);
            buttonChoseDate.setText("Chose Day");
            loadaysSales();
        }

        if (type.equals("month")) {
            currentDate =new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date()); ;
            textViewDisplayDate.setText(currentDate);
            buttonChoseDate.setText("Chose Month");
            loadMonthsSales();
        }
        buttonChoseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             choseDate();
            }
        });

    }


    private void choseDate() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");


    }

    private void loadMonthsSales() {
        monthSalesRef = FirebaseDatabase.getInstance().getReference().child("month_sales_count").child(ManagerData.RESTAURANT_PHONE).child(currentDate);

        monthSalesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemContslist.clear();

                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String category = dataSnapshot1.getKey();

                    monthSalesRef.child(category).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.exists()) {
                                Toast.makeText(DayBestSelesActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }

                            for (DataSnapshot dataSnapshot11 : dataSnapshot.getChildren()) {
                                String itemId = dataSnapshot11.getKey();
                                long count = dataSnapshot11.child("count").getValue(Long.class);
                                Count itemCount = new Count(category, itemId, count);
                                itemContslist.add(itemCount);
                            }

                            Collections.sort(itemContslist);
                            Collections.reverse(itemContslist);
                            BestSalesAdapter adapter = new BestSalesAdapter(itemContslist);
                            recyclerViewBesSales.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadaysSales() {
        daySalesRef = FirebaseDatabase.getInstance().getReference().child("day_sales_count").child(ManagerData.RESTAURANT_PHONE).child(currentDate);

        daySalesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemContslist.clear();

                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String category = dataSnapshot1.getKey();

                    daySalesRef.child(category).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.exists()) {
                                Toast.makeText(DayBestSelesActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }

                            for (DataSnapshot dataSnapshot11 : dataSnapshot.getChildren()) {
                                String itemId = dataSnapshot11.getKey();
                                long count = dataSnapshot11.child("count").getValue(Long.class);
                                Count itemCount = new Count(category, itemId, count);
                                itemContslist.add(itemCount);
                            }

                            Collections.sort(itemContslist);
                            Collections.reverse(itemContslist);
                            BestSalesAdapter adapter = new BestSalesAdapter(itemContslist);
                            recyclerViewBesSales.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(type.equals("day")) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = format.format(c.getTime());

            currentDate = strDate;
            loadaysSales();
            textViewDisplayDate.setText(strDate);
        }
        if(type.equals("month")) {
            SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
            String strDate = format.format(c.getTime());

            currentDate = strDate;
            loadMonthsSales();
            textViewDisplayDate.setText(strDate);
        }


    }

    private class BestSalesAdapter extends RecyclerView.Adapter<BestSalesViewHolder> {
        List<Count> mItemContslist;

        public BestSalesAdapter(List<Count> mItemContslist) {
            this.mItemContslist = mItemContslist;
        }

        @NonNull
        @Override
        public BestSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.best_sales_item, parent, false);
            BestSalesViewHolder viewHolder = new BestSalesViewHolder(view);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final BestSalesViewHolder holder, int position) {

            final Count count = mItemContslist.get(position);

            FirebaseDatabase.getInstance().getReference().child("menus").child(ManagerData.RESTAURANT_PHONE).child(count.getCategory())
                    .child(count.getItemId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    holder.textViewCatigory.setText(count.getCategory());
                    holder.textViewName.setText(name);
                    holder.textViewCount.setText("" + count.getCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @Override
        public int getItemCount() {
            return mItemContslist.size();
        }
    }

    /*

     */
}
