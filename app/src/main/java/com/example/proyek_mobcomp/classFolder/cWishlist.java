package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cWishlist implements Parcelable {
    protected int id;
    protected String fk_user;
    protected int fk_barang;

    public cWishlist(int id, String fk_user, int fk_barang) {
        this.id = id;
        this.fk_user = fk_user;
        this.fk_barang = fk_barang;
    }

    protected cWishlist(Parcel in) {
        id = in.readInt();
        fk_user = in.readString();
        fk_barang = in.readInt();
    }

    public static final Creator<cWishlist> CREATOR = new Creator<cWishlist>() {
        @Override
        public cWishlist createFromParcel(Parcel in) {
            return new cWishlist(in);
        }

        @Override
        public cWishlist[] newArray(int size) {
            return new cWishlist[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFk_user() {
        return fk_user;
    }

    public void setFk_user(String fk_user) {
        this.fk_user = fk_user;
    }

    public int getFk_barang() {
        return fk_barang;
    }

    public void setFk_barang(int fk_barang) {
        this.fk_barang = fk_barang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fk_user);
        dest.writeInt(fk_barang);
    }
}
