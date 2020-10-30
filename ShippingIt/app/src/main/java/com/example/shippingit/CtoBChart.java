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

public class CtoBChart extends Fragment {

    private Button add_data_button;

    public CtoBChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cto_b_chart, container, false);

        add_data_button = (Button) view.findViewById(R.id.add_data);

        add_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_cont_emp, new AddDataEmp());
                transaction.addToBackStack(null);
                transaction.commit();
                TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_emp);
                header_emp.setText(R.string.add_data);
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_emp);
                navigationView.setCheckedItem(R.id.costs_budget);
            }
        });

        return view;
    }
}