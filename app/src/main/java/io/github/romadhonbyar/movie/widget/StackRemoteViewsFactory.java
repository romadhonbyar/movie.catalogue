package io.github.romadhonbyar.movie.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.movie.ui.favorite.db.Fav;

import static io.github.romadhonbyar.movie.api.Global.PathImage;


class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AppDatabase db;
    private List<Fav> daftarFav = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        //required
    }

    @Override
    public void onDataSetChanged() {
        db = Room.databaseBuilder(mContext, AppDatabase.class, "favorite_db").allowMainThreadQueries().build();
        daftarFav = Arrays.asList(db.favDAO().readDataFav());
    }

    @Override
    public void onDestroy() {
        //required
    }

    @Override
    public int getCount() {
        return daftarFav.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.favorite_widget_item);

        Bitmap myDataBMP = null;
        try {
            myDataBMP = Glide.with(mContext)
                    .asBitmap()
                    .load(PathImage + daftarFav.get(position).getPath_img())
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .centerCrop())
                    .submit()
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, myDataBMP);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        extras.putString(FavoriteWidget.EXTRA_NAME, daftarFav.get(position).getName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}