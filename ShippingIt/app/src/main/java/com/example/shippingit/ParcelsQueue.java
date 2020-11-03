package com.example.shippingit;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ParcelsQueue extends Fragment {

    private TextView no_data;
    private Button add_to_stock;
    private ListView listView;
    private FirebaseAuth auth;
    private String uid;
    private ArrayList<Parcel> arrayList = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();
    private String amount;
    private String added_count;
    private String unique_item_id = "";
    private int item_position;
    private View view;
    private Context context;
    private String all;

    public ParcelsQueue() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parcels_queue, container, false);
        context = getContext();

        no_data = (TextView) view.findViewById(R.id.noData_PQueue);
        add_to_stock = (Button) view.findViewById(R.id.add_to_stock);
        listView = (ListView) view.findViewById(R.id.listView_PQueue);
        no_data.setVisibility(View.GONE);
        onStart();

        add_to_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unique_item_id.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please choose the parcel which will be added to the warehouse!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
                    if (day == 0)
                    {
                        day = 7;
                    }
                    int z;
                    if (added_count == null)
                    {
                        z = 0;
                    }
                    else
                    {
                        z = Integer.parseInt(added_count);
                    }
                    Parcel parcel = new Parcel(arrayList.get(item_position).getAddressee(), arrayList.get(item_position).getRecipient(), arrayList.get(item_position).getPickupaddress(), arrayList.get(item_position).getDeliveryaddress(), String.valueOf(day));
                    FirebaseDatabase.getInstance().getReference("ParcelsAtStock").push().setValue(parcel);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsAtQueue");
                    ref.child(unique_item_id).removeValue();
                    int result = z + 1;
                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("added", String.valueOf(result));
                    FirebaseDatabase.getInstance().getReference("PersonalStats").child(uid).updateChildren(map);
                    int x;
                    if(amount == null)
                    {
                        x = 0;
                    }
                    else
                    {
                        x = Integer.parseInt(amount);
                    }
                    int result1 = x + 1;
                    final HashMap<String, Object> dailymap = new HashMap<>();
                    dailymap.put("daily_amount", String.valueOf(result1));
                    FirebaseDatabase.getInstance().getReference("DailyStats").child("AddedDaily").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).updateChildren(dailymap);
                    int y;
                    if(all == null)
                    {
                        y = 0;
                    }
                    else
                    {
                        y = Integer.parseInt(all);
                    }
                    int result2 = y + 1;
                    final HashMap<String, Object> allmap = new HashMap<>();
                    allmap.put("all", String.valueOf(result2));
                    FirebaseDatabase.getInstance().getReference("DailyStats").child("DeliveredToAll").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(allmap);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0; i < listView.getChildCount(); i++)
                {
                    if(position == i)
                    {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gold));
                        unique_item_id = item_id.get(position);
                        item_position = position;
                    }
                    else
                    {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println(getContext());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (day == 0)
        {
            day = 7;
        }
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsAtQueue");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                item_id.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String addressee = dataSnapshot.child("addressee").getValue(String.class);
                    String recipient = dataSnapshot.child("recipient").getValue(String.class);
                    String paddress = dataSnapshot.child("pickupaddress").getValue(String.class);
                    String daddress = dataSnapshot.child("deliveryaddress").getValue(String.class);
                    String day = dataSnapshot.child("day").getValue(String.class);
                    String id = dataSnapshot.getKey();
                    item_id.add(id);
                    arrayList.add(new Parcel(addressee, recipient, paddress, daddress, day));
                }
                if(arrayList.size() == 0)
                {
                    no_data.setVisibility(View.VISIBLE);
                }
                CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(context, arrayList);
                listView.setAdapter(customArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PersonalStats").child(uid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                added_count = snapshot.child("added").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("DailyStats").child("AddedDaily").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day));
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                amount = snapshot.child("daily_amount").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference("DailyStats").child("DeliveredToAll").child(String.valueOf(year)).child(String.valueOf(month));
       ref4.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               all = snapshot.child("all").getValue(String.class);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}