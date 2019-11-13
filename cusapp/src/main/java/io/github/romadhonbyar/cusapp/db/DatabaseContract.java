package io.github.romadhonbyar.cusapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    private static final String AUTHORITY = "io.github.romadhonbyar.movie";
    private static final String SCHEME = "content";

    public static final class FavColumns implements BaseColumns {
        public static final String TABLE_NAME = "tFav";
        public static final String CODE = "code_favorite";
        public static final String NAME = "name_favorite";
        public static final String DESC = "desc_favorite";
        public static final String PATH = "path_favorite";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}