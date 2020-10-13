package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SignIn extends AppCompatActivity {

    private Button login_button;
    private EditText et_email, et_password;
    private ProgressBar login_progressBar;
    private FirebaseAuth auth;
    private String uid;
    private boolean employerid;
    private boolean stockworkerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login_button = (Button) findViewById(R.id.signin_button);
        et_email = (EditText) findViewById(R.id.email_edittext);
        et_password = (EditText) findViewById(R.id.password_edittext);
        login_progressBar = (ProgressBar) findViewById(R.id.progressBar);
        login_progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textemail = et_email.getText().toString();
                String textpassword = et_password.getText().toString();

                if(textemail.isEmpty())
                {
                    Toast.makeText(SignIn.this, "Please enter your e-mail!", Toast.LENGTH_LONG).show();
                    et_email.requestFocus();
                }
                else if(textpassword.isEmpty())
                {
                    Toast.makeText(SignIn.this, "Please enter your password!", Toast.LENGTH_LONG).show();
                    et_password.requestFocus();
                }
                else
                {
                    loggingin(textemail,textpassword);
                }
            }
        });
    }

    private void loggingin(String email, String password)
    {
        login_progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();
                if(task.isSuccessful())
                {
                    login_progressBar.setVisibility(View.GONE);
                    if(user.isEmailVerified())
                    {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Employer");
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    employerid = false;
                                    String id = dataSnapshot.getValue(String.class);
                                    if(uid.equals(id))
                                    {
                                        employerid = true;
                                        break;
                                    }
                                }
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("StockWorker");
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                        {
                                            stockworkerid = false;
                                            String id = dataSnapshot.getValue(String.class);
                                            if(uid.equals(id))
                                            {
                                                stockworkerid = true;
                                                break;
                                            }
                                        }

                                        if(employerid)
                                        {
                                            Toast.makeText(SignIn.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignIn.this, EmployerMenu.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if(stockworkerid)
                                        {
                                            Toast.makeText(SignIn.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignIn.this, StockWorkerMenu.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(SignIn.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignIn.this, CourierMenu.class);
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
                    else
                    {
                        user.sendEmailVerification();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                        builder.setCancelable(true);
                        builder.setTitle("Verification first!");
                        builder.setMessage("We can't let you log in without verification! Please check your e-mail account for more details!");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
                else
                {
                    login_progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignIn.this, "Error! Wrong e-mail or password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}