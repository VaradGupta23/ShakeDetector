package com.example.shakedetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Form extends AppCompatActivity {
    EditText txt_firstname, txt_lastname, txt_email, txt_password, txt_cpassword, txt_eno1, txt_eno2;
    Button btn_register;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);

        txt_firstname = findViewById(R.id.firstname);
        txt_lastname = findViewById(R.id.lastname);
        txt_email = findViewById(R.id.email);
        txt_password = findViewById(R.id.password);
        txt_cpassword = findViewById(R.id.c_password);
        txt_eno1 = findViewById(R.id.en1);
        txt_eno2 = findViewById(R.id.en2);
        btn_register = findViewById(R.id.button);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = txt_firstname.getText().toString();
                final String lastname = txt_lastname.getText().toString();
                final String email = txt_email.getText().toString();
                final String password = txt_password.getText().toString();
                final String cpassword = txt_cpassword.getText().toString();
                final String en1 = txt_eno1.getText().toString();
                final String en2 = txt_eno2.getText().toString();

                if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword) ||
                        TextUtils.isEmpty(en1) || TextUtils.isEmpty(en2)) {
                    Toast.makeText(Signup_Form.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6 || cpassword.length() < 6) {
                    Toast.makeText(Signup_Form.this, "Password too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(cpassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup_Form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();
                                            user information = new user(firstname, lastname, email, en1, en2);
                                            FirebaseDatabase.getInstance().getReference("User")
                                                    .child(uid)
                                                    .setValue(information)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(Signup_Form.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(Signup_Form.this, "User registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Signup_Form.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Signup_Form.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
