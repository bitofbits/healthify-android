package com.example.healthify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import Model.BaseFirestore;
import Model.Customer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Profile_UpdatePage extends AppCompatActivity
{
    TextView name;
    TextView email;
    TextView phone;
    TextView pass;
    Button up;
    Customer tmp = new Customer();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__update_page);

        name = findViewById(R.id.upname);
        email= findViewById(R.id.upemail);
        phone= findViewById(R.id.upphone);
        pass = findViewById(R.id.up_pass);
        up = findViewById(R.id.up_change);
        DocumentReference documentReference = BaseFirestore.db.collection("Customer").document(getIntent().getStringExtra("username"));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    tmp = task.getResult().toObject(Customer.class);
                    name.setText(tmp.getName());
                    email.setText(tmp.getEmail());
                    phone.setText(tmp.getPhone_number());
                    pass.setText(tmp.getPassword());
                }
                else
                {
                    Toast.makeText(Profile_UpdatePage.this, "Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                UpdateDetails();
                Toast.makeText(Profile_UpdatePage.this, "Clicked onClick!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateDetails()
    {
        BaseFirestore.db.collection("Customer").document(email.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot!=null)
                    {
                        Toast.makeText(Profile_UpdatePage.this, "user already exists, enter new email", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String id = tmp.getID();
                        Customer cnew = tmp;
                        cnew.setEmail(email.getText().toString());
                        cnew.setName(name.getText().toString());
                        cnew.setPassword(pass.getText().toString());
                        cnew.setPhone_number(phone.getText().toString());
                        BaseFirestore.db.collection("Customer").document(id).delete();
                        cnew.sendToFirestore();
                        Intent goback = new Intent(getApplicationContext(),CustomerHome.class);
                        goback.putExtra("user_name",cnew.getEmail());
                        startActivity(goback);
                    }
                }
                else
                {
                    Toast.makeText(Profile_UpdatePage.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
