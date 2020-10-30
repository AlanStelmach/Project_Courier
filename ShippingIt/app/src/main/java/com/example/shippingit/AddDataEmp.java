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

public class AddDataEmp extends Fragment {

    private String cost;
    private String profit;
    private EditText et_profit;
    private EditText et_cost;
    private Button data;

    public AddDataEmp() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_data_emp, container, false);

        et_profit = (EditText) view.findViewById(R.id.profit_data);
        et_cost = (EditText) view.findViewById(R.id.cost_data);
        data = (Button) view.findViewById(R.id.data_button);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getContext(), view);

                String profit_tx = et_profit.getText().toString();
                String cost_tx = et_cost.getText().toString();

                if(profit_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter value of profits!", Toast.LENGTH_LONG).show();
                    et_profit.requestFocus();
                }
                else if(cost_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter value of costs!", Toast.LENGTH_LONG).show();
                    et_cost.requestFocus();
                }
                else
                {
                    update_data();
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

    private void update_data()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        final String profit_data = et_profit.getText().toString();
        final String cost_data = et_cost.getText().toString();
        
        if (cost == null) {
            double x = 0;
            double y = Double.parseDouble(cost_data);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("cost", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            Toast.makeText(getActivity(), "Task finished successfully!", Toast.LENGTH_LONG).show();
            et_cost.setText("");

        } else {
            double x = Double.parseDouble(cost);
            double y = Double.parseDouble(cost_data);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("cost", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            Toast.makeText(getActivity(), "Task finished successfully!", Toast.LENGTH_LONG).show();
            et_cost.setText("");
        }

        if (profit == null) {
            double x = 0;
            double y = Double.parseDouble(profit_data);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("profit", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            et_profit.setText("");

        } else {
            double x = Double.parseDouble(profit);
            double y = Double.parseDouble(profit_data);
            double result = x + y;
            final HashMap<String, Object> map = new HashMap<>();
            map.put("profit", String.valueOf(result));
            FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).updateChildren(map);
            et_profit.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cost = String.valueOf(snapshot.child("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).child("cost").getValue(String.class));
                profit = String.valueOf(snapshot.child("CostProfit").child(String.valueOf(year)).child(String.valueOf(month)).child("profit").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}