package com.example.andelachallenge;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by codedentwickler on 3/4/17.
 */

public class AppController extends Application {


    private static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private RequestQueue getRequestQueue(){

        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <E> void addToRequestQueue(Request<E> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <E> void addToRequestQueue(Request<E> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelAppPendingRequest(String tag){
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }

}
