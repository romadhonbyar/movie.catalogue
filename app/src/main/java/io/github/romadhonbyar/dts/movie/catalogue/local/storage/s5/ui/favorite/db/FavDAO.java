package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFav(Fav fav);

    @Query("SELECT * FROM tFav")
    Fav[] readDataFav();

    @Delete
    void deleteFav(Fav fav);

    @Query("DELETE FROM tFav WHERE kode_favorite = :kode")
    void deleteByKode(String kode);

    @Query("SELECT * FROM tFav WHERE kode_favorite = :kode LIMIT 1")
    Cursor count(String kode);

    @Query("SELECT * FROM tFav WHERE kode_favorite = :kode LIMIT 1")
    Fav selectDetailFav(String kode);
}
