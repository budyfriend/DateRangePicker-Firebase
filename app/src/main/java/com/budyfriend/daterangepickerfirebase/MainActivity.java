package com.budyfriend.daterangepickerfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText input_minimal,
            input_maximal;
    Button btn_minimal,
            btn_maximal,
            cari;
    EditText et_npm,
            et_password;
    FloatingActionButton fab_kandidat;

    ArrayList<dataUser> list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton fab_add;
    AlertDialog builderAlert;
    Context context;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY", id);
    ProgressDialog progressDialog;
    Date date_minimal;
    Date date_maximal;

    SearchView searchData;
    Spinner sp_jurusan;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        getSupportActionBar().setTitle("Data View");

        fab_add = findViewById(R.id.fab_add);
        fab_kandidat = findViewById(R.id.fab_kandidat);
        cari = findViewById(R.id.cari);

        input_minimal = findViewById(R.id.input_minimal);
        input_maximal = findViewById(R.id.input_maximal);
        btn_minimal = findViewById(R.id.btn_minimal);
        btn_maximal = findViewById(R.id.btn_maximal);
        recyclerView = findViewById(R.id.recyclerView);
        searchData = findViewById(R.id.searchData);
        sp_jurusan = findViewById(R.id.sp_jurusan);

        fab_kandidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,KandidatActivity.class));
            }
        });

        String[] dataJurusan = {"Teknik Informatika", "Sistem Informasi", "Management Informasi"};
        ArrayList<String> arrayListJursan = new ArrayList<>(Arrays.asList(dataJurusan));
        ArrayAdapter<String> arrayAdapterJurusan = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayListJursan);
        sp_jurusan.setAdapter(arrayAdapterJurusan);

        sp_jurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showData(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String s = sp_jurusan.getSelectedItem().toString();
                Query query = database.child("user").orderByChild("nama").startAt(newText).endAt(newText + "\uf8ff");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            dataUser user = item.getValue(dataUser.class);
                            if (user.getJurusan().equals(s)) {
                                list.add(user);
                            }
                        }
                        adapterItem = new AdapterItem(context, list);
                        recyclerView.setAdapter(adapterItem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }
        });


        btn_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_minimal.setText(simpleDateFormat.format(calendar.getTime()));
                        date_minimal = calendar.getTime();

                        String input1 = input_minimal.getText().toString();
                        String input2 = input_maximal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()) {
                            cari.setEnabled(false);
                        } else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_maximal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_maximal.setText(simpleDateFormat.format(calendar.getTime()));
                        date_maximal = calendar.getTime();

                        String input1 = input_maximal.getText().toString();
                        String input2 = input_minimal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()) {
                            cari.setEnabled(false);
                        } else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                Query query = database.child("user").orderByChild("tgl_pendaftaran").startAt(date_minimal.getTime()).endAt(date_maximal.getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        showLisener(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });


    }

    EditText et_nama, tgl_daftar;
    Spinner et_jurusan;
    Button btnDateDaftar,
            simpanData;
    RadioGroup rb_group;
    RadioButton radioButton;
    Date tgl_daftar_date;

    private void inputData() {
        builderAlert = new AlertDialog.Builder(context).create();
        layoutInflater = getLayoutInflater();
        showInput = layoutInflater.inflate(R.layout.input_layout, null);
        builderAlert.setView(showInput);

        et_nama = showInput.findViewById(R.id.et_nama);
        et_npm = showInput.findViewById(R.id.et_npm);
        et_password = showInput.findViewById(R.id.et_password);
        tgl_daftar = showInput.findViewById(R.id.tgl_daftar);
        et_jurusan = showInput.findViewById(R.id.et_jurusan);
        btnDateDaftar = showInput.findViewById(R.id.btnDateDaftar);
        simpanData = showInput.findViewById(R.id.simpanData);
        rb_group = showInput.findViewById(R.id.rb_group);
        builderAlert.show();

        String[] dataJurusan = {"Teknik Informatika", "Sistem Informasi", "Management Informasi"};
        ArrayList<String> arrayListJursan = new ArrayList<>(Arrays.asList(dataJurusan));
        ArrayAdapter<String> arrayAdapterJurusan = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayListJursan);
        et_jurusan.setAdapter(arrayAdapterJurusan);

        simpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String npm = et_npm.getText().toString();
                final String password = et_password.getText().toString();
                final String nama = et_nama.getText().toString();
                String jurusan = et_jurusan.getSelectedItem().toString();
                String tgl = tgl_daftar.getText().toString();

                if (nama.isEmpty()) {
                    et_nama.setError("Data tidak boleh kosong");
                    et_nama.requestFocus();
                } else if (npm.isEmpty()) {
                    et_npm.setError("Data tidak boleh kosong");
                    et_npm.requestFocus();
                }else if (password.isEmpty()) {
                    et_password.setError("Data tidak boleh kosong");
                    et_password.requestFocus();
                }else if (tgl.isEmpty()) {
                    tgl_daftar.setError("Data tidak boleh kosong");
                    tgl_daftar.requestFocus();
                } else {
                    int selected = rb_group.getCheckedRadioButtonId();
                    radioButton = showInput.findViewById(selected);

                    database.child("user").child(npm).setValue(new dataUser(
                            npm,
                            nama,
                            radioButton.getText().toString(),
                            jurusan,
                            tgl_daftar_date.getTime(),
                            false
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            database.child("login").child(npm).setValue(new dataLogin(npm,nama,password,"user"));
                            Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            builderAlert.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            builderAlert.dismiss();
                        }
                    });

                }
            }
        });

        btnDateDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        tgl_daftar.setText(simpleDateFormat.format(calendar.getTime()));
                        tgl_daftar_date = calendar.getTime();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

    }

    private void showData(String s) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Query query = database.child("user").orderByChild("jurusan").equalTo(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            dataUser user = item.getValue(dataUser.class);
            list.add(user);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
        progressDialog.dismiss();
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