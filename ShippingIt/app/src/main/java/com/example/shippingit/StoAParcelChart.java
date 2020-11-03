package com.example.shippingit;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;

public class StoAParcelChart extends Fragment {

    private View view;
    private TextView no_data;
    private BarChart barChart;
    private ArrayList<BarEntry> arrayList = new ArrayList<>();
    private TextView delivered_tx;
    private TextView all_tx;

    public StoAParcelChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sto_a_parcel_chart, container, false);

        no_data = (TextView) view.findViewById(R.id.noData_StoA);
        delivered_tx = (TextView) view.findViewById(R.id.delivered_data);
        all_tx = (TextView) view.findViewById(R.id.all_data);
        barChart = (BarChart) view.findViewById(R.id.BarChart);
        no_data.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DailyStats").child("DeliveredToAll").child(String.valueOf(year)).child(String.valueOf(month));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                String delivered = snapshot.child("delivered").getValue(String.class);
                String all = snapshot.child("all").getValue(String.class);
                if(delivered == null && all == null)
                {
                    no_data.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.GONE);
                }
                else
                {
                    int x, y;
                    if(delivered == null)
                    {
                        x = 0;
                    }
                    else
                    {
                        x = Integer.parseInt(delivered);
                    }
                    if(all == null)
                    {
                        y = 0;
                    }
                    else
                    {
                        y = Integer.parseInt(all);
                    }
                    arrayList.clear();
                    arrayList.add(new BarEntry(Float.parseFloat("1"), (float) x));
                    arrayList.add(new BarEntry(Float.parseFloat("2"), (float) y));

                    BarDataSet barDataSet = new BarDataSet(arrayList,"");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);

                    barChart.setFitBars(true);
                    barChart.setData(barData);
                    barChart.disableScroll();
                    delivered_tx.setText("Parcels Delivered: "+x);
                    all_tx.setText("All Parcels: "+y);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}