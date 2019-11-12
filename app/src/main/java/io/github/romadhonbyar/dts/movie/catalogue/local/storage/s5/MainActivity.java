package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.favorite.FavFragment;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.movies.MoviesFragment;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.TVShowsFragment;


public class MainActivity extends AppCompatActivity {
    private MoviesFragment MyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            String SIMPLE_FRAGMENT_TAG = "My_Fragment_Tag";
            MyFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_TAG);
        } else if (MyFragment == null) {
            getFragmentPage(new MoviesFragment());
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.movies:
                    fragment = new MoviesFragment();
                    break;

                case R.id.tvshows:
                    fragment = new TVShowsFragment();
                    break;

                case R.id.favorite:
                    fragment = new FavFragment();
                    break;
            }
            return getFragmentPage(fragment);
        });

        setTitle(getString(R.string.app_name));
    }

    private boolean getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}