package com.jo.android.smartrestaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.smartrestaurant.fragments.TablesFragment;
import com.jo.android.smartrestaurant.model.OrderItem;
import com.jo.android.smartrestaurant.viewholders.OrderItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TableDetailsActivity extends AppCompatActivity {

   private TextView textViewTableNumber,textViewCustomerName,textViewTotalAmount;
   private RecyclerView recyclerViewOrders;
    private DatabaseReference tablesRef,ordersRef,menuRef;
    private List<OrderItem> orderItemList;

    public int i;
    private long tableNumber;
    private  OrdersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table_details);
        tableNumber=getIntent().getLongExtra(TablesFragment.TABLE_ID_KEY,0);
        tablesRef = FirebaseDatabase.getInstance().getReference().child("tables").child("0123456789");
        ordersRef=FirebaseDatabase.getInstance().getReference().child("tables").child("0123456789").child(""+tableNumber).child("orders");

        menuRef=FirebaseDatabase.getInstance().getReference().child("menus").child("0123456789");
        orderItemList=new ArrayList<>();
        textViewTableNumber=findViewById(R.id.tv_table_number_details);
        textViewCustomerName=findViewById(R.id.tv_customer_name);
        recyclerViewOrders=findViewById(R.id.recycler_view_orders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setHasFixedSize(true);
        textViewTableNumber.setText(""+tableNumber);
        loaduCustomerName();

        loadOrdersData();




       /* Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<ItemInCart> options= new FirebaseRecyclerOptions.Builder<ItemInCart>()
                .setQuery(ordersRef, ItemInCart.class)
                .build();

        FirebaseRecyclerAdapter<ItemInCart, OrderItemViewHolder> adapter=
        new FirebaseRecyclerAdapter<ItemInCart, OrderItemViewHolder>(options) {

            @NonNull
            @Override
            public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.table_order_list_item,parent,false);
                OrderItemViewHolder viewHolder=new OrderItemViewHolder(view);
                return viewHolder;

            }

            @Override
            protected void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position, @NonNull ItemInCart model) {

               String name =loadItemName(model.getCategory(),model.getItemId());
                holder.textViewQuantity.setText(model.getQuantity()+"");
                holder.textViewCategory.setText(model.getCategory());
                holder.textViewName.setText(name);



            }
        };
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
*/
       // recyclerViewOrders.setAdapter(adapter);
    }

    private void loadOrdersData(){

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderItemList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren() ){
                    String category=dataSnapshot1.child("category").getValue(String.class);

                    String itemId=dataSnapshot1.child("itemId").getValue(String.class);
                    long quantity=dataSnapshot1.child("quantity").getValue(Long.class);


                    OrderItem orderItem=new OrderItem(itemId,category,quantity);

                    orderItemList.add(orderItem);



                }
                adapter=new OrdersAdapter(orderItemList);
                recyclerViewOrders.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }



    private void loaduCustomerName(){

        tablesRef.child(""+tableNumber).child("user_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String customer_id= dataSnapshot.getValue(String.class);
               FirebaseDatabase.getInstance().getReference().child("users").child(customer_id).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      String firstName =dataSnapshot.child("firstName").getValue(String.class);
                       String lastName =dataSnapshot.child("lastName").getValue(String.class);

                       textViewCustomerName.setText(firstName+" "+lastName);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class OrdersAdapter extends RecyclerView.Adapter<OrderItemViewHolder>{

        List<OrderItem> mOrderItemList;

        public OrdersAdapter(List<OrderItem> orderItemList) {
            this.mOrderItemList = orderItemList;
        }

        @NonNull
        @Override
        public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.table_order_list_item,parent,false);
            OrderItemViewHolder viewHolder=new OrderItemViewHolder(view);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, int position) {


            final OrderItem orderItem= mOrderItemList.get(position);
            menuRef.child(orderItem.getCategory()).child(orderItem.getItemId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name=dataSnapshot.child("name").getValue(String.class);
                    holder.textViewQuantity.setText(orderItem.getQuantity()+"");
                    holder.textViewCategory.setText(orderItem.getCategory());
                    holder.textViewName.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }

        @Override
        public int getItemCount() {
            return mOrderItemList.size();
        }
    }
}
