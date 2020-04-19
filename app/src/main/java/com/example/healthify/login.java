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
import Model.PromoCodes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity
{
    TextView email;
    TextView password;
    Button signin;
    TextView signup;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PromoCodes c1 = new PromoCodes("AA123",0.1);
        c1.sendToFirestore();
        BaseFirestore.db.collection("PromoCodes").document(c1.getID()).update("Code","AB123");
        System.out.println("PROMO SHOULD BE UPDATED NOW--------------------");
        //DeliveryPartner raj = new DeliveryPartner("dp","1212","dp","dp",false,0);
        //raj.sendToFirestore();
        email=findViewById(R.id.edittext_login_enail);
        password=findViewById(R.id.editText_password);

        signup=findViewById(R.id.text_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

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
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"User does not exist , please sign up",Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                        else
                                        {

                                        }
                                    }
                                });
                                //Toast.makeText(getApplicationContext(),"User does not exist , please sign up",Toast.LENGTH_SHORT).show();
                                //finish();
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
//        Intent i = new Intent(context,ChooseMapAfterSignup.class);
        Intent i = new Intent(context,CustomerHome.class);
        i.putExtra("user_email",email.getText().toString());
        startActivity(i);
    }
    public void got_to_DeliveryPerson_Home()
    {

        System.out.println("Inside Delivery Partner!"+email.getText().toString());
        Intent it = new Intent(context,DeliveryPartnerHome.class);
        it.putExtra("user_name",email.getText().toString());
        startActivity(it);
    }


}
