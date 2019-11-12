package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tFav", indices = @Index(value = {"kode_favorite"}, unique = true))
public class Fav implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "kode_favorite")
    private String kode;

    @ColumnInfo(name = "nama_favorite")
    private String nama;

    @ColumnInfo(name = "desc_favorite")
    private String desc;

    @ColumnInfo(name = "type_favorite")
    private String type;

    @ColumnInfo(name = "path_favorite")
    private String path_img;

    @NonNull
    public String getKode() {
        return kode;
    }

    public void setKode(@NonNull String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath_img() {
        return path_img;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }
}
