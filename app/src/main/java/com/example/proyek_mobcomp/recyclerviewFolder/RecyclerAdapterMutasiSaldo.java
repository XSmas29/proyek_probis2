package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.CustomerDetailPurchaseActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.classFolder.cMutasi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapterMutasiSaldo extends RecyclerView.Adapter<RecyclerAdapterMutasiSaldo.ViewHolder>{
    ArrayList<cMutasi> arrMutasi = new ArrayList<>();

    public RecyclerAdapterMutasiSaldo(ArrayList<cMutasi> arrMutasi) {
        this.arrMutasi = arrMutasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_mutasi_saldo, parent, false);

        RecyclerAdapterMutasiSaldo.ViewHolder viewHolder = new RecyclerAdapterMutasiSaldo.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cMutasi mutasi = arrMutasi.get(position);
        holder.bind(mutasi, position);
    }

    @Override
    public int getItemCount() {
        return arrMutasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTanggalMutasi, txtJumlahMutasi, txtKeteranganMutasi;
        LinearLayout backgroundMutasiSaldo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTanggalMutasi = itemView.findViewById(R.id.txtTglMutasi);
            txtJumlahMutasi = itemView.findViewById(R.id.txtJumlahMutasi);
            txtKeteranganMutasi = itemView.findViewById(R.id.txtKeteranganMutasi);
            backgroundMutasiSaldo = itemView.findViewById(R.id.backgroundMutasiSaldo);
        }

        public void bind(cMutasi mutasi, int position) {
            if (mutasi.getJumlah() > 0){
                backgroundMutasiSaldo.setBackgroundColor(Color.parseColor("#FFDDDD"));
                txtJumlahMutasi.setTextColor(Color.parseColor("#BB2F2F"));
                txtJumlahMutasi.setText("Jumlah : + Rp. " + mutasi.getJumlah());
            }
            else{
                backgroundMutasiSaldo.setBackgroundColor(Color.parseColor("#DDFFDD"));
                txtJumlahMutasi.setTextColor(Color.parseColor("#31BB2F"));
                txtJumlahMutasi.setText("Jumlah : - Rp. " + (mutasi.getJumlah() * -1));
            }
            txtKeteranganMutasi.setText("Keterangan : " + mutasi.getKeterangan());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(mutasi.getTanggal());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                String dateFormated = dmyFormat.format(date);

                txtTanggalMutasi.setText(dateFormated.toString());
            }
        }
    }
}
