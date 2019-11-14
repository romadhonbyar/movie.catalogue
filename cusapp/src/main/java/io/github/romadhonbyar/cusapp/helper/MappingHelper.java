package io.github.romadhonbyar.cusapp.helper;

import android.database.Cursor;

import java.util.ArrayList;

import io.github.romadhonbyar.cusapp.db.DatabaseContract;
import io.github.romadhonbyar.cusapp.entity.Fav;

import static io.github.romadhonbyar.cusapp.db.DatabaseContract.FavColumns.CODE;
import static io.github.romadhonbyar.cusapp.db.DatabaseContract.FavColumns.DESC;
import static io.github.romadhonbyar.cusapp.db.DatabaseContract.FavColumns.NAME;
import static io.github.romadhonbyar.cusapp.db.DatabaseContract.FavColumns.PATH;

public class MappingHelper {

    public static ArrayList<Fav> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Fav> favList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            String code = notesCursor.getString(notesCursor.getColumnIndexOrThrow(CODE));
            String name = notesCursor.getString(notesCursor.getColumnIndexOrThrow(NAME));
            String desc = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESC));
            String path = notesCursor.getString(notesCursor.getColumnIndexOrThrow(PATH));
            favList.add(new Fav(code, name, desc, path));
        }
        return favList;
    }
}