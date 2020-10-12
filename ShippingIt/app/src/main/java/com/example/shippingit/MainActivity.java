package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String uid;
    private boolean employerid;
    private boolean stockworkerid;
    private static int counter=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {

                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if(user != null)
                {
                    if (user.isEmailVerified()) {
                        uid = user.getUid();
                        if (user != null) {
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Employer");
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        employerid = false;
                                        String id = dataSnapshot.getValue(String.class);
                                        if (uid.equals(id)) {
                                            employerid = true;
                                            break;
                                        }
                                    }
                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Stock Worker");
                                    reference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                stockworkerid = false;
                                                String id = dataSnapshot.getValue(String.class);
                                                if (uid.equals(id)) {
                                                    stockworkerid = true;
                                                    break;
                                                }
                                            }

                                            if (employerid) {
                                                Intent intent = new Intent(MainActivity.this, EmployerMenu.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else if (stockworkerid) {
                                                Intent intent = new Intent(MainActivity.this, StockWorkerMenu.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(MainActivity.this, CourierMenu.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } else {
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, SignIn.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    startActivity(intent);
                    finish();
                }
            }
        },counter);
    }
}