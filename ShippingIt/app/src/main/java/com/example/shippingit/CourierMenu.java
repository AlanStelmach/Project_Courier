package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CourierMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView menu_button;
    private FirebaseAuth auth;
    private TextView header_courier;

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

        auth = FirebaseAuth.getInstance();

        if(savedInstanceState == null) {
            header_courier.setText(R.string.parcels_delivery);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_courier, new ParcelsDelivery()).commit();
            navigationView.setCheckedItem(R.id.parcels_to_deliver);
        }

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
}