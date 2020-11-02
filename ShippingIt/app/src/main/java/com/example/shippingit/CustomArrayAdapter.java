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

public class CustomArrayAdapter extends ArrayAdapter<Parcel> {
    private Context context;
    private ArrayList<Parcel> arrayList;

    CustomArrayAdapter(Context context, ArrayList<Parcel> arrayList)
    {
        super(context, R.layout.list_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.list_item, parent, false);
        TextView field1_data = (TextView) item.findViewById(R.id.field1_addressee);
        TextView field2_data = (TextView) item.findViewById(R.id.field2_recipient);
        TextView field3_data = (TextView) item.findViewById(R.id.field3_paddress);
        TextView field4_data = (TextView) item.findViewById(R.id.field4_daddress);

        field1_data.setText("From: " + arrayList.get(position).getAddressee());
        field2_data.setText("To: " + arrayList.get(position).getRecipient());
        field3_data.setText("PA: " + arrayList.get(position).getPickupaddress());
        field4_data.setText("DA: " + arrayList.get(position).getDeliveryaddress());

        return item;
    }
}
