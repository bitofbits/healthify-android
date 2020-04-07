package com.example.healthify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    //public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button login;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Hello_______________________"+this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.button_login);
        signup= findViewById(R.id.button_mainactivity_signup);
    }
    public void login_onclick(View view)
    {
        Intent log = new Intent(MainActivity.this,login.class);
        startActivity(log);
        System.out.println("Entered onclick");
        // Access a Cloud Firestore instance from your Activity
        //Customer c2 = new Customer();

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
