package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private TextView register;
    private TextView issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login_button = (Button) findViewById(R.id.respassword_button);
        et_email = (EditText) findViewById(R.id.email_respassword);
        et_password = (EditText) findViewById(R.id.password_register);
        login_progressBar = (ProgressBar) findViewById(R.id.progressBarPass);
        register = (TextView) findViewById(R.id.register);
        issue = (TextView) findViewById(R.id.password_issue);
        login_progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, RestoringPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textemail = et_email.getText().toString();
                String textpassword = et_password.getText().toString();
                hideKeyboard(SignIn.this);
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

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private void loggingin(String email, String password)
    {
        login_progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();
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
                        et_email.setText("");
                        et_password.setText("");
                    }
                }
                else
                {
                    login_progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignIn.this, "Error! Wrong e-mail or password!", Toast.LENGTH_LONG).show();
                    et_email.setText("");
                    et_password.setText("");
                    et_email.requestFocus();
                }
            }
        });
    }
}