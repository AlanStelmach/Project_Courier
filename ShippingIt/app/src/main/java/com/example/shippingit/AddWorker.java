package com.example.shippingit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddWorker extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner sex_spinner, workplace_spinner;
    private Button add_user;
    private EditText name, surname, email, yob, pnumber;
    private int count=1;

    public AddWorker() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_worker, container, false);

        sex_spinner = (Spinner) view.findViewById(R.id.spinner_sex);
        workplace_spinner = (Spinner) view.findViewById(R.id.spinner_workplace);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sexes));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.workplaces));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(adapter1);
        workplace_spinner.setAdapter(adapter2);
        sex_spinner.setOnItemSelectedListener(this);
        workplace_spinner.setOnItemSelectedListener(this);
        name = (EditText) view.findViewById(R.id.et_name);
        surname = (EditText) view.findViewById(R.id.et_surname);
        email = (EditText) view.findViewById(R.id.et_email);
        yob = (EditText) view.findViewById(R.id.et_age);
        pnumber = (EditText) view.findViewById(R.id.et_pnumber);
        add_user = (Button) view.findViewById(R.id.add_user);

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getContext(), view);

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                String name_tx = name.getText().toString();
                String surname_tx = surname.getText().toString();
                String email_tx = email.getText().toString();
                String yob_tx = yob.getText().toString();
                String pnumber_tx = pnumber.getText().toString();
                String sex_tx = sex_spinner.getSelectedItem().toString();
                String workplace_tx = workplace_spinner.getSelectedItem().toString();

                if(name_tx.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter name!", Toast.LENGTH_LONG).show();
                    name.requestFocus();
                }
                else if(surname_tx.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter surname!", Toast.LENGTH_LONG).show();
                    surname.requestFocus();
                }
                else if(email_tx.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter e-mail!", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_tx).matches())
                {
                    Toast.makeText(getActivity(),"E-mail form is incorrect!", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                }
                else if(yob_tx.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter year of birth!", Toast.LENGTH_LONG).show();
                    yob.requestFocus();
                }
                else if(Integer.parseInt(yob_tx) < year-60 || Integer.parseInt(yob_tx) > year-18)
                {
                    Toast.makeText(getActivity(),"Year of birth form is incorrect!", Toast.LENGTH_LONG).show();
                    yob.requestFocus();
                }
                else if(pnumber_tx.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter phone number!", Toast.LENGTH_LONG).show();
                    pnumber.requestFocus();
                }
                else if(pnumber_tx.length() < 9)
                {
                    Toast.makeText(getActivity(),"Phone number is too short!", Toast.LENGTH_LONG).show();
                    pnumber.requestFocus();
                }
                else if(sex_tx.equals("None"))
                {
                    Toast.makeText(getActivity(),"Please select right gender!", Toast.LENGTH_LONG).show();
                    sex_spinner.requestFocus();
                }
                else if(workplace_tx.equals("None"))
                {
                    Toast.makeText(getActivity(),"Please select right workplace!", Toast.LENGTH_LONG).show();
                    workplace_spinner.requestFocus();
                }
                else
                {
                    registerUser();
                }
            }
        });

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int temp = Integer.parseInt(dataSnapshot.child("id").getValue(String.class));
                    if (temp>count) {
                        count = temp;
                    } else if(temp<count){
                        continue;
                    }
                    else
                    {
                        count = 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private void registerUser() {
        final String Sname = name.getText().toString();
        final String Ssurname = surname.getText().toString();
        final String Semail = email.getText().toString();
        final String Syob = yob.getText().toString();
        final String Spnumber = pnumber.getText().toString();
        final String Ssex = sex_spinner.getSelectedItem().toString();
        final String Sworkplace = workplace_spinner.getSelectedItem().toString();
        final String Sid = String.valueOf(count+1);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Adding...");
        progressDialog.show();

        User user = new User(Sname, Ssurname, Semail, Spnumber, Sid, Ssex, Sworkplace, Syob);
        FirebaseDatabase.getInstance().getReference("Users").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    name.setText("");
                    surname.setText("");
                    email.setText("");
                    pnumber.setText("");
                    yob.setText("");
                    sex_spinner.setSelection(0);
                    workplace_spinner.setSelection(0);
                    Toast.makeText(getActivity(),"Success!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Error!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}