package com.example.healthify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Model.Customer;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity
{
    TextView name;
    TextView email;
    TextView phone;
    TextView pass;
    Button sgnup;
    String n="Ram";
    String e="ram@email.com";
    String ph="1234";
    String pa="12";
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        System.out.println("Hodfjnsdjlknsfvn------------------------SIGNUP.JAVA");
        sgnup = findViewById(R.id.button_signup_sign);
        name= findViewById(R.id.edittext_name);
        email=findViewById(R.id.edittext_email);
        phone=findViewById(R.id.edittext_login_enail);
        pass = findViewById(R.id.edittext_pass);
        System.out.println(sgnup);
        sgnup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                n=name.getText().toString();
                e=email.getText().toString();
                ph=phone.getText().toString();
                pa=pass.getText().toString();
                if(n.equals("") || e.equals("") || ph.equals("") || pa.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"All fields are manditory",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Customer c= new Customer(n,e,ph,pa);
                    c.sendToFirestore();
                    Toast.makeText(getApplicationContext(),"Welcome to the Healthify!!! "+ n+"!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, CustomerHome.class);
                    i.putExtra("user_email",email.getText().toString());
                    startActivity(i);
                }

            }
        });
    }

}
