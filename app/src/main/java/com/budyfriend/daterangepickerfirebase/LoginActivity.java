package com.budyfriend.daterangepickerfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText username,
            password;

    Button btn_login;
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        getSupportActionBar().setTitle("Login");

        context = this;
        progressDialog = new ProgressDialog(context);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user  = username.getText().toString();
                final String pass  = password.getText().toString();
                if (user.isEmpty()){
                    username.setError("Data tidak boleh kosong");
                    username.requestFocus();
                }else if (pass.isEmpty()){
                    password.setError("Data tidak boleh kosong");
                    password.requestFocus();
                }else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    database.child("login").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                dataLogin login = snapshot.getValue(dataLogin.class);
                                if (login!= null){
                                    if (login.password.equals(pass)){
                                        progressDialog.dismiss();

                                        Preferences.setActive(context,true);
                                        Preferences.setUsername(context,user);
                                        if (login.role.equals("user")){
                                            startActivity(new Intent(context,VoteActivity.class));
                                            Preferences.setRole(context,"user");
                                            finish();
                                        }else {
                                            startActivity(new Intent(context,MainActivity.class));
                                            Preferences.setRole(context,"admin");
                                            finish();
                                        }

                                    }else {
                                        Toast.makeText(context,"Password salah",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }

                            }else {
                                Toast.makeText(context,"User belum terdaftar",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.isActive(context)){
            if (Preferences.getRole(context).equals("user")){
                startActivity(new Intent(context,VoteActivity.class));
                Preferences.setRole(context,"user");
            }else {
                startActivity(new Intent(context,MainActivity.class));
                Preferences.setRole(context,"admin");
            }
            finish();
        }
    }
}