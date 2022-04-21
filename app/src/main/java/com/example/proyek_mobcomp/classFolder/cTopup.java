package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cTopup implements Parcelable {
    int id;
    String fk_username;
    int jumlah;
    String bukti;
    int status;
    String created;
    String updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFk_username() {
        return fk_username;
    }

    public void setFk_username(String fk_username) {
        this.fk_username = fk_username;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getJumlahInString() {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(this.jumlah);
        String temp = ""; // penampung sementara untuk dalam for
//        System.out.println(awal.length());
        for (int i = awal.length(); i > 0 ; i--){
            if (ctr % 3 == 0) {
                if (i != awal.length()) {
                    hasil = "." + hasil;
                }
            }
            if (i != awal.length()) {
                hasil = awal.substring((i - 1), (i)) + hasil;
            }else{
                hasil = awal.substring((i - 1)) + hasil;
            }
            ctr++;
        }

        return hasil;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public cTopup(int id, String fk_username, int jumlah, String bukti, int status, String created, String updated) {
        this.id = id;
        this.fk_username = fk_username;
        this.jumlah = jumlah;
        this.bukti = bukti;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    protected cTopup(Parcel in) {
        id = in.readInt();
        fk_username = in.readString();
        jumlah = in.readInt();
        bukti = in.readString();
        status = in.readInt();
        created = in.readString();
        updated = in.readString();
    }

    public static final Creator<cTopup> CREATOR = new Creator<cTopup>() {
        @Override
        public cTopup createFromParcel(Parcel in) {
            return new cTopup(in);
        }

        @Override
        public cTopup[] newArray(int size) {
            return new cTopup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fk_username);
        parcel.writeInt(jumlah);
        parcel.writeString(bukti);
        parcel.writeInt(status);
        parcel.writeString(created);
        parcel.writeString(updated);
    }
}
