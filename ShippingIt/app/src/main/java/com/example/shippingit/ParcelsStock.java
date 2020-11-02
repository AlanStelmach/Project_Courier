package com.example.shippingit;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ParcelsStock extends Fragment {

    private TextView no_data;
    private ListView listView;
    private ArrayList<Parcel> arrayList = new ArrayList<>();

    public ParcelsStock() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parcels_stock, container, false);

        no_data = (TextView) view.findViewById(R.id.noData_PStock);
        listView = (ListView) view.findViewById(R.id.listView_PStock);
        no_data.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsAtStock");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String addressee = dataSnapshot.child("addressee").getValue(String.class);
                    String recipient = dataSnapshot.child("recipient").getValue(String.class);
                    String paddress = dataSnapshot.child("pickupaddress").getValue(String.class);
                    String daddress = dataSnapshot.child("deliveryaddress").getValue(String.class);
                    String day = dataSnapshot.child("day").getValue(String.class);
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
    }
}