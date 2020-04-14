package com.example.healthify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Model.Order;
import Model.Product;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    Button login;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("Hello_______________________"+this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Product p1 = new Product("Aloo Matar",100);
        Product p2 = new Product("Poha",50);
        Product p3 = new Product("Upma",50);
        Product p4 = new Product("Masala Dosa",100);
        Product p5 = new Product("Cheese Masala Dosa",120);
        Product p6 = new Product("Biryani",350);
        Product p7 = new Product("Grilled Sandwhich",150);
        Product p8 = new Product("Bread Butter",30);
        Product p9 = new Product("Roti(all)",50);
        p1.sendToFirestore();
        p2.sendToFirestore();
        p3.sendToFirestore();
        p4.sendToFirestore();
        p5.sendToFirestore();
        p6.sendToFirestore();
        p7.sendToFirestore();
        p8.sendToFirestore();
        p9.sendToFirestore();

        login = findViewById(R.id.button_login);
        signup= findViewById(R.id.button_mainactivity_signup);
    }
    public void login_onclick(View view)
    {
        Intent log = new Intent(MainActivity.this,login.class);
        startActivity(log);
        System.out.println("Entered onclick");
//        DocumentReference docRef = db.collection("Customer").document("55");
//        docRef.get();
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    spinner.setVisibility(View.GONE);
//                    if (document.exists())
//                    {
//                        t.setText(document.getData().toString());
//                        Log.d("yo: ", "DocumentSnapshot data: " + document.getData());
//                    } else
//                    {
//                        Log.d("yo2: ", "No such document");
//                    }
//                } else {
//                    Log.d("yo3: ", "get failed with ", task.getException());
//                }
//            }
//        });
    }
    public void signup_onclick(View view)
    {
        System.out.println("Hello_______________________"+this);
        Intent sign = new Intent(MainActivity.this,signup.class);
        startActivity(sign);
    }
}
