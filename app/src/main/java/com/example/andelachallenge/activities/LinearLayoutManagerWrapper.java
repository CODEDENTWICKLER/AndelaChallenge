package com.example.andelachallenge.activities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by codedentwickler on 3/8/17.
 */

public class LinearLayoutManagerWrapper extends LinearLayoutManager {

    private static final String TAG = "probe";

    public LinearLayoutManagerWrapper(Context context) {
        super(context, LinearLayoutManager.VERTICAL, false);

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }

        catch (IndexOutOfBoundsException e) {
            // I seriously don't know how to  Catch this Exception
            // So i'm just gonna go around it
            Log.e(TAG, "Met the IOOBE in Recycler View ");
        }

    }
}
