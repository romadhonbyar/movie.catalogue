package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.R;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
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
        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_01_darth_vader));
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_02_falcon));
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_03_star_wars_logo));
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_04_starwars));
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_05_storm_trooper));
    }

    @Override
    public void onDestroy() {
        //required
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.favorite_widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
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