package com.jo.android.smartrestaurant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.jo.android.smartrestaurant.data.ManagerData;
import com.jo.android.smartrestaurant.fragments.TablesFragment;
import com.jo.android.smartrestaurant.model.OrderItem;
import com.jo.android.smartrestaurant.viewholders.OrderItemViewHolder;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TableDetailsActivity extends AppCompatActivity {


    private static final int WRITE_EXTRINALE_CODE = 123;
    private TextView textViewTableNumber, textViewCustomerName, textViewTotalAmount;
    private RecyclerView recyclerViewOrders;
    private DatabaseReference tablesRef, ordersRef, menuRef, daySalesCountRef, monthSalesCountRef, restaurantOrdersRef;
    private List<OrderItem> orderItemList;
    private Button buttonPrint, buttonCheckout;
    private String customerOrders = "";
    private long subtotal;
    private long totalAmount;

    public int i;
    private long tableNumber;
    private OrdersAdapter adapter;
    private String customerName;
    private String orderWithPrice = "";


    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentMonth = new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table_details);
        tableNumber = getIntent().getLongExtra(TablesFragment.TABLE_ID_KEY, 0);
        tablesRef = FirebaseDatabase.getInstance().getReference().child("tables").child(ManagerData.RESTAURANT_PHONE);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("tables").child(ManagerData.RESTAURANT_PHONE).child("" + tableNumber).child("orders");
        daySalesCountRef = FirebaseDatabase.getInstance().getReference().child("day_sales_count").child(ManagerData.RESTAURANT_PHONE)
                .child(currentDate);
        monthSalesCountRef = FirebaseDatabase.getInstance().getReference().child("month_sales_count").child(ManagerData.RESTAURANT_PHONE)
                .child(currentMonth);
        restaurantOrdersRef = FirebaseDatabase.getInstance().getReference().child("restaurant_orders").child(ManagerData.RESTAURANT_PHONE);

        buttonPrint = findViewById(R.id.button_print);
        buttonCheckout = findViewById(R.id.button_manager_checkout);
        menuRef = FirebaseDatabase.getInstance().getReference().child("menus").child("0123456789");
        orderItemList = new ArrayList<>();
        textViewTableNumber = findViewById(R.id.tv_table_number_details);
        textViewCustomerName = findViewById(R.id.tv_customer_name);
        recyclerViewOrders = findViewById(R.id.recycler_view_orders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setHasFixedSize(true);
        textViewTableNumber.setText("" + tableNumber);
        loaduCustomerName();
        loadOrdersData();


        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf(customerName, tableNumber, customerOrders);

            }
        });
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdfCheck();
                checkOut();
            }
        });
    }

    private void checkOut() {
        increaseDayCount();
        increaseMonthCount();
        storeDate();
        deleteTable();




    }

    private void deleteTable() {
        tablesRef.child(""+tableNumber).removeValue();
        finish();

    }

    private void storeDate() {

        for (OrderItem item : orderItemList) {

            String id = restaurantOrdersRef.push().getKey();
            restaurantOrdersRef.child(id).setValue(item);


        }



    }


    private void increaseMonthCount() {
        for (final OrderItem orderItem : orderItemList) {
            monthSalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("count")) {
                        long count = dataSnapshot.child("count").getValue(Long.class);
                        count = count + 1;
                        monthSalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).child("count")
                                .setValue(count);


                    } else {
                        monthSalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).child("count")
                                .setValue(1);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void increaseDayCount() {
        for (final OrderItem orderItem : orderItemList) {
            daySalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("count")) {
                        long count = dataSnapshot.child("count").getValue(Long.class);
                        count = count + 1;
                        daySalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).child("count")
                                .setValue(count);


                    } else {
                        daySalesCountRef.child(orderItem.getCategory()).child(orderItem.getItemId()).child("count")
                                .setValue(1);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private void createPdfCheck() {

        Document mDoc = new Document();

        String fileName = "check_" + customerName + "_table" + tableNumber;
        String filePath = Environment.getExternalStorageDirectory() + "/" + fileName + ".pdf";
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(filePath));
            mDoc.open();
            mDoc.addAuthor(ManagerData.MANAGER_NAME);

            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 32,
                    Font.ITALIC, BaseColor.RED);
            Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 32,
                    Font.BOLD, BaseColor.BLACK);
            Font smallRedFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
                    Font.ITALIC, BaseColor.RED);
            Font smallBlackFont = new Font(Font.FontFamily.TIMES_ROMAN, 25,
                    Font.ITALIC, BaseColor.BLACK);
            Phrase phrase =
                    new Phrase("                                 created by @Easy Order ", smallRedFont);
            mDoc.add(new Paragraph("                    Restaurant", redFont));

            mDoc.add(new Paragraph("Customer Name: " + customerName, redFont));
            mDoc.add(new Paragraph("Table Number: " + tableNumber, redFont));
            mDoc.add(new Paragraph("Date: " + currentDate, redFont));
            mDoc.add(new Paragraph("Time: " + currentTime + "\nList of Orders:", redFont));

            mDoc.add(new Paragraph(orderWithPrice, smallBlackFont));
            mDoc.add(new Paragraph("Subtotal: " + subtotal + " EGP", smallBlackFont));
            mDoc.add(new Paragraph("service charge : " + subtotal * .1 + " EGP", smallBlackFont));
            mDoc.add(new Paragraph("total amount: " + (subtotal + subtotal * .1) + " EGP\n\n\n", blackFont));

            mDoc.add(phrase);

            mDoc.close();
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void createPdf(String customerName, long tableNumber, String customerOrders) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                savePdf(customerName, tableNumber, customerOrders);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTRINALE_CODE);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            savePdf(customerName, tableNumber, customerOrders);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTRINALE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePdf(customerName, tableNumber, customerOrders);
                } else {
                    Toast.makeText(this, "permissions denied....!", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private void savePdf(String customerName, long tableNumber, String customerOrders) {
        Document mDoc = new Document();

        String fileName = customerName + "_table" + tableNumber;
        String filePath = Environment.getExternalStorageDirectory() + "/" + fileName + ".pdf";
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(filePath));
            mDoc.open();
            mDoc.addAuthor(ManagerData.MANAGER_NAME);

            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 32,
                    Font.ITALIC, BaseColor.RED);
            Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 32,
                    Font.ITALIC, BaseColor.BLACK);
            Font smallRedFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
                    Font.ITALIC, BaseColor.RED);
            Phrase phrase =
                    new Phrase("                                 created by @Easy Order ", smallRedFont);

            mDoc.add(new Paragraph("Customer Name: " + customerName, redFont));
            mDoc.add(new Paragraph("Table Number: " + tableNumber, redFont));
            mDoc.add(new Paragraph("Order Time: " + currentTime + "\nList of Orders:", redFont));

            mDoc.add(new Paragraph(customerOrders + "\n\n\n\n", blackFont));


            mDoc.add(phrase);

            mDoc.close();
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    private void loadOrdersData() {

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    orderItemList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String category = dataSnapshot1.child("category").getValue(String.class);

                        String itemId = dataSnapshot1.child("itemId").getValue(String.class);
                        long quantity = dataSnapshot1.child("quantity").getValue(Long.class);


                        OrderItem orderItem = new OrderItem(itemId, category, quantity);

                        orderItemList.add(orderItem);


                    }
                    adapter = new OrdersAdapter(orderItemList);
                    recyclerViewOrders.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }


    private void loaduCustomerName() {

        tablesRef.child("" + tableNumber).child("user_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String customer_id = dataSnapshot.getValue(String.class);
                    FirebaseDatabase.getInstance().getReference().child("users").child(customer_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String lastName = dataSnapshot.child("lastName").getValue(String.class);
                            customerName = firstName + " " + lastName;

                            textViewCustomerName.setText(firstName + " " + lastName);
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

    private class OrdersAdapter extends RecyclerView.Adapter<OrderItemViewHolder> {

        List<OrderItem> mOrderItemList;

        public OrdersAdapter(List<OrderItem> orderItemList) {
            this.mOrderItemList = orderItemList;
        }

        @NonNull
        @Override
        public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.table_order_list_item, parent, false);
            OrderItemViewHolder viewHolder = new OrderItemViewHolder(view);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, int position) {


            final OrderItem orderItem = mOrderItemList.get(position);
            menuRef.child(orderItem.getCategory()).child(orderItem.getItemId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    long price = dataSnapshot.child("price").getValue(Long.class);

                    holder.textViewQuantity.setText(orderItem.getQuantity() + "");
                    holder.textViewCategory.setText(orderItem.getCategory());
                    holder.textViewName.setText(name);
                    long amountPrice = price * orderItem.getQuantity();
                    subtotal += amountPrice;

                    customerOrders += orderItem.getQuantity() + "   " + orderItem.getCategory() + "   " + name + "\n";
                    orderWithPrice += orderItem.getQuantity() + "   " + orderItem.getCategory() + "   " + name + "   " + amountPrice + " EGP\n";

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
