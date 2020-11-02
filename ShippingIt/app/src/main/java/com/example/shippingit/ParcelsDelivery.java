package com.example.shippingit;

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

public class ParcelsDelivery extends Fragment {

    private TextView no_data;
    private Button deliver;
    private ListView listView;
    private ArrayList<Parcel> arrayList = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();
    private FirebaseAuth auth;
    private String uid;
    private String delivered_count;
    private String unique_item_id = "";
    private String amount;

    public ParcelsDelivery() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parcels_delivery, container, false);

        no_data = (TextView) view.findViewById(R.id.noData_PDelivery);
        deliver = (Button) view.findViewById(R.id.deliver);
        listView = (ListView) view.findViewById(R.id.listView_PDelivery);
        no_data.setVisibility(View.GONE);

        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unique_item_id.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please choose the parcel which will be delivered!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
                    if(day == 0)
                    {
                        day = 7;
                    }
                    int x;
                    if (delivered_count == null)
                    {
                        x = 0;
                    }
                    else
                    {
                        x = Integer.parseInt(delivered_count);
                    }
                    int result = x + 1;
                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("delivered", String.valueOf(result));
                    FirebaseDatabase.getInstance().getReference("PersonalStats").child(uid).updateChildren(map);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsToDeliver").child(uid);
                    ref.child(unique_item_id).removeValue();
                    int z;
                    if(amount == null)
                    {
                        z = 0;

                    }
                    else
                    {
                        z = Integer.parseInt(amount);
                    }
                    int result1 = z + 1;
                    final HashMap<String, Object> dailymap = new HashMap<>();
                    dailymap.put("daily_amount", String.valueOf(result1));
                    FirebaseDatabase.getInstance().getReference("DailyStats").child("SendDaily").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).updateChildren(dailymap);
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(day == 0)
        {
            day = 7;
        }
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsToDeliver").child(uid);
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
                CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(getContext(), arrayList);
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
                delivered_count = snapshot.child("delivered").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("DailyStats").child("SendDaily").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day));
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                amount = snapshot.child("daily_amount").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}