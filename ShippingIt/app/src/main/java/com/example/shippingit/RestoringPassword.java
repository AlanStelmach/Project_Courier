package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RestoringPassword extends AppCompatActivity {

    private EditText email;
    private Button send;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoring_password);

        email = (EditText) findViewById(R.id.email_respassword);
        send = (Button) findViewById(R.id.respassword_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPass);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(RestoringPassword.this);

                String email_txt = email.getText().toString();

                if(email_txt.isEmpty())
                {
                    Toast.makeText(RestoringPassword.this, "Please enter E-mail!", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_txt).matches())
                {
                    Toast.makeText(RestoringPassword.this,"E-mail form is incorrect!", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email_txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RestoringPassword.this, "Success!", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RestoringPassword.this, "Error!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }
}