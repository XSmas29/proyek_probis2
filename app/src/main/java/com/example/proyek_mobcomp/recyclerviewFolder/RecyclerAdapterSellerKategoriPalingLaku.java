package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;

import java.util.ArrayList;

public class RecyclerAdapterSellerKategoriPalingLaku extends RecyclerView.Adapter<RecyclerAdapterSellerKategoriPalingLaku.ViewHolder>{
    ArrayList<cKategori> arrayListKategori;
    ArrayList<cProduct> arrayListProduct;
    ArrayList<cDetailPurchase> arrayListDTrans;

    public RecyclerAdapterSellerKategoriPalingLaku(ArrayList<cKategori> arrayListKategori, ArrayList<cProduct> arrayListProduct, ArrayList<cDetailPurchase> arrayListDTrans) {
        this.arrayListKategori = arrayListKategori;
        this.arrayListProduct = arrayListProduct;
        this.arrayListDTrans = arrayListDTrans;
    }

    // temporary
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_kategori_paling_laku, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cKategori kategori = arrayListKategori.get(position);
        holder.bind(kategori, position);
    }

    @Override
    public int getItemCount() {
        return arrayListKategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaKategori, jumlahJual;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKategori = itemView.findViewById(R.id.textView_namaKategori);
            jumlahJual = itemView.findViewById(R.id.textView_jumlahPenjualan);
        }

        public void bind(cKategori kategori, int position) {
            namaKategori.setText(kategori.getNama());

            // menghitung penjualan per kategori dari dtrans
            int ctrPenjualan = 0;
            for (int i = 0; i < arrayListDTrans.size(); i++){
                for (int j = 0; j < arrayListProduct.size(); j++){
                    if (arrayListProduct.get(j).getId() == arrayListDTrans.get(i).getFk_barang()){
                        if (arrayListProduct.get(j).getFk_kategori() == kategori.getId()){
                            ctrPenjualan++;
                        }
                    }
                }
            }

            jumlahJual.setText(ctrPenjualan+"");

        }
    }
}
