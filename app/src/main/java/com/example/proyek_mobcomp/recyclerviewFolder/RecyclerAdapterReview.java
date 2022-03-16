package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cReview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapterReview extends RecyclerView.Adapter<RecyclerAdapterReview.ViewHolder> {
    ArrayList<cReview> arrReview = new ArrayList<>();

    public RecyclerAdapterReview(ArrayList<cReview> arrReview) {
        this.arrReview = arrReview;
    }

    @NonNull
    @Override
    public RecyclerAdapterReview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_review, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterReview.ViewHolder holder, int position) {
        cReview review = arrReview.get(position);
        holder.bind(review, position);
    }

    @Override
    public int getItemCount() {
        return arrReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView[] arrStar = new ImageView[5];
        TextView reviewer, waktu, isi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardProdukSearchGrid);
            arrStar[0] = itemView.findViewById(R.id.imageView_starReview0);
            arrStar[1] = itemView.findViewById(R.id.imageView_starReview1);
            arrStar[2] = itemView.findViewById(R.id.imageView_starReview2);
            arrStar[3] = itemView.findViewById(R.id.imageView_starReview3);
            arrStar[4] = itemView.findViewById(R.id.imageView_starReview4);
            reviewer = itemView.findViewById(R.id.textView_namaReviewer);
            waktu = itemView.findViewById(R.id.textView_waktuReview);
            isi = itemView.findViewById(R.id.textView_isiReview);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(cReview review, int position) {
            reviewer.setText(review.getNamalengkap());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(review.getTanggal());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMMM yyyy");
                String dateFormated = dmyFormat.format(date);

                waktu.setText(dateFormated.toString());
            }
            isi.setText(review.getIsi());

            if (review.getStar() > 0 && review.getStar() < 6) {
                for (int i = 0; i < review.getStar(); i++) {
                    arrStar[i].setImageResource(R.drawable.ic_baseline_star_24);
                }
            }else{
                Toast.makeText(itemView.getContext(), "Fatal fault star formating", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
