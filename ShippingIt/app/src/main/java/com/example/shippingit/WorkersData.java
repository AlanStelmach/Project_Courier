package com.example.shippingit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Calendar;

public class WorkersData extends Fragment {

    private View view;
    private String key = "com.example.shippingit";
    private String uid;
    private ImageView profile_pic;
    private TextView full_name;
    private TextView id;
    private TextView workplace;
    private TextView yob;
    private TextView sex;
    private TextView email;
    private TextView pnumber;
    private TextView added;
    private TextView delivered;

    public WorkersData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workers_data, container, false);

        profile_pic = (ImageView) view.findViewById(R.id.worker_picture);
        full_name = (TextView) view.findViewById(R.id.worker_full_name);
        id = (TextView) view.findViewById(R.id.worker_id);
        workplace = (TextView) view.findViewById(R.id.worker_workplace);
        yob = (TextView) view.findViewById(R.id.worker_yob);
        sex = (TextView) view.findViewById(R.id.worker_sex);
        email = (TextView) view.findViewById(R.id.worker_email);
        pnumber = (TextView) view.findViewById(R.id.worker_pnumber);
        added = (TextView) view.findViewById(R.id.worker_added);
        delivered = (TextView) view.findViewById(R.id.worker_delivered);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        Bundle bundle = this.getArguments();
        uid = bundle.getString(key);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String surname = snapshot.child("surname").getValue(String.class);
                String email_tx = snapshot.child("email").getValue(String.class);
                String pnumber_tx = snapshot.child("pnumber").getValue(String.class);
                String workplace_tx = snapshot.child("workplace").getValue(String.class);
                String id_tx = snapshot.child("id").getValue(String.class);
                String yob_tx = snapshot.child("yob").getValue(String.class);
                String sex_tx = snapshot.child("sex").getValue(String.class);
                String fullname = name + " " + surname;
                String result = String.valueOf(year - Integer.parseInt(yob_tx));
                String value = yob_tx + " ("+result+" year old)";
                full_name.setText(fullname);
                id.setText(id_tx);
                workplace.setText(workplace_tx);
                yob.setText(value);
                sex.setText(sex_tx);
                email.setText(email_tx);
                pnumber.setText(pnumber_tx);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PersonalStats").child(uid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value_added = snapshot.child("added").getValue(String.class);
                String value_delivered = snapshot.child("delivered").getValue(String.class);

                if(value_added == null)
                {
                    added.setText("Parcels added to warehouse: 0");
                }
                else
                {
                    added.setText("Amount of parcels added to warehouse: " + value_added);
                }

                if(value_delivered == null)
                {
                    delivered.setText("Amount of parcels delivered: 0");
                }
                else
                {
                    delivered.setText("Amount of parcels delivered: " + value_delivered);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("Images").child(uid).child("1.jpg");
        reference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile_pic.setImageBitmap(bitmap);
            }
        });
    }
}