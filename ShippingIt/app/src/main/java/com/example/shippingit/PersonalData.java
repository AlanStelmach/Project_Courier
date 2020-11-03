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

public class PersonalData extends Fragment {

    private View view;
    private FirebaseAuth auth;
    private String uid;
    private TextView no_data;
    private ListView listView;
    private Context context;
    private ArrayList<String> item_id = new ArrayList<>();
    private ArrayList<UserFull_Name> arrayList = new ArrayList<>();
    private String key = "com.example.shippingit";

    public PersonalData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        context = getContext();

        no_data = (TextView) view.findViewById(R.id.noData_PData);
        listView = (ListView) view.findViewById(R.id.listView_PData);
        no_data.setVisibility(View.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(key,item_id.get(position));
                WorkersData workersData = new WorkersData();
                workersData.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_cont_emp, workersData);
                transaction.addToBackStack(null);
                transaction.commit();
                TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_emp);
                header_emp.setText(R.string.pdata);
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_emp);
                navigationView.setCheckedItem(R.id.personal_data);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_emp);
        header_emp.setText(R.string.personal_data);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item_id.clear();
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(!dataSnapshot.getKey().equals(uid))
                    {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String surname = dataSnapshot.child("surname").getValue(String.class);
                        String workplace = dataSnapshot.child("workplace").getValue(String.class);
                        String id = dataSnapshot.getKey();
                        item_id.add(id);
                        arrayList.add(new UserFull_Name(name, surname, workplace));
                    }
                }
                if (arrayList.size() == 0)
                {
                    no_data.setVisibility(View.VISIBLE);
                }
                PDataArrayAdapter pDataArrayAdapter = new PDataArrayAdapter(context, arrayList);
                listView.setAdapter(pDataArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}