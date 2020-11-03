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

public class GetNewParcels extends Fragment {

    private TextView no_data;
    private Button get_new;
    private ListView listView;
    private ArrayList<Parcel> arrayList = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();
    private FirebaseAuth auth;
    private String uid;
    private String unique_item_id = "";
    private int item_position;
    private View view;
    private Context context;

    public GetNewParcels() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_get_new_parcels, container, false);
        context = getContext();

        no_data = (TextView) view.findViewById(R.id.noData_GNParcels);
        get_new = (Button) view.findViewById(R.id.get_new);
        listView = (ListView) view.findViewById(R.id.listView_GNParcels);
        no_data.setVisibility(View.GONE);
        onStart();

        get_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unique_item_id.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please choose the parcel which will be added!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
                    if(day == 0)
                    {
                        day = 7;
                    }
                    Parcel parcel = new Parcel(arrayList.get(item_position).getAddressee(), arrayList.get(item_position).getRecipient(), arrayList.get(item_position).getPickupaddress(), arrayList.get(item_position).getDeliveryaddress(), String.valueOf(day));
                    FirebaseDatabase.getInstance().getReference("ParcelsToDeliver").child(uid).push().setValue(parcel);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsAtStock");
                    ref.child(unique_item_id).removeValue();
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
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsAtStock");
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
                if (arrayList.size() == 0)
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
    }
}