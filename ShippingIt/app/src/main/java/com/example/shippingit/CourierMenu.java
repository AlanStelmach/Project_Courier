package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CourierMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView menu_button;
    private FirebaseAuth auth;
    private TextView header_courier;
    private ImageView user_picture;
    private View header;
    private String uid;
    private String return_id = "3";
    private String var = "com.example.shippingit";
    private String data = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        header_courier = (TextView) findViewById(R.id.header_name_course);
        menu_button = (ImageView) findViewById(R.id.courier_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.courier_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_courier);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        header = navigationView.getHeaderView(0);
        user_picture = (ImageView) header.findViewById(R.id.user_picture);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        if(savedInstanceState == null) {
            header_courier.setText(R.string.parcels_delivery);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new ParcelsDelivery()).commit();
            navigationView.setCheckedItem(R.id.parcels_to_deliver);
        }

        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourierMenu.this, UserData.class);
                intent.putExtra(var, return_id);
                intent.putExtra(data, uid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.parcels_to_deliver:
            {
                header_courier.setText(R.string.parcels_delivery);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new ParcelsDelivery()).commit();
                navigationView.setCheckedItem(R.id.parcels_to_deliver);
                break;
            }
            case R.id.get_parcels:
            {
                header_courier.setText(R.string.get_parcels);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new GetNewParcels()).commit();
                navigationView.setCheckedItem(R.id.get_parcels);
                break;
            }
            case R.id.parcels_to_stock:
            {
                header_courier.setText(R.string.parcels_stock);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new AddParcelsStock()).commit();
                navigationView.setCheckedItem(R.id.parcels_to_stock);
                break;
            }
            case R.id.petrol_usage:
            {
                header_courier.setText(R.string.petrol);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new PetrolUsage()).commit();
                navigationView.setCheckedItem(R.id.petrol_usage);
                break;
            }
            case R.id.courier_logout:
            {
                auth.signOut();
                Intent intent = new Intent(CourierMenu.this, SignIn.class);
                startActivity(intent);
                finish();
                break;
            }
            default:
            {
                Toast.makeText(CourierMenu.this, "Error! Something went wrong!", Toast.LENGTH_LONG).show();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Users").child(uid).child("name").getValue(String.class);
                String surname = dataSnapshot.child("Users").child(uid).child("surname").getValue(String.class);
                header = navigationView.getHeaderView(0);
                TextView name_surname = (TextView) header.findViewById(R.id.full_name);
                name_surname.setText(name + " " + surname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("Images").child(uid).child("1.jpg");
        reference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView profile = (ImageView) header.findViewById(R.id.user_picture);
                profile.setImageBitmap(bitmap);
            }
        });
    }
}