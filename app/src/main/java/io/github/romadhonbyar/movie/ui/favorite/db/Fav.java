package io.github.romadhonbyar.movie.ui.favorite.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tFav", indices = @Index(value = {"code_favorite"}, unique = true))
public class Fav implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "code_favorite")
    private String code;

    @ColumnInfo(name = "name_favorite")
    private String name;

    @ColumnInfo(name = "desc_favorite")
    private String desc;

    @ColumnInfo(name = "type_favorite")
    private String type;

    @ColumnInfo(name = "path_favorite")
    private String path_img;

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
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
