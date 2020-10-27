package com.example.shippingit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserData extends AppCompatActivity {

    //private ImageView back;
    private ImageView profile_pic;
    private String var = "com.example.shippingit";
    private String data = "userid";
    private String return_id;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        final Intent intent = getIntent();
        return_id = intent.getStringExtra(var);
        id = intent.getStringExtra(data);

        //back = (ImageView) findViewById(R.id.back);
        profile_pic = (ImageView) findViewById(R.id.user_picture);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(return_id.equals("1"))
                {
                    Intent intent1 = new Intent(UserData.this, EmployerMenu.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    finish();
                }
                else if(return_id.equals("2"))
                {
                    Intent intent2 = new Intent(UserData.this, StockWorkerMenu.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    finish();
                }
                else
                {
                    Intent intent3 = new Intent(UserData.this, CourierMenu.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent3);
                    finish();
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}