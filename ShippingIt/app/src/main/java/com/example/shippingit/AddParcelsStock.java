package com.example.shippingit;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddParcelsStock extends Fragment {

    private Button add_parcel;
    private Button deliver_to_stock;
    private TextView no_data;
    private ListView listView;
    private FirebaseAuth auth;
    private String uid;
    private ArrayList<Parcel> arrayList = new ArrayList<>();
    private String [] field_1;
    private String [] field_2;
    private String [] field_3;
    private String [] field_4;

    public AddParcelsStock() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_parcels_stock, container, false);

        add_parcel = (Button) view.findViewById(R.id.add_parcel);
        deliver_to_stock = (Button) view.findViewById(R.id.deliver_to_stock);
        no_data = (TextView) view.findViewById(R.id.noData_APStock);
        listView = (ListView) view.findViewById(R.id.listView_APStock);
        no_data.setVisibility(View.GONE);

        deliver_to_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Have to fix clearing listView to not double items or to change String array to ArrayList and use method clear(); , It would probably solve it
        // Also it will be a good move to make CustomArrayAdapter as a global class

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ParcelsToStock").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                field_1 = new String[arrayList.size()];
                field_2 = new String[arrayList.size()];
                field_3 = new String[arrayList.size()];
                field_4 = new String[arrayList.size()];
                for(int i = 0; i < arrayList.size(); i++)
                {
                    field_1[i] = arrayList.get(i).getAddressee();
                    field_2[i] = arrayList.get(i).getRecipient();
                    field_3[i] = arrayList.get(i).getPickupaddress();
                    field_4[i] = arrayList.get(i).getDeliveryaddress();
                }
                CustomArrayaAapter customArrayaAapter = new CustomArrayaAapter(getContext(), field_1, field_2, field_3, field_4);
                listView.setAdapter(customArrayaAapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class CustomArrayaAapter extends ArrayAdapter<String>
    {
        Context context;
        String []field1;
        String []field2;
        String []field3;
        String []field4;

        CustomArrayaAapter(Context context, String []field1, String []field2, String []field3, String [] field4)
        {
            super(context, R.layout.list_item, R.id.field1_addressee, field1);
            this.context = context;
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.list_item, parent, false);
            TextView field1_data = (TextView) item.findViewById(R.id.field1_addressee);
            TextView field2_data = (TextView) item.findViewById(R.id.field2_recipient);
            TextView field3_data = (TextView) item.findViewById(R.id.field3_paddress);
            TextView field4_data = (TextView) item.findViewById(R.id.field4_daddress);

            field1_data.setText(field1[position]);
            field2_data.setText(field2[position]);
            field3_data.setText(field3[position]);
            field4_data.setText(field4[position]);

            return item;
        }
    }
}