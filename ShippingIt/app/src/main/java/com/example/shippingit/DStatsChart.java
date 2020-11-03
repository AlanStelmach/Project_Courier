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

public class DStatsChart extends Fragment {

    private View view;
    private TextView no_data1;
    private TextView no_data2;
    private BarChart barChart1;
    private BarChart barChart2;
    private ArrayList<BarEntry> barEntries1 = new ArrayList<>();
    private ArrayList<BarEntry> barEntries2 = new ArrayList<>();
    private TextView added;
    private TextView sent;

    public DStatsChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_d_stats_chart, container, false);

        no_data1 = (TextView) view.findViewById(R.id.noData_Daily1);
        no_data2 = (TextView) view.findViewById(R.id.noData_Daily2);
        barChart1 = (BarChart) view.findViewById(R.id.Daily1Chart);
        barChart2 = (BarChart) view.findViewById(R.id.Daily2Chart);
        added = (TextView) view.findViewById(R.id.daily_added);
        sent = (TextView) view.findViewById(R.id.daily_sent);
        no_data1.setVisibility(View.GONE);
        no_data2.setVisibility(View.GONE);
        onStart();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (day == 0)
        {
            day = 7;
        }
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("DailyStats").child("AddedDaily").child(String.valueOf(year)).child(String.valueOf(month));
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barEntries1.clear();
                String a1 = snapshot.child("1").child("daily_amount").getValue(String.class);
                String a2 = snapshot.child("2").child("daily_amount").getValue(String.class);
                String a3 = snapshot.child("3").child("daily_amount").getValue(String.class);
                String a4 = snapshot.child("4").child("daily_amount").getValue(String.class);
                String a5 = snapshot.child("5").child("daily_amount").getValue(String.class);
                String a6 = snapshot.child("6").child("daily_amount").getValue(String.class);
                String a7 = snapshot.child("7").child("daily_amount").getValue(String.class);
                if (a1 == null && a2 == null && a3 == null && a4 == null && a5 == null && a6 == null && a7 == null)
                {
                    no_data1.setVisibility(View.VISIBLE);
                    barChart1.setVisibility(View.GONE);
                    added.setVisibility(View.GONE);
                }
                else
                {
                    int x1, x2, x3, x4, x5, x6, x7;
                    if(a1 == null)
                    {
                        x1 = 0;
                    }
                    else
                    {
                        x1 = Integer.parseInt(a1);
                    }
                    if(a2 == null)
                    {
                        x2 = 0;
                    }
                    else
                    {
                        x2 = Integer.parseInt(a2);
                    }
                    if(a3 == null)
                    {
                        x3 = 0;
                    }
                    else
                    {
                        x3 = Integer.parseInt(a3);
                    }
                    if(a4 == null)
                    {
                        x4 = 0;
                    }
                    else
                    {
                        x4 = Integer.parseInt(a4);
                    }
                    if(a5 == null)
                    {
                        x5 = 0;
                    }
                    else
                    {
                        x5 = Integer.parseInt(a5);
                    }
                    if(a6 == null)
                    {
                        x6 = 0;
                    }
                    else
                    {
                        x6 = Integer.parseInt(a6);
                    }
                    if(a7 == null)
                    {
                        x7 = 0;
                    }
                    else
                    {
                        x7 = Integer.parseInt(a7);
                    }
                    barEntries1.clear();
                    barEntries1.add(new BarEntry(Float.parseFloat("1"), (float) x1));
                    barEntries1.add(new BarEntry(Float.parseFloat("2"), (float) x2));
                    barEntries1.add(new BarEntry(Float.parseFloat("3"), (float) x3));
                    barEntries1.add(new BarEntry(Float.parseFloat("4"), (float) x4));
                    barEntries1.add(new BarEntry(Float.parseFloat("5"), (float) x5));
                    barEntries1.add(new BarEntry(Float.parseFloat("6"), (float) x6));
                    barEntries1.add(new BarEntry(Float.parseFloat("7"), (float) x7));

                    BarDataSet barDataSet = new BarDataSet(barEntries1,"");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);

                    barChart1.setFitBars(true);
                    barChart1.setData(barData);
                    barChart1.disableScroll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("DailyStats").child("SendDaily").child(String.valueOf(year)).child(String.valueOf(month));
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barEntries2.clear();
                String a1 = snapshot.child("1").child("daily_amount").getValue(String.class);
                String a2 = snapshot.child("2").child("daily_amount").getValue(String.class);
                String a3 = snapshot.child("3").child("daily_amount").getValue(String.class);
                String a4 = snapshot.child("4").child("daily_amount").getValue(String.class);
                String a5 = snapshot.child("5").child("daily_amount").getValue(String.class);
                String a6 = snapshot.child("6").child("daily_amount").getValue(String.class);
                String a7 = snapshot.child("7").child("daily_amount").getValue(String.class);
                if (a1 == null && a2 == null && a3 == null && a4 == null && a5 == null && a6 == null && a7 == null)
                {
                    no_data1.setVisibility(View.VISIBLE);
                    barChart1.setVisibility(View.GONE);
                    sent.setVisibility(View.GONE);
                }
                else
                {
                    int x1, x2, x3, x4, x5, x6, x7;
                    if(a1 == null)
                    {
                        x1 = 0;
                    }
                    else
                    {
                        x1 = Integer.parseInt(a1);
                    }
                    if(a2 == null)
                    {
                        x2 = 0;
                    }
                    else
                    {
                        x2 = Integer.parseInt(a2);
                    }
                    if(a3 == null)
                    {
                        x3 = 0;
                    }
                    else
                    {
                        x3 = Integer.parseInt(a3);
                    }
                    if(a4 == null)
                    {
                        x4 = 0;
                    }
                    else
                    {
                        x4 = Integer.parseInt(a4);
                    }
                    if(a5 == null)
                    {
                        x5 = 0;
                    }
                    else
                    {
                        x5 = Integer.parseInt(a5);
                    }
                    if(a6 == null)
                    {
                        x6 = 0;
                    }
                    else
                    {
                        x6 = Integer.parseInt(a6);
                    }
                    if(a7 == null)
                    {
                        x7 = 0;
                    }
                    else
                    {
                        x7 = Integer.parseInt(a7);
                    }
                    barEntries2.clear();
                    barEntries2.add(new BarEntry(Float.parseFloat("1"), (float) x1));
                    barEntries2.add(new BarEntry(Float.parseFloat("2"), (float) x2));
                    barEntries2.add(new BarEntry(Float.parseFloat("3"), (float) x3));
                    barEntries2.add(new BarEntry(Float.parseFloat("4"), (float) x4));
                    barEntries2.add(new BarEntry(Float.parseFloat("5"), (float) x5));
                    barEntries2.add(new BarEntry(Float.parseFloat("6"), (float) x6));
                    barEntries2.add(new BarEntry(Float.parseFloat("7"), (float) x7));

                    BarDataSet barDataSet = new BarDataSet(barEntries2,"");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);

                    barChart2.setFitBars(true);
                    barChart2.setData(barData);
                    barChart2.disableScroll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}