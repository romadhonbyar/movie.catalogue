package io.github.romadhonbyar.movie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.Objects;

import io.github.romadhonbyar.movie.ui.favorite.db.AppDatabase;

import static io.github.romadhonbyar.movie.provider.DatabaseContract.AUTHORITY;
import static io.github.romadhonbyar.movie.provider.DatabaseContract.NoteColumns.TABLE_NAME;

public class FavorieProvider extends ContentProvider {

    private static final int FAV = 1;
    private static final int FAV_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                FAV_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        if (sUriMatcher.match(uri) == FAV) {
            AppDatabase db = Room.databaseBuilder(Objects.requireNonNull(getContext()), AppDatabase.class, "favorite_db").build();
            cursor = db.favDAO().readAllDataFav();
        } else {
            cursor = null;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
