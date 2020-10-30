package com.example.shippingit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class AddParcelsStock extends Fragment {

    private Button add_parcel;
    private Button deliver_to_stock;

    public AddParcelsStock() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_parcels_stock, container, false);

        add_parcel = (Button) view.findViewById(R.id.add_parcel);
        deliver_to_stock = (Button) view.findViewById(R.id.deliver_to_stock);

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
}