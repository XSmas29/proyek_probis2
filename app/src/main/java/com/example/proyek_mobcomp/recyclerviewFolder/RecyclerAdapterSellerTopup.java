package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cTopup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapterSellerTopup extends RecyclerView.Adapter<RecyclerAdapterSellerTopup.ViewHolder> {

    ArrayList<cTopup> listTopup = new ArrayList<>();

    public RecyclerAdapterSellerTopup(ArrayList<cTopup> listTopup) {
        this.listTopup = listTopup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_topup_seller, parent, false);

        RecyclerAdapterSellerTopup.ViewHolder viewHolder = new RecyclerAdapterSellerTopup.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cTopup topup = listTopup.get(position);
        holder.bind(topup, position);
    }

    @Override
    public int getItemCount() {
        return listTopup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtID;
        TextView txtStatus;
        TextView txtTanggal;
        TextView txtJumlah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtID = itemView.findViewById(R.id.txtIdCairkanSaldo);
            txtStatus = itemView.findViewById(R.id.txtStatusCairkanSaldo);
            txtTanggal = itemView.findViewById(R.id.txtTanggalCairkanSaldo);
            txtJumlah = itemView.findViewById(R.id.txtJumlahCairkanSaldo);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(cTopup topup, int position) {
            txtID.setText("Pencairan #" + topup.getId());

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

            Date d = null;
            try
            {
                d = input.parse(topup.getCreated());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            System.out.println(formatted);

            txtTanggal.setText("Tanggal : " + formatted);
            txtJumlah.setText("Jumlah : Rp. " + topup.getJumlahInString());
            if (topup.getStatus() == 0){
                txtStatus.setText("Status : Pending");
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.yellow));
            }
            else if (topup.getStatus() == 1){
                txtStatus.setText("Status : Success");
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.green));
            }else if (topup.getStatus() == -1){
                txtStatus.setText("Status : Rejected");
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.red));
            }
        }
    }
}
