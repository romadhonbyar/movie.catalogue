package io.github.romadhonbyar.movie.provider;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "io.github.romadhonbyar.movie";

    private DatabaseContract(){}

    public static final class NoteColumns implements BaseColumns {
        public static final String TABLE_NAME = "tFav";
    }
}
