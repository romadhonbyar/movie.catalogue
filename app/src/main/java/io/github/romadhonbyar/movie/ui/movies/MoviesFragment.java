package io.github.romadhonbyar.movie.ui.movies;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.api.RetrofitClient;
import io.github.romadhonbyar.movie.helper.SharedPrefManager;
import io.github.romadhonbyar.movie.ui.movies.adapter.MoviesAdapter;
import io.github.romadhonbyar.movie.ui.movies.model.main.MoviesModel;
import io.github.romadhonbyar.movie.ui.movies.model.main.MoviesModelResults;
import io.github.romadhonbyar.movie.ui.alarm.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.romadhonbyar.movie.BuildConfig.API_KEY;


public class MoviesFragment extends Fragment {
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<MoviesModelResults> pList = new ArrayList<>();
    private SearchView searchView;
    private ProgressBar pLoad;
    private String lang;
    private TextView emptyView;
    private MenuItem search;

    public MoviesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_movies, container, false);

        Objects.requireNonNull(getActivity()).setTitle(R.string.movies);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MoviesAdapter(Objects.requireNonNull(getActivity()), pList);

        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rv_list_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pLoad = Objects.requireNonNull(getActivity()).findViewById(R.id.pbLoading);
        emptyView = Objects.requireNonNull(getActivity()).findViewById(R.id.empty_view);
        lang = SharedPrefManager.getInstance(getActivity()).getLang();

        if (savedInstanceState != null) {
            if (getArrayList() != null) {
                List<MoviesModelResults> all = new Gson().fromJson(getArrayList(), new TypeToken<List<MoviesModelResults>>() {
                }.getType());

                recyclerView.setAdapter(new MoviesAdapter(Objects.requireNonNull(getContext()), all));
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
                    List<MoviesModelResults> all = new Gson().fromJson(getArrayList(), new TypeToken<List<MoviesModelResults>>() {
                    }.getType());

                    recyclerView.setAdapter(new MoviesAdapter(Objects.requireNonNull(getContext()), all));
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                }
            }
        }

        emptyView.setOnClickListener(v -> getFragmentPage(new MoviesFragment()));
    }

    private void loadData(String language, ProgressBar pLoad) {

        RetrofitClient.getInstance().getApi().getMovieList(API_KEY, language).enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(@NonNull Call<MoviesModel> call, @NonNull Response<MoviesModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final List<MoviesModelResults> all = Objects.requireNonNull(response.body()).getResults();

                    saveArrayList(all);

                    adapter = new MoviesAdapter(Objects.requireNonNull(getContext()), all);
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
            public void onFailure(@NonNull Call<MoviesModel> call, @NonNull Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                pLoad.setVisibility(View.GONE);
            }
        });
    }

    private void loadDataSearch(String language, ProgressBar pLoad, String query) {

        RetrofitClient.getInstance().getApi().getMovieSearch(API_KEY, language, query).enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(@NonNull Call<MoviesModel> call, @NonNull Response<MoviesModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final List<MoviesModelResults> all = Objects.requireNonNull(response.body()).getResults();

                    saveArrayList(all);

                    adapter = new MoviesAdapter(Objects.requireNonNull(getContext()), all);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    if (response.body().getTotalResults() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.mes_no_data, Toast.LENGTH_LONG).show();
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
                    }
                    pLoad.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    pLoad.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesModel> call, @NonNull Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                pLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                pLoad.setVisibility(View.VISIBLE);
                if (Objects.equals(lang, "in")) {
                    loadDataSearch("id-ID", pLoad, query);
                } else {
                    loadDataSearch("en-US", pLoad, query);
                }

                hideKeyboard(searchView);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("Sayang: ", newText);
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

    private void saveArrayList(List<MoviesModelResults> list) {
        String httpParamJSONList = new Gson().toJson(list);

        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("your_prefes_movie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("your_prefes_movie", httpParamJSONList);

        editor.apply();
    }

    private String getArrayList() {
        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("your_prefes_movie", Context.MODE_PRIVATE);
        return prefs.getString("your_prefes_movie", null);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            ((FragmentActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
        }
    }
}