package com.budyfriend.daterangepickerfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterVote  extends RecyclerView.Adapter<AdapterVote.VoteViewHolder> {
    ArrayList<dataKandidat> dataKandidatArrayList;
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterVote(ArrayList<dataKandidat> dataKandidatArrayList, Context context) {
        this.dataKandidatArrayList = dataKandidatArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_kandidat,parent,false);
        return new VoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        holder.viewBind(dataKandidatArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataKandidatArrayList.size();
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama_ketua,
                tv_npm_ketua,
                tv_jurusan_ketua,
                tv_nama_wakil,
                tv_npm_wakil,
                tv_jurusan_wakil,
                tv_year,
                tv_count,
                group_to;
        Switch voting;

        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);

            voting = itemView.findViewById(R.id.voting);
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
            database.child("user").child(Preferences.getUsername(context)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataUser user = snapshot.getValue(dataUser.class);
                    if (user!= null){
                        if (user.isVoting()){
                            voting.setVisibility(View.GONE);
                        }else {
                            voting.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            voting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database.child("user").child(Preferences.getUsername(context)).child("voting").setValue(true);
                    database.child("group").child(dataKandidat.key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dataKandidat kandidat = snapshot.getValue(dataKandidat.class);
                            long count = kandidat.count + 1;
                            database.child("group").child(dataKandidat.key).child("count").setValue(count);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


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
                    group_to.setText("Group ke-"+dataKandidat.group);
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
        }
    }
}
