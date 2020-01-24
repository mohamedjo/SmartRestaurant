package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.data.UserData;
import com.jo.android.smartrestaurant.model.ItemInCart;
import com.jo.android.smartrestaurant.viewholders.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.jo.android.smartrestaurant.MenuActivity.ITEM_ID_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.CATEGORY_TITTLE_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.RESTAURANT_ID_KEY;

public class RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecommendation;

    private PastItemsAdapter adapter;

    private List<ItemInCart> recommendedItemsList;
    private DatabaseReference userPastOrdersRef,menuRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        userPastOrdersRef= FirebaseDatabase.getInstance().getReference().child("user_orders").child(UserData.USER_ID).child(UserData.RESTAURANT_ID);
        menuRef= FirebaseDatabase.getInstance().getReference().child("menus").child(UserData.RESTAURANT_ID);
        recommendedItemsList=new ArrayList<>();
        loadPastOrders();
        recyclerViewRecommendation=findViewById(R.id.recycler_view_recommendation);
        recyclerViewRecommendation.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommendation.setHasFixedSize(true);
        adapter=new PastItemsAdapter(recommendedItemsList);







    }

    private void loadPastOrders(){
        userPastOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recommendedItemsList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ItemInCart pastItem=dataSnapshot1.getValue(ItemInCart.class);

                   if(validToAdd(recommendedItemsList,pastItem)) {
                       recommendedItemsList.add(pastItem);
                   }

                }
                recyclerViewRecommendation.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private boolean validToAdd(List<ItemInCart> recommendedItemsList, ItemInCart pastItem) {

        for(ItemInCart item:recommendedItemsList){
            if (pastItem.getCategory().equals(item.getCategory())&&pastItem.getItemId().equals(item.getItemId())){

                return false ;

            }


        }
        return true;

    }

    private class PastItemsAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        private List<ItemInCart> mRecomendedItemslist;

        public PastItemsAdapter(List<ItemInCart> mRecomendedItemslist) {
            this.mRecomendedItemslist = mRecomendedItemslist;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_item_list,parent,false);
            ItemViewHolder viewHolder=new ItemViewHolder(view);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
            final ItemInCart item=mRecomendedItemslist.get(position);
           menuRef.child(item.getCategory()).child(item.getItemId()).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       String name= dataSnapshot.child("name").getValue(String.class);
                       String description= dataSnapshot.child("description").getValue(String.class);
                       long price= dataSnapshot.child("price").getValue(Long.class);
                       holder.textViewNAme.setText(item.getCategory()+"  "+name);
                       holder.textViewDescription.setText(description);
                       holder.buttonPrice.setText("EGP "+price);


                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               sendToItemDetailsActivity(item.getItemId(),item.getCategory());
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
        public int getItemCount() {
            return mRecomendedItemslist.size();
        }
    }

    private void sendToItemDetailsActivity(String item_id,String whichPart) {

        Intent intent=new Intent(RecommendationActivity.this,ItemDetailsActivity.class);
        intent.putExtra(CATEGORY_TITTLE_KEY,whichPart);
        intent.putExtra(RESTAURANT_ID_KEY,UserData.RESTAURANT_ID);
        intent.putExtra(ITEM_ID_KEY,item_id);

        startActivity(intent);
    }
}
