package io.github.romadhonbyar.movie.provider;

import android.database.Cursor;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "io.github.romadhonbyar.movie";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class NoteColumns implements BaseColumns {
        public static final String TABLE_NAME = "tFav";
    }
}
