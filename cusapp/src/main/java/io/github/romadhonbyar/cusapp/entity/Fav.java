package io.github.romadhonbyar.cusapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Fav implements Parcelable {

    //private int id;
    private String code;
    private String name;
    private String desc;
    private String path;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getKode() {
        return code;
    }

    public void setKode(String kode) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.path);
    }

    //public Fav(int id, String kode, String name, String desc, String path) {
    public Fav(String kode, String name, String desc, String path) {
        //this.id = id;
        this.code = code;
        this.name = name;
        this.desc = desc;
        this.path = path;
    }

    private Fav(Parcel in) {
        //this.id = in.readInt();
        this.code = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.path = in.readString();
    }

    public static final Creator<Fav> CREATOR = new Creator<Fav>() {
        @Override
        public Fav createFromParcel(Parcel source) {
            return new Fav(source);
        }

        @Override
        public Fav[] newArray(int size) {
            return new Fav[size];
        }
    };
}