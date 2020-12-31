package com.budyfriend.daterangepickerfirebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterKandidat extends RecyclerView.Adapter<AdapterKandidat.KandidatViewHolder> {
    ArrayList<dataKandidat> dataKandidatArrayList;
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterKandidat(ArrayList<dataKandidat> dataKandidatArrayList, Context context) {
        this.dataKandidatArrayList = dataKandidatArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public KandidatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_kandidat, parent, false);
        return new KandidatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KandidatViewHolder holder, int position) {
        holder.viewBind(dataKandidatArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataKandidatArrayList.size();
    }

    public class KandidatViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama_ketua,
                tv_npm_ketua,
                tv_jurusan_ketua,
                tv_nama_wakil,
                tv_npm_wakil,
                tv_jurusan_wakil,
                tv_year,
                tv_count,
                group_to;

        public KandidatViewHolder(@NonNull View itemView) {
            super(itemView);

            group_to = itemView.findViewById(R.id.group_to);
            tv_nama_ketua = itemView.findViewById(R.id.tv_nama_ketua);
            tv_npm_ketua = itemView.findViewById(R.id.tv_npm_ketua);
            tv_jurusan_ketua = itemView.findViewById(R.id.tv_jurusan_ketua);
            tv_nama_wakil = itemView.findViewById(R.id.tv_nama_wakil);
            tv_npm_wakil = itemView.findViewById(R.id.tv_npm_wakil);
            tv_jurusan_wakil = itemView.findViewById(R.id.tv_jurusan_wakil);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_count = itemView.findViewById(R.id.tv_count);
        }

        public void viewBind(final dataKandidat dataKandidat) {
            database.child("user").child(dataKandidat.ketua).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nama_ketua = snapshot.child("nama").getValue(String.class);
                    String jurusan_ketua = snapshot.child("jurusan").getValue(String.class);

                    tv_nama_ketua.setText("Ketua : " + nama_ketua);
                    tv_npm_ketua.setText("NPM : " + dataKandidat.ketua);
                    tv_jurusan_ketua.setText("Jurusan : " + jurusan_ketua);

                    tv_year.setText("Tahun : " + dataKandidat.year);
                    tv_count.setText("Count : " + dataKandidat.count);
                    group_to.setText("Group ke-" + dataKandidat.group);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.child("user").child(dataKandidat.wakil).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nama_wakil = snapshot.child("nama").getValue(String.class);
                    String jurusan_wakil = snapshot.child("jurusan").getValue(String.class);

                    tv_nama_wakil.setText("Wakil : " + nama_wakil);
                    tv_npm_wakil.setText("NPM : " + dataKandidat.wakil);
                    tv_jurusan_wakil.setText("Jurusan : " + jurusan_wakil);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("Apa kamu yakin ingin hapus?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.child("group").child(dataKandidat.key).removeValue();
                                    Toast.makeText(context,"Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create()
                            .show();
                }
            });
        }
    }
}
