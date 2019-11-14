package io.github.romadhonbyar.cusapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Objects;

import io.github.romadhonbyar.cusapp.CustomOnItemClickListener;
import io.github.romadhonbyar.cusapp.R;
import io.github.romadhonbyar.cusapp.entity.Fav;

import static io.github.romadhonbyar.cusapp.Global.PathImage;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.NoteViewHolder> {
    private final ArrayList<Fav> listNotes = new ArrayList<>();
    private final Activity activity;

    public FavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Fav> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Fav> listNotes) {
        if (listNotes.size() > 0) {
            this.listNotes.clear();
        }
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvTitle.setText(listNotes.get(position).getName());
        holder.tvDescription.setText(listNotes.get(position).getDesc());
        Glide.with(activity)
                .load(PathImage + Objects.requireNonNull(listNotes).get(position).getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(holder.imgFav);
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription;
        final CardView cvFav;
        ImageView imgFav;

        NoteViewHolder(View itemView) {
            super(itemView);
            imgFav = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.tv_item_name);
            tvDescription = itemView.findViewById(R.id.tv_item_desc);
            cvFav = itemView.findViewById(R.id.main_card_view);
        }
    }
}