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

public class RecyclerAdapterCustomerHistoryTopUp extends RecyclerView.Adapter<RecyclerAdapterCustomerHistoryTopUp.ViewHolder> {
    ArrayList<cTopup> arrTopup = new ArrayList<>();

    public RecyclerAdapterCustomerHistoryTopUp(ArrayList<cTopup> arrTopup) {
        this.arrTopup = arrTopup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_history_top_up, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cTopup topup = arrTopup.get(position);
        holder.bind(topup, position);
    }

    @Override
    public int getItemCount() {
        return arrTopup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID;
        TextView txtStatus;
        TextView txtTanggal;
        TextView txtJumlah;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtIdTopUp);
            txtStatus = itemView.findViewById(R.id.txtStatusTopUp);
            txtTanggal = itemView.findViewById(R.id.txtTanggalTopUp);
            txtJumlah = itemView.findViewById(R.id.txtJumlahTopUp);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(cTopup topup, int position) {
            txtID.setText("Top Up ID #" + topup.getId());

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
//            System.out.println(formatted);

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
