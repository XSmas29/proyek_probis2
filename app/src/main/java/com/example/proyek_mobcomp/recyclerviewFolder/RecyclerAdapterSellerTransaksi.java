package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*

    Update Changes :
    - 18 April : Berubah dari yg menampilkan per barang (dtrans) menjadi per transaksi (htrans)

 */

public class RecyclerAdapterSellerTransaksi extends RecyclerView.Adapter<RecyclerAdapterSellerTransaksi.ViewHolder> {

    ArrayList<cDetailPurchase> listTrans = new ArrayList<>();
    ArrayList<cProduct> listBarangTrans = new ArrayList<>();
    ArrayList<cHeaderPurchase> listHTrans = new ArrayList<>();

    OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public RecyclerAdapterSellerTransaksi(ArrayList<cDetailPurchase> listTrans, ArrayList<cProduct> listBarangTrans, ArrayList<cHeaderPurchase> listHTrans) {
        this.listTrans = listTrans;
        this.listBarangTrans = listBarangTrans;
        this.listHTrans = listHTrans;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // untuk menampilkan per transaksi (htrans)
        cHeaderPurchase header = listHTrans.get(position);

        ArrayList<cDetailPurchase> detail = new ArrayList<>();
        ArrayList<cProduct> produk = new ArrayList<>();

        for (int i = 0; i < listTrans.size(); i ++){
            if (listTrans.get(i).getFk_htrans() == header.getId()){
                detail.add(listTrans.get(i));
                produk.add(listBarangTrans.get(i));
            }
        }

        holder.bind(header, produk, detail, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(header, produk, detail);
            }
        });
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // untuk menampilkan per barang (dtrans)
//        /*
//            - 18 April 2022 : tidak digunakan
//         */
//        cDetailPurchase detail = listTrans.get(position);
//        cProduct produk = listBarangTrans.get(position);
//        holder.bind(produk, detail, position);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickCallback.onItemClicked(produk, detail);
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return listHTrans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgTrans;
        TextView txtIDTrans;
        TextView txtTanggal;
        TextView txtNamaTrans;
        TextView txtJumlahBarang;
        TextView txtTotalTrans;
        TextView txtStatusTrans;
        TextView txtBarangLain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTrans = itemView.findViewById(R.id.imgSellerGambarDetail);
            txtIDTrans = itemView.findViewById(R.id.txtSellerIDTransaksi);
            txtTanggal = itemView.findViewById(R.id.txtSellerTanggal);
            txtNamaTrans = itemView.findViewById(R.id.txtSellerNamaTransaksi);
            txtJumlahBarang = itemView.findViewById(R.id.txtJumlahBarang);
            txtTotalTrans = itemView.findViewById(R.id.txtSellerTotalTransaksi);
            txtStatusTrans = itemView.findViewById(R.id.txtSellerStatusTransaksi);
            txtBarangLain = itemView.findViewById(R.id.txtSellerBarangLain);

        }


        public void bind(cHeaderPurchase header, ArrayList<cProduct> produk, ArrayList<cDetailPurchase> detail, int position) { // untuk menampilkan per transaksi (htrans)
            Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                    produk.get(0).getGambar()).into(imgTrans);

            txtIDTrans.setText("Transaksi #" + header.getId());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(header.getTanggal());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy");
                String dateFormated = dmyFormat.format(date);

                txtTanggal.setText(dateFormated.toString());
            }

            txtNamaTrans.setText("Nama Barang : " + produk.get(0).getNama());

            // menghitung jumlah barang yang dipesan, baik beda jenis barang maupun sama
            int jumlahBarang = 0;
            for (int i = 0; i < detail.size(); i++){
                jumlahBarang +=  detail.get(i).getJumlah();
            }
            txtJumlahBarang.setText("Jumlah : " + jumlahBarang);

            txtTotalTrans.setText("Total : Rp. " + header.getGrandtotalInString());

            if (detail.size() > 1 && produk.size() > 1){
//                txtBarangLain.setVisibility(View.VISIBLE);
//                txtBarangLain.setText("+" + (detail.size() - 1) + " barang lain");
                txtNamaTrans.setText("Nama Barang : " + produk.get(0).getNama() + "\n" +
                                        "+" + (detail.size() - 1) + " barang lain");
            }

            if (detail.get(0).getStatus().equalsIgnoreCase("completed")){
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.green));
                txtStatusTrans.setText("Status : Completed");
            }
            else if (detail.get(0).getStatus().equalsIgnoreCase("rejected")){
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.red));
                txtStatusTrans.setText("Status : Rejected");
            }
            else{
                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.yellow));
                String status = detail.get(0).getStatus().substring(0, 1).toUpperCase() + detail.get(0).getStatus().substring(1);
                txtStatusTrans.setText("Status : " + status);
            }


        }


//        public void bind(cProduct produk, cDetailPurchase detail, int position) { // untuk menampilkan per barang (dtrans)
//            Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
//                    produk.getGambar()).into(imgTrans);
//
//            txtIDTrans.setText("Transaksi #" + detail.getId());
//            txtNamaTrans.setText("Nama Barang : " + produk.getNama());
//            txtTotalTrans.setText("Total : Rp. " + detail.getSubtotalInString());
//
//            if (detail.getStatus().equalsIgnoreCase("completed")){
//                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.green));
//                txtStatusTrans.setText("Status : Completed");
//            }
//            else if (detail.getStatus().equalsIgnoreCase("rejected")){
//                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.red));
//                txtStatusTrans.setText("Status : Rejected");
//            }
//            else{
//                txtStatusTrans.setTextColor(itemView.getResources().getColor(R.color.yellow));
//                String status = detail.getStatus().substring(0, 1).toUpperCase() + detail.getStatus().substring(1);
//                txtStatusTrans.setText("Status : " + status);
//            }
//        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(cHeaderPurchase header, ArrayList<cProduct> produk, ArrayList<cDetailPurchase> detail);
    }
}
