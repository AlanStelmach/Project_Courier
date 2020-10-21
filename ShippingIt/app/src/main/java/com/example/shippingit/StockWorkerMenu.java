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

public class  StockWorkerMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView menu_button;
    private FirebaseAuth auth;
    private TextView header_stworker;

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

        auth = FirebaseAuth.getInstance();

        if(savedInstanceState == null) {
            header_stworker.setText(R.string.parcels);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_stworker, new ParcelsStock()).commit();
            navigationView.setCheckedItem(R.id.warehouse_parcel);
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
}