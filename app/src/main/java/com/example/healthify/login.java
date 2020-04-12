package com.example.healthify;

import android.content.Context;
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
import Model.DeliveryPartner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity
{
    TextView email;
    TextView password;
    Button signin;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DeliveryPartner raj = new DeliveryPartner("raj@email.com","1212","raj","raj123",true,0);
        raj.sendToFirestore();
        email=findViewById(R.id.edittext_login_enail);
        password=findViewById(R.id.editText_password);
        System.out.println("INSIDE LOGIN CLASS -----------------------");
        System.out.println("Initially : "+signin);
        signin=findViewById(R.id.button_signin);
        System.out.println("Finally : "+signin);
        signin.setOnClickListener(new View.OnClickListener()
        {
            //Toast.makeText(getApplicationContext(),"User exists with password :",Toast.LENGTH_SHORT).show();
            @Override
            public void onClick(View view)
            {
                final String e_mail = email.getText().toString();
                DocumentReference docref = Customer.db.collection("Customer").document(e_mail);
                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {

                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists())
                            {
                                String pa = doc.getString("password");

                                Customer c = doc.toObject(Customer.class);
                                //System.out.println(c);
                                if(pa.equals(password.getText().toString()))
                                {
                                    //login successful

                                    //Toast.makeText(getApplicationContext(),"Correct: "+c.getEmail(),Toast.LENGTH_SHORT).show();
                                    go_to_Customet_Home();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Incorrect Password customer",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                final DocumentReference  doc2= BaseFirestore.db.collection("DeliveryPartner").document(e_mail);
                                doc2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot ref = task.getResult();
                                            if(ref.exists())
                                            {
                                                String pa = ref.getString("password");
                                                if(pa.equals(password.getText().toString()))
                                                {
                                                    got_to_DeliveryPerson_Home();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),"Incorrect Password delivery",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        else
                                        {

                                        }
                                    }
                                });
                                Toast.makeText(getApplicationContext(),"User does not exist , please sign up",Toast.LENGTH_SHORT).show();
                                finish();
                                //go to previous activity
                            }
                        }
                        else
                        {
                            //now check DileveryPartner collection.
                            Toast.makeText(getApplicationContext(),"Error connecting to servers",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void go_to_Customet_Home()
    {
//        Bundle b = new Bundle();
//        b.putString("user_name",email.getText().toString());
        Intent i = new Intent(context,CustomerHome.class);
        i.putExtra("user_email",email.getText().toString());
        startActivity(i);
    }
    public void got_to_DeliveryPerson_Home()
    {

        System.out.println("Inside Delivery Partner!");
        Intent i = new Intent(context,DeliveryPartnerHome.class);
        i.putExtra("user_name",email.getText().toString());
        startActivity(i);
    }

}
