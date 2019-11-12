package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.R;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.api.RetrofitClient;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.helper.LanguageActivity;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.helper.SharedPrefManager;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.adapter.TVShowsAdapter;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.model.main.TVShowsModel;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.model.main.TVShowsModelResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.BuildConfig.API_KEY;

public class TVShowsFragment extends Fragment {
    private RecyclerView recyclerView;
    private TVShowsAdapter adapter;
    private List<TVShowsModelResults> pList = new ArrayList<>();

    public TVShowsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_tvshows, container, false);

        Objects.requireNonNull(getActivity()).setTitle(R.string.tv_shows);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TVShowsAdapter(Objects.requireNonNull(getActivity()), pList);

        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rv_list_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProgressBar pLoad = Objects.requireNonNull(getActivity()).findViewById(R.id.pbLoading);

        String lang = SharedPrefManager.getInstance(getActivity()).getLang();

        if (savedInstanceState != null) {
            if (getArrayList() != null) {
                List<TVShowsModelResults> all = new Gson().fromJson(getArrayList(), new TypeToken<List<TVShowsModelResults>>() {
                }.getType());

                recyclerView.setAdapter(new TVShowsAdapter(Objects.requireNonNull(getContext()), all));
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
                pLoad.setVisibility(View.GONE);
            } else {
                Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_LONG).show();
                pLoad.setVisibility(View.GONE);
            }
        } else {
            if (isNetworkConnected()) {
                if (Objects.equals(lang, "in")) {
                    loadData("id-ID", pLoad);
                } else {
                    loadData("en-US", pLoad);
                }
            } else {
                if (getArrayList() != null) {
                    List<TVShowsModelResults> all = new Gson().fromJson(getArrayList(), new TypeToken<List<TVShowsModelResults>>() {
                    }.getType());

                    recyclerView.setAdapter(new TVShowsAdapter(Objects.requireNonNull(getContext()), all));
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                }
            }
        }
    }

    private void loadData(String language, ProgressBar pLoad) {

        RetrofitClient.getInstance().getApi().getTVList(API_KEY, language).enqueue(new Callback<TVShowsModel>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsModel> call, @NonNull Response<TVShowsModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final List<TVShowsModelResults> all = Objects.requireNonNull(response.body()).getResults();

                    saveArrayList(all);

                    adapter = new TVShowsAdapter(Objects.requireNonNull(getContext()), all);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsModel> call, @NonNull Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                pLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

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

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.action_favorite);
        registrar.setVisible(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

    private void saveArrayList(List<TVShowsModelResults> list) {
        String httpParamJSONList = new Gson().toJson(list);

        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("your_prefes_tv", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("your_prefes_tv", httpParamJSONList);

        editor.apply();
    }

    private String getArrayList() {
        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("your_prefes_tv", Context.MODE_PRIVATE);
        return prefs.getString("your_prefes_tv", null);
    }
}