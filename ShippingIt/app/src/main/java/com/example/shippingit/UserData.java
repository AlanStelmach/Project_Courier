package com.example.shippingit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;

public class UserData extends AppCompatActivity {

    private ImageView profile_pic;
    private String var = "com.example.shippingit";
    private String data = "userid";
    private String return_id;
    private String id;
    private TextView fullname, workerid, workplace, sex, age, email, pnumber;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        final Intent intent = getIntent();
        return_id = intent.getStringExtra(var);
        id = intent.getStringExtra(data);
        fullname = (TextView) findViewById(R.id.tv_fullname);
        workerid = (TextView) findViewById(R.id.tv_id);
        workplace = (TextView) findViewById(R.id.tv_workplace);
        sex = (TextView) findViewById(R.id.tv_sex);
        age = (TextView) findViewById(R.id.tv_age);
        email = (TextView) findViewById(R.id.tv_email);
        pnumber = (TextView) findViewById(R.id.tv_pnumber);
        profile_pic = (ImageView) findViewById(R.id.user_picture);
        onStart();

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                String name_data = dataSnapshot.child("Users").child(id).child("name").getValue(String.class);
                String surname_data = dataSnapshot.child("Users").child(id).child("surname").getValue(String.class);
                String fullname_string = name_data + " " + surname_data;
                String id_data = dataSnapshot.child("Users").child(id).child("id").getValue(String.class);
                String workplace_data = dataSnapshot.child("Users").child(id).child("workplace").getValue(String.class);
                String yob_data = dataSnapshot.child("Users").child(id).child("yob").getValue(String.class);
                String sex_data = dataSnapshot.child("Users").child(id).child("sex").getValue(String.class);
                String email_data = dataSnapshot.child("Users").child(id).child("email").getValue(String.class);
                String pnumber_data = dataSnapshot.child("Users").child(id).child("pnumber").getValue(String.class);
                int YOB = Integer.parseInt(yob_data);
                String result = String.valueOf(year-YOB);
                String value = yob_data + " ("+result+" year old)";
                fullname.setText(fullname_string);
                workerid.setText(id_data);
                workplace.setText(workplace_data);
                age.setText(value);
                sex.setText(sex_data);
                email.setText(email_data);
                pnumber.setText(pnumber_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("Images").child(id).child("1.jpg");
        reference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile_pic.setImageBitmap(bitmap);
            }
        });
    }

    private void FileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageuri = data.getData();
            UploadImage();
        }
    }

    private String getFileExtension (Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding...");
        progressDialog.show();

        if(imageuri != null)
        {
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Images").child(id).child("1.jpg");
            fileRef.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            if(url.isEmpty()) {
                                progressDialog.dismiss();
                                Toast.makeText(UserData.this, "Error! Please try again!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UserData.this, "Image uploaded successfully!", Toast.LENGTH_LONG).show();
                                onStart();
                            }
                        }
                    });
                }
            });
        }
    }
}