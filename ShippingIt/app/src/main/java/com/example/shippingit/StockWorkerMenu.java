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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class  StockWorkerMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView menu_button;
    private FirebaseAuth auth;
    private TextView header_stworker;
    private ImageView user_picture;
    private View header;
    private String uid;
    private String return_id = "2";
    private String var = "com.example.shippingit";
    private String data = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_worker);

        header_stworker = (TextView) findViewById(R.id.header_name_stworker);
        menu_button = (ImageView) findViewById(R.id.stworker_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.stworker_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_stworker);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        header = navigationView.getHeaderView(0);
        user_picture = (ImageView) header.findViewById(R.id.user_picture);

        if(savedInstanceState == null) {
            header_stworker.setText(R.string.parcels);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_stworker, new ParcelsStock()).commit();
            navigationView.setCheckedItem(R.id.warehouse_parcel);
        }

        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockWorkerMenu.this, UserData.class);
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
            case R.id.warehouse_parcel:
            {
                header_stworker.setText(R.string.parcels);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_stworker, new ParcelsStock()).commit();
                navigationView.setCheckedItem(R.id.warehouse_parcel);
                break;
            }
            case R.id.parcel_queue:
            {
                header_stworker.setText(R.string.parcel_queue);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_stworker, new ParcelsQueue()).commit();
                navigationView.setCheckedItem(R.id.parcel_queue);
                break;
            }
            case R.id.stworker_logout:
            {
                auth.signOut();
                Intent intent = new Intent(StockWorkerMenu.this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            }
            default:
            {
                Toast.makeText(StockWorkerMenu.this, "Error! Something went wrong!", Toast.LENGTH_LONG).show();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

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