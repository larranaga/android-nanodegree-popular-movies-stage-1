package com.assignments.larra.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignments.larra.popularmovies.data.Movie;
import com.assignments.larra.popularmovies.utilities.Constants;
import com.assignments.larra.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    CardView mBannerCardView;
    ImageView mMovieBannerView;
    TextView mMovieTitleView;
    TextView mMovieRatingView;
    CardView mDateCardView;
    TextView mMovieDateView;
    CardView mSynopsisCardView;
    TextView mMovieSynopsisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        final boolean getBigSize = true;

        mBannerCardView = (CardView) findViewById(R.id.cv_detail);
        mSynopsisCardView = (CardView) findViewById(R.id.cv_synopsis);
        mDateCardView = (CardView) findViewById(R.id.cv_date);
        mMovieBannerView = (ImageView) mBannerCardView.findViewById(R.id.iv_movie_banner);
        mMovieTitleView = (TextView) mBannerCardView.findViewById(R.id.tv_movie_title);
        mMovieRatingView = (TextView) mBannerCardView.findViewById(R.id.tv_movie_rating);
        mMovieSynopsisView = (TextView) mSynopsisCardView.findViewById(R.id.tv_synopsis);
        mMovieDateView = (TextView) mDateCardView.findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra(Constants.EXTRA_MOVIE)){
                Movie movie =  intent.getParcelableExtra(Constants.EXTRA_MOVIE);

                if(movie.getBannerPath() != null){
                    Uri imagePath = NetworkUtils.buildImageUri(movie.getBannerPath(),getBigSize);
                    Picasso.with(this).load(imagePath).into(mMovieBannerView);
                }
                else{
                    Picasso.with(this).load(R.drawable.notfound).into(mMovieBannerView);
                }
                mMovieTitleView.setText(movie.getTitle());
                String displayDate = "Release date " + movie.getDate();
                mMovieDateView.setText(displayDate);
                mMovieRatingView.setText(String.valueOf(movie.getRating()));
                mMovieSynopsisView.setText(movie.getSynopsis());
            }
        }

    }
}
