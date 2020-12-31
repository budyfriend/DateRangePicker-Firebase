package com.budyfriend.daterangepickerfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context;
    ArrayList<dataKandidat> dataKandidatArrayList = new ArrayList<>();
    AdapterVote adapterVote;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);


        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        recyclerView = findViewById(R.id.recyclerView);

        showData();


    }

    private void showData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database.child("group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKandidatArrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    dataKandidat kandidat = item.getValue(dataKandidat.class);
                    if (kandidat!= null){
                        kandidat.setKey(item.getKey());
                        dataKandidatArrayList.add(kandidat);
                    }
                }
                adapterVote = new AdapterVote(dataKandidatArrayList,context);
                recyclerView.setAdapter(adapterVote);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        MenuItem item = menu.findItem(R.id.item_logout);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(context)
                        .setMessage("Apa kamu yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Preferences.clearData(context);
                                startActivity(new Intent(context,LoginActivity.class));
                                finish();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create()
                        .show();
                return true;
            }
        });

        return true;
    }
}