package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register_button;
    private boolean bool;
    private FirebaseAuth auth;
    private String name_data;
    private String surname_data;
    private String id_data;
    private String yob_data;
    private String pnumber_data;
    private String sex_data;
    private String workplace_data;
    private ProgressBar progressBar;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email_respassword);
        password = (EditText) findViewById(R.id.password_register);
        register_button = (Button) findViewById(R.id.respassword_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPass);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(RegisterActivity.this);

                String email_tx = email.getText().toString();
                String password_tx = password.getText().toString();

                if(email_tx.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Please enter your e-mail!", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                }
                else if(password_tx.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Please enter your password!", Toast.LENGTH_LONG).show();
                    password.requestFocus();
                }
                else if(password_tx.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Password is too short!", Toast.LENGTH_LONG).show();
                    password.requestFocus();
                }
                else
                {
                    register();
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

    private void register()
    {
        progressBar.setVisibility(View.VISIBLE);
        final String Semail = email.getText().toString();
        final String Spassword = password.getText().toString();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bool = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String email_val = dataSnapshot.child("email").getValue(String.class);
                    if (email_val.equals(Semail)) {
                        bool = true;
                        name_data = dataSnapshot.child("name").getValue(String.class);
                        surname_data = dataSnapshot.child("surname").getValue(String.class);
                        sex_data = dataSnapshot.child("sex").getValue(String.class);
                        yob_data = dataSnapshot.child("yob").getValue(String.class);
                        pnumber_data = dataSnapshot.child("pnumber").getValue(String.class);
                        id_data = dataSnapshot.child("id").getValue(String.class);
                        workplace_data = dataSnapshot.child("workplace").getValue(String.class);
                        value = dataSnapshot.getKey().toString();
                        break;
                    }
                }
                if (bool)
                {
                    auth.createUserWithEmailAndPassword(Semail, Spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser userauth = auth.getCurrentUser();
                                userauth.sendEmailVerification();
                                if(!userauth.isEmailVerified())
                                {
                                    reference.child(value).removeValue();
                                    String uid = auth.getCurrentUser().getUid();
                                    auth.signOut();
                                    if (workplace_data.equals("Stock Worker")) {
                                        FirebaseDatabase.getInstance().getReference("StockWorker").child(uid).setValue(uid);
                                    }
                                    User user = new User(name_data, surname_data, Semail, pnumber_data, id_data, sex_data, workplace_data, yob_data);
                                    FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            email.setText("");
                                            password.setText("");
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, SignIn.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                email.setText("");
                                password.setText("");
                                Toast.makeText(RegisterActivity.this, "There is an account with such E-mail address already!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    email.setText("");
                    password.setText("");
                    Toast.makeText(RegisterActivity.this, "There is no account with such E-mail address!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}