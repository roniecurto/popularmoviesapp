package com.example.simplepopularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private final static String KEY_PARAM = "api_key";
    private final static String API_KEY = "change_api";

    /**
     * Builds the URL.
     *
     * @param givenURL url to be parsed.
     * @return The URL to use to query the moviedb server.
     */
    public static URL buildUrl(String givenURL) {
        Uri builtUri = Uri.parse(givenURL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();

        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Builds the URL.
     *
     * @param givenURL url to be parsed.
     * @return The URL to use to query the moviedb server.
     */
    public static URL buildUrlAdditionalInfo(String givenURL, String movie_id, String infoType) {
        Uri builtUri = Uri.parse(givenURL).buildUpon()
            .appendPath(movie_id)
            .appendPath(infoType)
            .appendQueryParameter(KEY_PARAM, API_KEY)
            .build();
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
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
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
