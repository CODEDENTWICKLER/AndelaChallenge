package com.example.andelachallenge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by codedentwickler on 3/10/17.
 */

public final class Utils {

    private static final String ACCESS_TOKEN = "c5fe92724414f458e9536b18d1b12a41f6e38bbc";
    private static final String ACCESS_TOKEN_PARAMS = "access_token";
    public static final String PAGE_NO_PARAMS = "page";
    private static final String SIZE_PER_PAGE_PARAMS = "per_page";
    public static final String SIZE_PER_PAGE = "100";

    public static final String GITHUB_QUERY_URL =
            "https://api.github.com/search/users?q=language:java+location:lagos";

    public static final String GITHUB_QUERY_URL_WITH_PAGINATION =
            Uri.parse(GITHUB_QUERY_URL).buildUpon().
                    appendQueryParameter(SIZE_PER_PAGE_PARAMS, SIZE_PER_PAGE).toString();


    public static final String GITHUB_QUERY_URL_WITH_ACCESS_TOKEN =
            Uri.parse(GITHUB_QUERY_URL_WITH_PAGINATION).buildUpon().
                    appendQueryParameter(ACCESS_TOKEN_PARAMS, ACCESS_TOKEN).toString();

    public static final String GITHUB_PROFILE_URL_KEY = "html_url";
    public static final String GITHUB_USERNAME_KEY = "login";

    public static final String GITHUB_IMAGE_URL_KEY = "avatar_url";
    private static final String LOG_TAG = "Utilities Logger";


    public static void makeSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


//    private boolean isInternet(){
//
//        final Handler handler = new Handler();
//        final boolean[] isRunning = {true};
//        final boolean[] success = {false};
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isRunning[0]) {
//                    try {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                success[0] = isConnected(mRecyclerView.getContext());
//                                isRunning[0] = false;
//                            }
//                        });
//                    }
//                    catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//                }
//            }
//        }).start();
//        return success[0];
//    }

    public static boolean isConnected(Context context){
        if (isNetworkConnected(context)) {

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new
                        URL("https://clients3.github.com/generate204").openConnection();
                urlConnection.setRequestProperty("User-Agent","Android");
                urlConnection.setRequestProperty("Connection","close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();

                return (urlConnection.getResponseCode() == 204 && urlConnection.getContentLength() == 0);

            } catch (IOException e) {
                Log.e(LOG_TAG,"Error Checking internet connection");
            }

            }
        else {
            Log.d(LOG_TAG,"No Network Available");

        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService( CONNECTIVITY_SERVICE );

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;
        return false;
    }
}
