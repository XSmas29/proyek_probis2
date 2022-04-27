package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapterSellerPendapatan extends RecyclerView.Adapter<RecyclerAdapterSellerPendapatan.ViewHolder> {
    ArrayList<cHeaderPurchase> listHTrans = new ArrayList<>();
    ArrayList<cDetailPurchase> listDTrans = new ArrayList<>();

    public RecyclerAdapterSellerPendapatan(ArrayList<cHeaderPurchase> listHTrans, ArrayList<cDetailPurchase> listDTrans) {
        this.listHTrans = listHTrans;
        this.listDTrans = listDTrans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_topup_seller, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cHeaderPurchase header = listHTrans.get(position);
        ArrayList<cDetailPurchase> detail = new ArrayList<>();

        for (int i = 0; i < listDTrans.size(); i ++){
            if (listDTrans.get(i).getFk_htrans() == header.getId()){
                detail.add(listDTrans.get(i));
            }
        }

        holder.bind(header, detail, position);
    }

    @Override
    public int getItemCount() {
        return listHTrans.size();
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
        public void bind(cHeaderPurchase header, ArrayList<cDetailPurchase> detail, int position) {
            txtID.setText("Transaksi #" + header.getId());


            txtJumlah.setText("Jumlah : Rp " + header.getGrandtotalInString());
            //txtJumlah.setTextColor(itemView.getResources().getColor(R.color.green));


            SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(header.getTanggal());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                java.text.SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy");
                String dateFormated = dmyFormat.format(date);

                txtTanggal.setText(dateFormated.toString());
            }


            if (detail.get(0).getStatus().equalsIgnoreCase("completed")){
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.green));
                txtStatus.setText("Status : Completed");
            }
            else if (detail.get(0).getStatus().equalsIgnoreCase("rejected")){
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.red));
                txtStatus.setText("Status : Rejected");
            }
            else{
                txtStatus.setTextColor(itemView.getResources().getColor(R.color.yellow));
                String status = detail.get(0).getStatus().substring(0, 1).toUpperCase() + detail.get(0).getStatus().substring(1);
                txtStatus.setText("Status : " + status);
            }
        }
    }
}
