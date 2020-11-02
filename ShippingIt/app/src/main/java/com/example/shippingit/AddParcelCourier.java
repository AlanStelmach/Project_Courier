package com.example.shippingit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class AddParcelCourier extends Fragment {

    private EditText addressee;
    private EditText recipient;
    private EditText paddress;
    private EditText daddress;
    private Button add;
    private String address = ", Lublin, Poland";
    private FirebaseAuth auth;
    private String uid;


    public AddParcelCourier() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_parcel_courier, container, false);

        addressee = (EditText) view.findViewById(R.id.addressee_data);
        recipient = (EditText) view.findViewById(R.id.recipient_data);
        paddress = (EditText) view.findViewById(R.id.pickupaddress_data);
        daddress = (EditText) view.findViewById(R.id.deliveryaddress_data);
        add = (Button) view.findViewById(R.id.parcel_button);

        auth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getContext(), view);

                String addressee_tx = addressee.getText().toString();
                String recipient_tx = recipient.getText().toString();
                String paddress_tx = paddress.getText().toString();
                String daddress_tx = daddress.getText().toString();

                if(addressee_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter addressee's full name!", Toast.LENGTH_LONG).show();
                    addressee.requestFocus();
                }
                else if(recipient_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter recipient's full name!", Toast.LENGTH_LONG).show();
                    recipient.requestFocus();
                }
                else if(paddress_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter pick up address!", Toast.LENGTH_LONG).show();
                    paddress.requestFocus();
                }
                else if(daddress_tx.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter delivery address!", Toast.LENGTH_LONG).show();
                    daddress.requestFocus();
                }
                else
                {
                    paddress_tx += address;
                    daddress_tx += address;
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK)-1; // -1, if day - 1 == 0 --> day = 7
                    if(day == 0)
                    {
                        day = 7;
                    }
                    uid = auth.getCurrentUser().getUid();
                    Parcel parcel = new Parcel(addressee_tx, recipient_tx, paddress_tx, daddress_tx, String.valueOf(day));
                    FirebaseDatabase.getInstance().getReference("ParcelsToStock").child(uid).push().setValue(parcel);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                    addressee.setText("");
                    recipient.setText("");
                    paddress.setText("");
                    daddress.setText("");
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
}