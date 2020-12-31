package com.budyfriend.daterangepickerfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<dataUser> dataUserArrayList;
    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY",id);

    public AdapterItem(Context context, ArrayList<dataUser> dataUserArrayList) {
        this.context = context;
        this.dataUserArrayList = dataUserArrayList;
    }

    @NonNull
    @Override
    public AdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.viewBind(dataUserArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataUserArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama,
                tv_jk,
                tv_jurusan,
                tv_tanggal_pendaftaran;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_jk = itemView.findViewById(R.id.tv_jk);
            tv_jurusan = itemView.findViewById(R.id.tv_jurusan);
            tv_tanggal_pendaftaran = itemView.findViewById(R.id.tv_tanggal_pendaftaran);
        }

        public void viewBind(dataUser dataUser) {
            tv_nama.setText(dataUser.getNama());
            tv_jk.setText(dataUser.getJk());
            tv_jurusan.setText(dataUser.getJurusan());
            tv_tanggal_pendaftaran.setText(simpleDateFormat.format(dataUser.getTgl_pendaftaran()));
        }
    }
}
