package com.assignments.larra.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by larra on 17/06/2017.
 */

public  final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String SCHEME = "https";
    private static final String MOVIES_DB_AUTHORITY = "api.themoviedb.org";
    private static final String IMAGES_DB_AUTHORITY = "image.tmdb.org";
    private static final String VERSION_PATH = "3";
    private static final String DISCOVER_PATH = "discover";
    private static final String MOVIE_PATH = "movie";
    private static final String POSTER_PATH = "p";
    private static final String TARGET_PATH = "t";
    private static final String KEY_PARAM = "api_key";
    private static final String SORT_PARAM = "sort_by";
    private static final String PAGE_PARAM = "page";
    private static final String popularity = "popularity.desc";
    private static final String rating = "vote_average.desc";
    private static final String IMAGE_SIZE_PATH_SMALL = "w342";
    private static final String IMAGE_SIZE_PATH_BIG = "w500";

    private static final String api_key = "API_KEY";

    public static URL buildURL(boolean byPopular){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(MOVIES_DB_AUTHORITY)
                .path(VERSION_PATH)
                .appendPath(DISCOVER_PATH)
                .appendPath(MOVIE_PATH)
                .appendQueryParameter(KEY_PARAM, api_key)
                .appendQueryParameter(SORT_PARAM, (byPopular ? popularity : rating));


        Uri builtUri = builder.build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "built URL to fetch movies " + url);
        return url;
    }

    public static Uri buildImageUri(String imagePath, boolean getBigSize){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(IMAGES_DB_AUTHORITY)
                .path(TARGET_PATH)
                .appendEncodedPath(POSTER_PATH)
                .appendEncodedPath(getBigSize ? IMAGE_SIZE_PATH_BIG : IMAGE_SIZE_PATH_SMALL)
                .appendEncodedPath(imagePath);
        Log.v(TAG, "image URI to fetch posters " + builder.build().toString());
        return builder.build();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            String result;
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                result = scanner.next();
                //Log.v(TAG, "api response: " + result);
                return result;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
