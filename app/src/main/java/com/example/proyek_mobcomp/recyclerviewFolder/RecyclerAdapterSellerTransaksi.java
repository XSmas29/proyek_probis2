package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterSellerTransaksi extends RecyclerView.Adapter<RecyclerAdapterSellerTransaksi.ViewHolder> {

    ArrayList<cDetailPurchase> listTrans = new ArrayList<>();
    ArrayList<cProduct> listBarangTrans = new ArrayList<>();

    OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public RecyclerAdapterSellerTransaksi(ArrayList<cDetailPurchase> listTrans, ArrayList<cProduct> listBarangTrans) {
        this.listTrans = listTrans;
        this.listBarangTrans = listBarangTrans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_trans_seller, parent, false);

        RecyclerAdapterSellerTransaksi.ViewHolder viewHolder = new RecyclerAdapterSellerTransaksi.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cDetailPurchase detail = listTrans.get(position);
        cProduct produk = listBarangTrans.get(position);
        holder.bind(produk, detail, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(produk, detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTrans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgTrans;
        TextView txtIDTrans;
        TextView txtNamaTrans;
        TextView txtTotalTrans;
        TextView txtStatusTrans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTrans = itemView.findViewById(R.id.imgSellerGambarDetail);
            txtIDTrans = itemView.findViewById(R.id.txtSellerIDTransaksi);
            txtNamaTrans = itemView.findViewById(R.id.txtSellerNamaTransaksi);
            txtTotalTrans = itemView.findViewById(R.id.txtSellerTotalTransaksi);
            txtStatusTrans = itemView.findViewById(R.id.txtSellerStatusTransaksi);

        }

        public void bind(cProduct produk, cDetailPurchase detail, int position) {
            Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                    produk.getGambar()).into(imgTrans);

            txtIDTrans.setText("Transaksi #" + detail.getId());
            txtNamaTrans.setText("Nama Barang : " + produk.getNama());
            txtTotalTrans.setText("Total : Rp. " + detail.getSubtotalInString());

            if (detail.getStatus().equalsIgnoreCase("completed")){
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.green));
                txtStatusTrans.setText("Status : Completed");
            }
            else if (detail.getStatus().equalsIgnoreCase("rejected")){
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.red));
                txtStatusTrans.setText("Status : Rejected");
            }
            else{
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.yellow));
                String status = detail.getStatus().substring(0, 1).toUpperCase() + detail.getStatus().substring(1);
                txtStatusTrans.setText("Status : " + status);
            }
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(cProduct produk, cDetailPurchase detail);
    }
}
