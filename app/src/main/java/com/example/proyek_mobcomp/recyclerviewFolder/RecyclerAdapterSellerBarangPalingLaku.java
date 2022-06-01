package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cKategori;

import java.util.ArrayList;

public class RecyclerAdapterSellerBarangPalingLaku extends RecyclerView.Adapter<RecyclerAdapterSellerBarangPalingLaku.ViewHolder>{

    ArrayList<String> listBarang;
    ArrayList<Integer> listJumlah;

    public RecyclerAdapterSellerBarangPalingLaku(ArrayList<String> listBarang, ArrayList<Integer> listJumlah) {
        this.listBarang = listBarang;
        this.listJumlah = listJumlah;
    }

    @NonNull
    @Override
    public RecyclerAdapterSellerBarangPalingLaku.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_kategori_paling_laku, parent, false);

        RecyclerAdapterSellerBarangPalingLaku.ViewHolder viewHolder = new RecyclerAdapterSellerBarangPalingLaku.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSellerBarangPalingLaku.ViewHolder holder, int position) {
        String barang = listBarang.get(position);
        Integer jumlah = listJumlah.get(position);
        holder.bind(barang, jumlah);
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaKategori, jumlahJual;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKategori = itemView.findViewById(R.id.textView_namaKategori);
            jumlahJual = itemView.findViewById(R.id.textView_jumlahPenjualan);
        }

        public void bind(String barang, int jumlah) {
            namaKategori.setText(barang);
            jumlahJual.setText(jumlah+"");

        }
    }
}
