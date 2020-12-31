package com.budyfriend.daterangepickerfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class KandidatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    LayoutInflater layoutInflater;
    Context context;
    AdapterKandidat adapterKandidat;
    ProgressDialog progressDialog;


    Spinner sp_npm_ketua, sp_npm_wakil;
    EditText et_nama_ketua, et_nama_wakil,
            et_group;
    Button btn_simpan;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kandidat);
        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);
        layoutInflater = getLayoutInflater();
        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        showData();

        getSupportActionBar().setTitle("Kandidat");

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                View itemView = layoutInflater.inflate(R.layout.form_kandidat, null);
                dialog.setView(itemView);

                sp_npm_ketua = itemView.findViewById(R.id.sp_npm_ketua);
                et_nama_ketua = itemView.findViewById(R.id.et_nama_ketua);

                sp_npm_wakil = itemView.findViewById(R.id.sp_npm_wakil);
                et_nama_wakil = itemView.findViewById(R.id.et_nama_wakil);

                et_group = itemView.findViewById(R.id.et_group);
                btn_simpan = itemView.findViewById(R.id.btn_simpan);

                showDataSpiner(sp_npm_ketua, et_nama_ketua);
                showDataSpiner(sp_npm_wakil, et_nama_wakil);

                showCountGroup();


                dialog.show();

                btn_simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String npm_ketua = sp_npm_ketua.getSelectedItem().toString();
                        String nama_ketua = et_nama_ketua.getText().toString();

                        final String npm_wakil = sp_npm_wakil.getSelectedItem().toString();
                        String nama_wakil = et_nama_wakil.getText().toString();

                        final String group = et_group.getText().toString();
                        final String year = simpleDateFormat.format(System.currentTimeMillis());
                        if (nama_ketua.isEmpty()) {
                            et_nama_ketua.setError("Data tidak boleh kosong");
                            et_nama_ketua.requestFocus();
                        } else if (nama_wakil.isEmpty()) {
                            et_nama_wakil.setError("Data tidak boleh kosong");
                            et_nama_wakil.requestFocus();
                        } else if (group.isEmpty()) {
                            et_group.setError("Data tidak boleh kosong");
                            et_group.requestFocus();
                        } else if (nama_ketua.equals(nama_wakil)){
                            Toast.makeText(context,"Data tidak boleh sama",Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();

                            database.child("group").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean saved = false;

                                    for (DataSnapshot item : snapshot.getChildren()) {
                                        dataKandidat kandidat = item.getValue(dataKandidat.class);
                                        if (kandidat != null) {
                                            if (kandidat.ketua.equals(npm_ketua) || kandidat.wakil.equals(npm_ketua)) {
                                                saved = true;
                                            } else if (kandidat.ketua.equals(npm_wakil) || kandidat.wakil.equals(npm_wakil)) {
                                                saved = true;
                                            }
                                        }
                                    }

                                    if (saved) {
                                        Toast.makeText(context, "Data sudah ada", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        database.child("group").child(group + "_" + year).setValue(new dataKandidat(
                                                npm_ketua,
                                                npm_wakil,
                                                group,
                                                year,
                                                0
                                        )).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                });

                sp_npm_ketua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        database.child("user").child(parent.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String nama = snapshot.child("nama").getValue(String.class);
                                et_nama_ketua.setText(nama);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp_npm_wakil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        database.child("user").child(parent.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String nama = snapshot.child("nama").getValue(String.class);
                                et_nama_wakil.setText(nama);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }


    ArrayList<dataKandidat> dataKandidatArrayList = new ArrayList<>();


    private void showData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database.child("group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKandidatArrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    dataKandidat kandidat = item.getValue(dataKandidat.class);
                    if (kandidat != null) {
                        kandidat.setKey(item.getKey());
                        dataKandidatArrayList.add(kandidat);
                    }
                }
                adapterKandidat = new AdapterKandidat(dataKandidatArrayList, context);
                recyclerView.setAdapter(adapterKandidat);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    long countGroup = 0;

    private void showCountGroup() {
        database.child("group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countGroup = snapshot.getChildrenCount() + 1;
                et_group.setText(String.valueOf(countGroup));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ArrayList<String> arrayList = new ArrayList<>();

    private void showDataSpiner(final Spinner sp, final EditText editText) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String npm = item.child("npm").getValue(String.class);
                    arrayList.add(npm);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayList);
                sp.setAdapter(arrayAdapter);

                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        database.child("user").child(parent.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String nama = snapshot.child("nama").getValue(String.class);
                                editText.setText(nama);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}