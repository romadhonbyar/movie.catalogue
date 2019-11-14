package io.github.romadhonbyar.movie.ui.favorite.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.ui.favorite.FavFragment;
import io.github.romadhonbyar.movie.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.movie.ui.favorite.db.Fav;
import io.github.romadhonbyar.movie.ui.movies.MovieDetailActivity;
import io.github.romadhonbyar.movie.ui.tv_shows.TVShowDetailActivity;

import static io.github.romadhonbyar.movie.api.Global.PathImage;


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private ArrayList<Fav> daftarFav;
    private AppDatabase db;
    private Context mCtx;

    public FavAdapter(ArrayList<Fav> daftarFav, Context mCtx) {
        this.daftarFav = daftarFav;
        this.mCtx = mCtx;
        db = Room.databaseBuilder(
                mCtx.getApplicationContext(),
                AppDatabase.class, "favorite_db").allowMainThreadQueries().build();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView item;
        private ImageView imgPhoto;
        private TextView Desc, Nama;

        ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            Nama = itemView.findViewById(R.id.tv_item_name);
            Desc = itemView.findViewById(R.id.tv_item_desc);
            item = itemView.findViewById(R.id.main_card_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String getKode = daftarFav.get(position).getCode();
        final String getNama = daftarFav.get(position).getName();
        final String getDesc = daftarFav.get(position).getDesc();
        final String getType = daftarFav.get(position).getType();
        final String getPath_img = daftarFav.get(position).getPath_img();

        holder.Nama.setText(getNama);

        if (!getDesc.isEmpty()) {
            holder.Desc.setText(getDesc);
        } else {
            holder.Desc.setText(Html.fromHtml("<i>Null</i>"));
        }

        Glide.with(holder.itemView.getContext())
                .load(PathImage + getPath_img)
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(holder.imgPhoto);

        holder.item.setOnLongClickListener(v -> {
            String detail = v.getContext().getResources().getString(R.string.detail);
            String delete = v.getContext().getResources().getString(R.string.delete);
            String option = v.getContext().getResources().getString(R.string.option);

            CharSequence[] menuPilihan = {detail, delete};
            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(option)
                    .setItems(menuPilihan, (dialog1, which) -> {
                        switch (which) {
                            case 0:
                                onDetailData(getKode, getType);
                                break;
                            case 1:
                                onDeleteData(position);
                                break;
                        }
                    });
            dialog.create();
            dialog.show();
            return true;
        });
    }

    private void onDetailData(String getKode, String getType) {
        if (getType.equals("movie")) {
            Intent dd = new Intent(mCtx.getApplicationContext(), MovieDetailActivity.class);
            dd.putExtra("id_movie", getKode);
            dd.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.getApplicationContext().startActivity(dd);
        } else {
            Intent dd = new Intent(mCtx.getApplicationContext(), TVShowDetailActivity.class);
            dd.putExtra("id_tv_show", getKode);
            dd.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.getApplicationContext().startActivity(dd);
        }
    }

    private void onDeleteData(int position) {
        db.favDAO().deleteFav(daftarFav.get(position));
        daftarFav.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, daftarFav.size());
        getFragmentPage(new FavFragment());

        Toast.makeText(mCtx, R.string.mes_delete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return daftarFav.size();
    }

    private void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            ((FragmentActivity) mCtx).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
        }
    }
}