package com.example.shippingit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class PDataArrayAdapter extends ArrayAdapter<UserFull_Name>
{
    private Context context;
    private ArrayList<UserFull_Name> arrayList;

    PDataArrayAdapter(Context context, ArrayList<UserFull_Name> arrayList)
    {
        super(context, R.layout.user_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.user_item, parent, false);
        TextView field1_data = (TextView) item.findViewById(R.id.first_name);
        TextView field2_data = (TextView) item.findViewById(R.id.second_name);
        TextView field3_data = (TextView) item.findViewById(R.id.workplace_name);

        field1_data.setText("Name: " + arrayList.get(position).getName());
        field2_data.setText("Surname: " + arrayList.get(position).getSurname());
        field3_data.setText("Workplace: " + arrayList.get(position).getWorkplace());

        return item;
    }
}
