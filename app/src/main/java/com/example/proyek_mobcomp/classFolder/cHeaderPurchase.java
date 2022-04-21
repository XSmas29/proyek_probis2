package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cHeaderPurchase implements Parcelable {
    protected int id;
    protected String fk_customer;
    protected int grandtotal;
    protected String tanggal;

    public cHeaderPurchase(int id, String fk_customer, int grandtotal, String tanggal) {
        this.id = id;
        this.fk_customer = fk_customer;
        this.grandtotal = grandtotal;
        this.tanggal = tanggal;
    }

    protected cHeaderPurchase(Parcel in) {
        id = in.readInt();
        fk_customer = in.readString();
        grandtotal = in.readInt();
        tanggal = in.readString();
    }

    public static final Creator<cHeaderPurchase> CREATOR = new Creator<cHeaderPurchase>() {
        @Override
        public cHeaderPurchase createFromParcel(Parcel in) {
            return new cHeaderPurchase(in);
        }

        @Override
        public cHeaderPurchase[] newArray(int size) {
            return new cHeaderPurchase[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFk_customer() {
        return fk_customer;
    }

    public void setFk_customer(String fk_customer) {
        this.fk_customer = fk_customer;
    }

    public int getGrandtotal() {
        return grandtotal;
    }

    public String getGrandtotalInString(){
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(this.grandtotal);
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

    public void setGrandtotal(int grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fk_customer);
        dest.writeInt(grandtotal);
        dest.writeString(tanggal);
    }
}
