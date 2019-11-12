package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Fav.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavDAO favDAO();
}
