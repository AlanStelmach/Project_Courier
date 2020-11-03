package com.example.shippingit;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddParcelsStock extends Fragment {

    private Button add_parcel;
    private Button deliver_to_stock;
    private TextView no_data;
    private ListView listView;
    private FirebaseAuth auth;
    private String uid;
    private ArrayList<Parcel> arrayList = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();
    private String unique_item_id = "";
    private int item_position;
    private String added_count;
    private View view;
    private Context context;

    public AddParcelsStock() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_parcels_stock, container, false);
        context = getContext();

        add_parcel = (Button) view.findViewById(R.id.add_parcel);
        deliver_to_stock = (Button) view.findViewById(R.id.deliver_to_stock);
        no_data = (TextView) view.findViewById(R.id.noData_APStock);
        listView = (ListView) view.findViewById(R.id.listView_APStock);
        no_data.setVisibility(View.GONE);
        onStart();

        deliver_to_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unique_item_id.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please choose the parcel which will be delivered to the warehouse!", Toast.LENGTH_LONG).show();
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
                    FirebaseDatabase.getInstance().getReference("ParcelsAtQueue").push().setValue(parcel);
                    int x;
                    if (added_count == null)
                    {
                        x = 0;
                    }
                    else
                    {
                        x = Integer.parseInt(added_count);
                    }
                    int result = x + 1;
                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("added", String.valueOf(result));
                    FirebaseDatabase.getInstance().getReference("PersonalStats").child(uid).updateChildren(map);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsToStock").child(uid);
                    ref.child(unique_item_id).removeValue();
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                }
            }
        });

        add_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_cont_courier, new AddParcelCourier());
                transaction.addToBackStack(null);
                transaction.commit();
                TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_courier);
                header_emp.setText(R.string.add_parcel);
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_courier);
                navigationView.setCheckedItem(R.id.parcels_to_stock);
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
        TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_courier);
        header_emp.setText(R.string.parcels_stock);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsToStock").child(uid);
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
    }
}