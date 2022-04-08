package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.CustomerDetailPurchaseActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapterHeaderPurchase extends RecyclerView.Adapter<RecyclerAdapterHeaderPurchase.ViewHolder> {
    ArrayList<cHeaderPurchase> arrHTrans = new ArrayList<>();

    public RecyclerAdapterHeaderPurchase(ArrayList<cHeaderPurchase> arrHTrans) {
        this.arrHTrans = arrHTrans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_header_purchase_history, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cHeaderPurchase htrans = arrHTrans.get(position);
        holder.bind(htrans, position);
    }

    @Override
    public int getItemCount() {
        return arrHTrans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdTrans, txtGrandtotal, txtTanggal;
        Button btnDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdTrans = itemView.findViewById(R.id.textView_idTrans);
            txtGrandtotal = itemView.findViewById(R.id.textView_grandtotal);
            txtTanggal = itemView.findViewById(R.id.textView_purchaseDate);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }

        public void bind(cHeaderPurchase htrans, int position) {
            txtIdTrans.setText(htrans.getId()+"");
            txtGrandtotal.setText("Rp " + htrans.getGrandtotalInString()+"");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(htrans.getTanggal());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy");
                String dateFormated = dmyFormat.format(date);

                txtTanggal.setText(dateFormated.toString());
            }

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), CustomerDetailPurchaseActivity.class);
                    i.putExtra("idHTrans", htrans.getId());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
