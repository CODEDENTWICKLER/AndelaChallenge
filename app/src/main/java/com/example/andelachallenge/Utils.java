package com.example.andelachallenge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by codedentwickler on 3/10/17.
 */

public final class Utils {

    public static final String ACCESS_TOKEN = "c5fe92724414f458e9536b18d1b12a41f6e38bbc";
    public static final String ACCESS_TOKEN_PARAMS = "access_token";
    public static final String PAGE_NO_PARAMS = "page";
    public static final String SIZE_PER_PAGE_PARAMS = "per_page";
    public static final String NO_OF_PAGES = "1";
    public static final String SIZE_PER_PAGE = "200";

    public static final String GITHUB_QUERY_URL =
            "https://api.github.com/search/users?q=language:java+location:lagos";

    public static final String GITHUB_QUERY_URL_WITH_PAGINATION =
            Uri.parse(GITHUB_QUERY_URL).buildUpon().
                    appendQueryParameter(PAGE_NO_PARAMS,NO_OF_PAGES).
                    appendQueryParameter(SIZE_PER_PAGE_PARAMS, SIZE_PER_PAGE).toString();


    public static final String GITHUB_QUERY_URL_WITH_ACCESS_TOKEN =
            Uri.parse(GITHUB_QUERY_URL_WITH_PAGINATION).buildUpon().
                    appendQueryParameter(ACCESS_TOKEN_PARAMS, ACCESS_TOKEN).toString();

    public static final String GITHUB_PROFILE_URL_KEY = "html_url";
    public static final String GITHUB_USERNAME_KEY = "login";

    public static final String GITHUB_IMAGE_URL_KEY = "avatar_url";


    public static void makeSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
