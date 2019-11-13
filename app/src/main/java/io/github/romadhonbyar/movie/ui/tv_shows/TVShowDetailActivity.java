package io.github.romadhonbyar.movie.ui.tv_shows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.api.RetrofitClient;
import io.github.romadhonbyar.movie.helper.SharedPrefManager;
import io.github.romadhonbyar.movie.ui.favorite.db.AppDatabase;
import io.github.romadhonbyar.movie.ui.favorite.db.Fav;
import io.github.romadhonbyar.movie.ui.other.SettingsActivity;
import io.github.romadhonbyar.movie.ui.tv_shows.model.detail.TVShowsDetailModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.romadhonbyar.movie.BuildConfig.API_KEY;
import static io.github.romadhonbyar.movie.api.Global.PathImage;
import static io.github.romadhonbyar.movie.helper.FormatData.minuteToFullTime;


public class TVShowDetailActivity extends AppCompatActivity {
    Toolbar mToolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    private AppDatabase db;
    private Menu menu;
    Cursor res;
    Fav fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_show_01_scrolling_main);

        db = Room.databaseBuilder(Objects.requireNonNull(getApplicationContext()),
                AppDatabase.class, "favorite_db").allowMainThreadQueries().build();

        fab = findViewById(R.id.fab);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String id_tv_show = (String) b.get("id_tv_show");
            ProgressBar pLoad = Objects.requireNonNull(this).findViewById(R.id.progressBar);

            String lang = SharedPrefManager.getInstance(this).getLang();

            if (isNetworkConnected()) {
                if (Objects.equals(lang, "in")) {
                    loadData(id_tv_show, "id-ID", pLoad);
                } else {
                    loadData(id_tv_show, "en-US", pLoad);
                }
            } else {
                Toast.makeText(TVShowDetailActivity.this, R.string.connect, Toast.LENGTH_LONG).show();
            }

            res = db.favDAO().count(id_tv_show);
            fav = db.favDAO().selectDetailFav(id_tv_show);
            if (res.getCount() == 1) {
                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_white_border_24dp);
            }
        }

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        collapsingToolbarLayout = findViewById(R.id.mycoll);
        collapsingToolbarLayout.animate();
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.secondaryTextColor));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.secondaryTextColor));

        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption();
                } else if (isShow) {
                    isShow = false;
                    hideOption();
                }
            }
        });
    }

    private void hideOption() {
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setVisible(false);
    }

    private void showOption() {
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        hideOption();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(TVShowDetailActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    private void loadData(String id_tv_show, String language, ProgressBar pLoad) {
        View include_tv = findViewById(R.id.include_tv);

        RetrofitClient.getInstance().getApi().getTVDetailList(id_tv_show, API_KEY, language).enqueue(new Callback<TVShowsDetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<TVShowsDetailModel> call, @NonNull Response<TVShowsDetailModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final TVShowsDetailModel data = response.body();

                    ImageView imgDataPhoto = findViewById(R.id.img_poster);
                    TextView tvDataDescription = findViewById(R.id.tv_data_description);
                    TextView tvStatus = findViewById(R.id.tv_data_status);
                    TextView tvLanguage = findViewById(R.id.tv_data_language);
                    TextView tvRuntime = findViewById(R.id.tv_data_runtime);
                    TextView tvGenres = findViewById(R.id.tv_data_genres);

                    Glide.with(getBaseContext())
                            .load(PathImage + Objects.requireNonNull(data).getPosterPath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(imgDataPhoto);

                    collapsingToolbarLayout.setTitle(data.getOriginalName());
                    mToolbar.setSubtitle(data.getStatus());

                    if (!data.getOverview().isEmpty()) {
                        tvDataDescription.setText(data.getOverview());
                    } else {
                        tvDataDescription.setText(Html.fromHtml("<i>Null</i>"));
                    }
                    tvStatus.setText(data.getStatus());
                    tvLanguage.setText(data.getOriginalLanguage());

                    ArrayList<String> runTime = new ArrayList<>();
                    for (int a = 0; a < data.getEpisodeRunTime().size(); a++) {
                        runTime.add(minuteToFullTime(data.getEpisodeRunTime().get(a)));
                    }

                    String strRunTime = String.valueOf(runTime);
                    strRunTime = strRunTime.replaceAll("\\[", "").replaceAll("\\]", "");
                    tvRuntime.setText(strRunTime);

                    ArrayList<String> list1 = new ArrayList<>();
                    for (int a = 0; a < data.getGenres().size(); a++) {
                        list1.add(data.getGenres().get(a).getName());
                    }

                    String strGen = String.valueOf(list1);
                    strGen = strGen.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("&", "&amp;");
                    tvGenres.setText(Html.fromHtml(strGen));

                    Toast.makeText(TVShowDetailActivity.this, R.string.success, Toast.LENGTH_LONG).show();

                    if (res.getCount() == 1) {
                        Log.e("Data", "APAX: " + res.getCount() + " - " + fav.getName());
                        // delete data
                        fab.setOnClickListener(view -> {
                            db.favDAO().deleteByKode(id_tv_show);

                            refresh();
                        });
                    } else {
                        Log.e("Data", "APAX: " + res.getCount());

                        // insert data
                        fab.setOnClickListener(view -> {
                            Fav add = new Fav();
                            add.setCode(data.getId().toString());
                            add.setName(data.getOriginalName());
                            add.setDesc(data.getOverview());
                            add.setType("tv");
                            add.setPath_img(data.getPosterPath());
                            insertData(add);

                            refresh();
                        });
                    }
                } else {
                    Toast.makeText(TVShowDetailActivity.this, R.string.failed, Toast.LENGTH_LONG).show();
                }
                pLoad.setVisibility(View.GONE);
                include_tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsDetailModel> call, @NonNull Throwable t) {
                Toast.makeText(TVShowDetailActivity.this, "ERROR > " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                pLoad.setVisibility(View.GONE);
                include_tv.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
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
                if (res.getCount() == 1) {
                    Toast.makeText(TVShowDetailActivity.this, R.string.mes_delete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TVShowDetailActivity.this, R.string.mes_add, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}