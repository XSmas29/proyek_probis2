package com.example.proyek_mobcomp.recyclerviewFolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.AdminConfTopUpDetailActivity;
import com.example.proyek_mobcomp.AdminConfWithdrawDetailActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.classFolder.cUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapterAdminConfWithdraw extends RecyclerView.Adapter<RecyclerAdapterAdminConfWithdraw.ViewHolder> {
    ArrayList<cTopup> arrTopUp = new ArrayList<>();
    ArrayList<cUser> arrUser = new ArrayList<>();

    public RecyclerAdapterAdminConfWithdraw(ArrayList<cTopup> arrTopUp, ArrayList<cUser> arrUser) {
        this.arrTopUp = arrTopUp;
        this.arrUser = arrUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_admin_conf_top_up, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cTopup topup = arrTopUp.get(position);
        holder.bind(topup, position);
    }

    @Override
    public int getItemCount() {
        return arrTopUp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView txtID;
        TextView txtStatus;
        TextView txtTanggal;
        TextView txtJumlah;
        TextView txtNama;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            txtID = itemView.findViewById(R.id.txtIdTopUp);
            txtStatus = itemView.findViewById(R.id.txtStatusTopUp);
            txtTanggal = itemView.findViewById(R.id.txtTanggalTopUp);
            txtJumlah = itemView.findViewById(R.id.txtJumlahTopUp);
            txtNama = itemView.findViewById(R.id.txtNama);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(cTopup topup, int position) {
            txtID.setText("Withdraw ID #" + topup.getId());

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
            txtJumlah.setText("Jumlah : Rp. " + topup.getJumlah());
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

            for (int i = 0; i < arrUser.size(); i++){
                if (arrUser.get(i).getUsername().equals(topup.getFk_username())){
                    txtNama.setText("Nama Toko : " + arrUser.get(i).getToko());
                }
            }


            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), AdminConfWithdrawDetailActivity.class);
                    i.putExtra("idwithdraw", topup.getId());
                    ((Activity)itemView.getContext()).startActivityForResult(i, 110);
                }
            });
        }
    }
}
