package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.R;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.helper.LanguageActivity;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.adapter.FavAdapter;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.db.Fav;


public class FavFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private AppDatabase db;
    private ArrayList<Fav> daftarFav;
    private TextView emptyView;

    public FavFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_fav, container, false);

        Objects.requireNonNull(getActivity()).setTitle(R.string.favorite);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        daftarFav = new ArrayList<>();

        db = Room.databaseBuilder(Objects.requireNonNull(Objects.requireNonNull(getActivity()).getApplicationContext()),
                AppDatabase.class, "favorite_db").allowMainThreadQueries().build();

        ProgressBar pLoad = Objects.requireNonNull(getActivity()).findViewById(R.id.pbLoading);

        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rv_list_data);
        emptyView = getView().findViewById(R.id.empty_view);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            daftarFav.addAll(Arrays.asList(db.favDAO().readDataFav()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new FavAdapter(daftarFav, Objects.requireNonNull(getActivity()));
            recyclerView.setAdapter(adapter);

            if (adapter.getItemCount() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            pLoad.setVisibility(View.GONE);
        }, 1000);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(getActivity(), LanguageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.action_favorite);
        registrar.setVisible(false);
    }
}