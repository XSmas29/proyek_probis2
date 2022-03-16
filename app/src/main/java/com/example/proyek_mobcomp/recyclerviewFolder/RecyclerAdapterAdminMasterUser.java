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

import com.example.proyek_mobcomp.AdminUserProfileActivity;
import com.example.proyek_mobcomp.CustomerDetailPurchaseActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cUser;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerAdapterAdminMasterUser extends RecyclerView.Adapter<RecyclerAdapterAdminMasterUser.ViewHolder> {
    ArrayList<cUser> arrUser = new ArrayList<>();

    public RecyclerAdapterAdminMasterUser(ArrayList<cUser> arrUser) {
        this.arrUser = arrUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_admin_master_user, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cUser user = arrUser.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return arrUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtNama, txtRole;
        Button btnDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.textView_username);
            txtNama = itemView.findViewById(R.id.textView_nama);
            txtRole = itemView.findViewById(R.id.textView_role);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }

        public void bind(cUser user, int position) {
            txtUsername.setText(user.getUsername());
            txtNama.setText(user.getNama());
            txtRole.setText(user.getRole().toLowerCase());

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), AdminUserProfileActivity.class);
                    i.putExtra("username", user.getUsername());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
