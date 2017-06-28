package com.assignments.larra.popularmovies.utilities;

import android.util.Log;

import com.assignments.larra.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by larra on 20/06/2017.
 */

public final class MovieDBUtils {

    private static final String TAG = MovieDBUtils.class.getSimpleName();

    public static Movie[] getMoviesFromJson(String jsonMoviesResponse) throws JSONException{
        Movie[] movies;
        JSONObject moviesJson = new JSONObject(jsonMoviesResponse);
        final String OWM_RESULTS = "results";
        final String OWM_TITLE = "title";
        final String OWM_POSTER = "poster_path";
        final String OWM_SYNOPSIS = "overview";
        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_RATING = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";
        final String default_synopsis = "missing synopsis";

        if(moviesJson.has(OWM_MESSAGE_CODE)){
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);
            switch (errorCode){
                case HttpURLConnection.HTTP_OK:
                    Log.v(TAG, "message successfully received");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    Log.e(TAG, "http not found error");
                    return null;
                default:
                    Log.e(TAG, "server down error");
                    return null;
            }
        }
        JSONArray moviesJsonArray = moviesJson.getJSONArray(OWM_RESULTS);
        movies = new Movie[moviesJsonArray.length()];
        Log.v(TAG, "array size = " + moviesJsonArray.length());

        for(int i = 0; i < moviesJsonArray.length(); i++){

            JSONObject movieJson = moviesJsonArray.getJSONObject(i);
            String title = movieJson.getString(OWM_TITLE);
            String synopsis = default_synopsis ;

            if(! movieJson.getString(OWM_SYNOPSIS).equals(""))
                synopsis = movieJson.getString(OWM_SYNOPSIS);

            String posterPath =  null;
            if(!movieJson.isNull(OWM_POSTER))
                posterPath = movieJson.getString(OWM_POSTER).replace("\\/","/");

            double rating = movieJson.getDouble(OWM_RATING);
            String releaseDate = movieJson.getString(OWM_RELEASE_DATE);
            movies[i] = new Movie(title, synopsis, posterPath, rating, releaseDate);
        }
        return movies;
    }
}
