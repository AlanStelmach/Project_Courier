package com.example.shippingit;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;

public class CtoBChart extends Fragment {

    private Button add_data_button;
    private TextView no_data;
    private PieChart pieChart;
    private String cost;
    private String profit;
    private double budget;
    private double x, y;
    private ArrayList<PieEntry> arrayList = new ArrayList<>();
    private TextView profit_tx;
    private TextView costs_tx;
    private TextView budget_tx;

    public CtoBChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cto_b_chart, container, false);

        add_data_button = (Button) view.findViewById(R.id.add_data);
        no_data = (TextView) view.findViewById(R.id.noData_CtoB);
        profit_tx = (TextView) view.findViewById(R.id.profit);
        costs_tx = (TextView) view.findViewById(R.id.costs);
        budget_tx = (TextView) view.findViewById(R.id.budget);
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        no_data.setVisibility(View.GONE);
        onStart();

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

    @Override
    public void onStart() {
        super.onStart();

        TextView header_emp = (TextView) getActivity().findViewById(R.id.header_name_emp);
        header_emp.setText(R.string.costs_to_budget);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CostProfit").child(String.valueOf(year)).child(String.valueOf(month));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                cost = snapshot.child("cost").getValue(String.class);
                profit = snapshot.child("profit").getValue(String.class);
                if(cost == null && profit == null)
                {
                    no_data.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                }
                else {
                    if (cost == null) {
                        x = 0;
                    } else {
                        x = Double.parseDouble(cost);
                    }
                    if(profit == null)
                    {
                        y = 0;
                    }
                    else
                    {
                        y = Double.parseDouble(profit);
                    }
                    budget = y - x;
                    arrayList.clear();
                    arrayList.add(new PieEntry((float) budget, "Budget"));
                    arrayList.add(new PieEntry((float) x, "Cost"));

                    PieDataSet pieDataSet = new PieDataSet(arrayList, "Costs to Budget Chart");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(16f);
                    PieData pieData = new PieData(pieDataSet);

                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Costs to Budget Chart");
                    pieChart.disableScroll();
                    pieChart.animate();
                    profit_tx.setText("Monthly Profit: "+profit);
                    costs_tx.setText("Monthly Costs: "+cost);
                    budget_tx.setText("Monthly Budget: ~ "+Math.round(budget));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}