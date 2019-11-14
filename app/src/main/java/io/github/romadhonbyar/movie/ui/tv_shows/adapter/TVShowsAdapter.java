package io.github.romadhonbyar.movie.ui.tv_shows.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Html;
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


import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.movie.ui.favorite.db.Fav;
import io.github.romadhonbyar.movie.ui.tv_shows.TVShowDetailActivity;
import io.github.romadhonbyar.movie.ui.tv_shows.TVShowsFragment;
import io.github.romadhonbyar.movie.ui.tv_shows.model.main.TVShowsModelResults;

import static io.github.romadhonbyar.movie.api.Global.PathImage;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.RvViewHolder> implements Filterable {
    private Context mCtx;
    private List<TVShowsModelResults> pList;
    private List<TVShowsModelResults> filteredpList;
    private AppDatabase db;
    private Cursor res;

    public TVShowsAdapter(Context mCtx, List<TVShowsModelResults> pList) {
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
        TVShowsModelResults tv = filteredpList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(PathImage + tv.getPosterPath())
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(holder.imgPhoto);

        res = db.favDAO().count(tv.getId().toString());
        if (res.getCount() == 1) {
            holder.tvName.setText("❤ " + tv.getOriginalName());
        } else {
            holder.tvName.setText(tv.getOriginalName());
        }

        if (!tv.getOverview().isEmpty()) {
            holder.tvDesc.setText(tv.getOverview());
        } else {
            holder.tvDesc.setText(Html.fromHtml("<i>Null</i>"));
        }

        holder.item.setOnLongClickListener(v -> {
            res = db.favDAO().count(tv.getId().toString());
            if (res.getCount() == 1) {
                onDeleteData(tv.getId().toString());
                getFragmentPage(new TVShowsFragment());
            } else {
                Fav add = new Fav();
                add.setCode(tv.getId().toString());
                add.setName(tv.getOriginalName());
                add.setDesc(tv.getOverview());
                add.setType("tv");
                add.setPath_img(tv.getPosterPath());

                insertData(add);
                getFragmentPage(new TVShowsFragment());
            }
            return true;
        });

        holder.item.setOnClickListener(arg0 -> {
            Intent dd = new Intent(mCtx.getApplicationContext(), TVShowDetailActivity.class);
            dd.putExtra("id_tv_show", tv.getId().toString());

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

    private void onDeleteData(String id_tv_show) {
        db.favDAO().deleteByKode(id_tv_show);
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
                    List<TVShowsModelResults> filteredList = new ArrayList<>();
                    for (TVShowsModelResults myData : pList) {
                        if (myData.getOriginalName().toLowerCase().contains(charString) || myData.getOverview().toLowerCase().contains(charString) || myData.getName().toLowerCase().contains(charString)) {
                            filteredList.add(myData);
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
                filteredpList = (ArrayList<TVShowsModelResults>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}