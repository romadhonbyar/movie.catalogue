package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.List;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.R;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db.Fav;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.MovieDetailActivity;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.MoviesFragment;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.model.main.MoviesModelResults;

import static io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.api.Global.PathImage;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.RvViewHolder> implements Filterable {
    private Context mCtx;
    private List<MoviesModelResults> pList;
    private List<MoviesModelResults> filteredpList;
    private AppDatabase db;
    private Cursor res;

    public MoviesAdapter(Context mCtx, List<MoviesModelResults> pList) {
        this.mCtx = mCtx;
        this.pList = pList;
        this.filteredpList = pList;
        db = Room.databaseBuilder(
                mCtx.getApplicationContext(),
                AppDatabase.class, "favorite_db").allowMainThreadQueries().build();
    }

    @Override
    public int getItemCount() {
        return filteredpList.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDesc;
        private CardView item;

        RvViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            item = itemView.findViewById(R.id.main_card_view);
        }
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_item, viewGroup, false);
        return new RvViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RvViewHolder holder, int position) {
        MoviesModelResults mov = filteredpList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(PathImage + mov.getPosterPath())
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(holder.imgPhoto);

        res = db.favDAO().count(mov.getId().toString());
        if (res.getCount() == 1) {
            holder.tvName.setText("❤ " + mov.getOriginalTitle());
        } else {
            holder.tvName.setText(mov.getOriginalTitle());
        }

        if (!mov.getOverview().isEmpty()) {
            holder.tvDesc.setText(mov.getOverview());
        } else {
            holder.tvDesc.setText(Html.fromHtml("<i>Null</i>"));
        }

        holder.item.setOnLongClickListener(v -> {
            res = db.favDAO().count(mov.getId().toString());
            if (res.getCount() == 1) {
                onDeleteData(mov.getId().toString());
                getFragmentPage(new MoviesFragment());
            } else {
                Fav add = new Fav();
                add.setKode(mov.getId().toString());
                add.setNama(mov.getOriginalTitle());
                add.setDesc(mov.getOverview());
                add.setType("movie");
                add.setPath_img(mov.getPosterPath());

                insertData(add);
                getFragmentPage(new MoviesFragment());
            }
            return true;
        });

        holder.item.setOnClickListener(arg0 -> {
            Intent dd = new Intent(mCtx.getApplicationContext(), MovieDetailActivity.class);
            dd.putExtra("id_movie", mov.getId().toString());

            dd.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.getApplicationContext().startActivity(dd);
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void insertData(final Fav fav) {

        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return db.favDAO().insertFav(fav);
            }

            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(mCtx, R.string.mes_add, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private void onDeleteData(String id_movie) {
        db.favDAO().deleteByKode(id_movie);
        Toast.makeText(mCtx, R.string.mes_delete, Toast.LENGTH_SHORT).show();
    }

    private void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            ((FragmentActivity) mCtx).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
        }
    }



    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredpList = pList;
                } else {

                    List<MoviesModelResults> filteredList = new ArrayList<>();

                    for (MoviesModelResults androidVersion : pList) {

                        if (androidVersion.getOriginalTitle().toLowerCase().contains(charString) || androidVersion.getOverview().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    filteredpList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredpList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredpList = (ArrayList<MoviesModelResults>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}