package com.example.shippingit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddWorker extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner sex_spinner, workplace_spinner;

    public AddWorker() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_worker, container, false);

        sex_spinner = (Spinner) view.findViewById(R.id.spinner_sex);
        workplace_spinner = (Spinner) view.findViewById(R.id.spinner_workplace);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sexes));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.workplaces));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(adapter1);
        workplace_spinner.setAdapter(adapter2);
        sex_spinner.setOnItemSelectedListener(this);
        workplace_spinner.setOnItemSelectedListener(this);

        return  view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}