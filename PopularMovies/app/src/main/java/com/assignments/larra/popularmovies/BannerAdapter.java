package com.assignments.larra.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.assignments.larra.popularmovies.data.Movie;
import com.assignments.larra.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by larra on 17/06/2017.
 */

class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private Movie[] mMovieData;

    private final BannerAdapterOnClickHandler mClickHandler;

    interface BannerAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    public BannerAdapter(BannerAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        Context context = holder.mBannerView.getContext();
        Movie movie = mMovieData[position];
        if(movie.getBannerPath() != null){
            Uri imagePath = NetworkUtils.buildImageUri(movie.getBannerPath(), false);
            Picasso.with(context).load(imagePath).resize(500,750).into(holder.mBannerView);
        }
        else{
            Picasso.with(context).load(R.drawable.notfound).resize(500,750).into(holder.mBannerView);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieData == null ? 0 : mMovieData.length;
    }


    class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mBannerView;
        BannerViewHolder(View itemView) {
            super(itemView);
            mBannerView = (ImageView) itemView.findViewById(R.id.iv_banner);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = mMovieData[position];
            mClickHandler.onClick(movie);
        }
    }

    void setMoviesData(Movie []moviesData){
        mMovieData = moviesData;
        notifyDataSetChanged();
    }
}
