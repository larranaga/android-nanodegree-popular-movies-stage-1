package com.assignments.larra.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.assignments.larra.popularmovies.data.Movie;
import com.assignments.larra.popularmovies.utilities.Constants;
import com.assignments.larra.popularmovies.utilities.MovieDBUtils;
import com.assignments.larra.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements BannerAdapter.BannerAdapterOnClickHandler{
    RecyclerView mRecyclerView;
    private BannerAdapter mBannerAdapter;
    TextView mErrorMessageDisplay;
    ProgressBar mLoadingIndicator;
    private boolean order_by_popularity;


    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int NUMBER_OF_COLS = 2;
        order_by_popularity = true;
        RecyclerView.LayoutManager mLayoutManager;

        setContentView(R.layout.movie_grid_layout);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);
        mLayoutManager = new GridLayoutManager(this,NUMBER_OF_COLS);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mBannerAdapter = new BannerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mBannerAdapter);

        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_order_by_popularity){
            order_by_popularity = true;
            loadMovieData();
        }
        else if(id == R.id.action_order_by_rating){
            order_by_popularity = false;
            loadMovieData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData() {
        showMovieView();

        new FetchMoviesTask().execute(order_by_popularity);
    }

    private void showMovieView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class FetchMoviesTask extends AsyncTask<Boolean,Void,Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

        @Override
        protected Movie[] doInBackground(Boolean... params) {
            if(params.length == 0)
                return null;
            boolean sortByPopularity = params[0];

            URL moviesRequestURl = NetworkUtils.buildURL(sortByPopularity);
            if(!isOnline())
                return null;
            try{
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestURl);
                return MovieDBUtils.getMoviesFromJson(jsonMoviesResponse);

            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null) {
                showMovieView();
                mBannerAdapter.setMoviesData(movies);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
