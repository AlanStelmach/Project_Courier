package com.example.shippingit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;

public class PetrolUsage extends Fragment {

    private EditText petrol_data;
    private Button add_cost;
    private String cost;

    public PetrolUsage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_petrol_usage, container, false);

        petrol_data = (EditText) view.findViewById(R.id.petrol_data);
        add_cost = (Button) view.findViewById(R.id.petrol_button);

        add_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getContext(), view);

                String cost = petrol_data.getText().toString();

                if(cost.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter value first!", Toast.LENGTH_LONG).show();
                    petrol_data.requestFocus();
                }
                else
                {
                    update_cost();
                }
            }
        });

        return view;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private void update_cost()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        final String cost_to_add = petrol_data.getText().toString();

        if (cost == null) {
            double x = 0;
            double y = Double.parseDouble(cost_to_add);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("cost", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            Toast.makeText(getActivity(), "Task finished successfully!", Toast.LENGTH_LONG).show();
            petrol_data.setText("");

        } else {
            double x = Double.parseDouble(cost);
            double y = Double.parseDouble(cost_to_add);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("cost", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            Toast.makeText(getActivity(), "Task finished successfully!", Toast.LENGTH_LONG).show();
            petrol_data.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CostProfit").child(String.valueOf(year)).child(String.valueOf(month));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cost = snapshot.child("cost").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}