package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.model.Item;
import com.jo.android.smartrestaurant.viewholders.ItemViewHolder;

import static com.jo.android.smartrestaurant.UserHomeActivity.CATEGORY_TITTLE_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.RESTAURANT_ID_KEY;


public class MenuActivity extends AppCompatActivity {

    public static final String ITEM_ID_KEY = "item_id";

    private RecyclerView recyclerViewItems;

    private DatabaseReference itemReference,salesCountRef;
    private TextView textViewCategoryTittle;
    private String whichPart,restaurant_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent=getIntent();
         whichPart=intent.getStringExtra(CATEGORY_TITTLE_KEY);

         restaurant_id=intent.getStringExtra(RESTAURANT_ID_KEY);
        Toast.makeText(this, whichPart, Toast.LENGTH_SHORT).show();
        recyclerViewItems=findViewById(R.id.recycler_view_iten);
        textViewCategoryTittle=findViewById(R.id.tv_category_tittle);
        textViewCategoryTittle.setText(whichPart);

        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));


        itemReference= FirebaseDatabase.getInstance().getReference().child("menus").child(restaurant_id)
                .child(whichPart);

        FirebaseRecyclerOptions<Item> options= new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemReference,Item.class)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_item_list,parent,false);
                ItemViewHolder viewHolder=new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull Item model) {

                holder.textViewNAme.setText(model.getName());
                holder.textViewDescription.setText(model.getDescription());
                holder.buttonPrice.setText("EGP "+model.getPrice());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       String item_id= getRef(position).getKey();
                        sendToItemDetailsActivity(item_id);
                    }
                });


            }
        };
        recyclerViewItems.setAdapter(adapter);
        adapter.startListening();

    }

    private void sendToItemDetailsActivity(String item_id) {

        Intent intent=new Intent(MenuActivity.this,ItemDetailsActivity.class);
        intent.putExtra(CATEGORY_TITTLE_KEY,whichPart);
        intent.putExtra(RESTAURANT_ID_KEY,restaurant_id);
        intent.putExtra(ITEM_ID_KEY,item_id);




        startActivity(intent);
    }
}
