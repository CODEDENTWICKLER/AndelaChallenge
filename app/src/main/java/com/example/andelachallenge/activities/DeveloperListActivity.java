package com.example.andelachallenge.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andelachallenge.AppController;
import com.example.andelachallenge.R;
import com.example.andelachallenge.Utils;
import com.example.andelachallenge.Views.RecyclerViewEmptySupport;
import com.example.andelachallenge.adapter.DevelopersAdapter;
import com.example.andelachallenge.data.DeveloperContract.DeveloperEntry;
import com.example.andelachallenge.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.andelachallenge.Utils.GITHUB_IMAGE_URL_KEY;
import static com.example.andelachallenge.Utils.GITHUB_PROFILE_URL_KEY;
import static com.example.andelachallenge.Utils.GITHUB_QUERY_URL_WITH_PAGINATION;
import static com.example.andelachallenge.Utils.GITHUB_USERNAME_KEY;
import static com.example.andelachallenge.Utils.SIZE_PER_PAGE;
import static com.example.andelachallenge.Utils.isInternetAvailable;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DeveloperDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DeveloperListActivity extends AppCompatActivity {

    public static final String PERSIST_LIST_KEY = "key";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean mTwoPane;
    private ArrayList<Developer> mDeveloperList;
    RecyclerViewEmptySupport mRecyclerView;
    public Comparator<Developer> mComparator;
    private DevelopersAdapter mDeveloperAdapter;
    private static final String TAG = "TESTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setUpRecyclerView();

        notifyOffline();

        mDeveloperList = new ArrayList<>();

        mComparator = new Comparator<Developer>() {

            @Override
            public int compare(Developer dev1, Developer dev2) {
                return dev1.getUsername().compareTo(dev2.getUsername());
            }

        };
        mDeveloperAdapter = new DevelopersAdapter(this, mComparator);
        mRecyclerView.setAdapter(mDeveloperAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(PERSIST_LIST_KEY)) {
            mDeveloperList = savedInstanceState.getParcelableArrayList(PERSIST_LIST_KEY);
            Log.d(TAG, "Length of list is:" + mDeveloperList.size());
        } else {
            if (isInternetAvailable(this) && mDeveloperList.isEmpty()) {
                fetchDataAndInsertIntoDatabase(new ServerCallback() {
                    @Override
                    public void onSuccess() {
                        mDeveloperList.addAll(fetchDataFromDatabase());
                        if (mDeveloperList != null) {
                            mDeveloperAdapter.swap(mDeveloperList);
                            mDeveloperAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        notifyOffline();
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.item_list);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.developer_activity_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mDeveloperList != null && !mDeveloperList.isEmpty()) {
                    final ArrayList<Developer> filteredModelList = filter(mDeveloperList, newText);
                    mDeveloperAdapter.replaceAll(filteredModelList);
                    mRecyclerView.scrollToPosition(0);
                }

                return true;
            }
        });
        return true;
    }

    public void sortList(List<Developer> developers) {

        Collections.sort(developers, mComparator);
        mDeveloperAdapter.notifyDataSetChanged();
    }

    private static ArrayList<Developer> filter(ArrayList<Developer> developers, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final ArrayList<Developer> filteredModelList = new ArrayList<>();
        for (Developer developer : developers) {
            final String text = developer.getUsername();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(developer);
            }
        }
        return filteredModelList;
    }

    public interface ServerCallback {
        void onSuccess();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
        if (!mDeveloperList.isEmpty())
            outState.putParcelableArrayList(PERSIST_LIST_KEY, mDeveloperList);
    }

    private void fetchDataAndInsertIntoDatabase(final ServerCallback callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_QUERY_URL_WITH_PAGINATION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getContentResolver().delete(DeveloperEntry.CONTENT_URI, null, null);

                        ContentValues[] contentValues;
                        try {
                            int total_count = response.getInt("total_count");
                            int noOfPages = (int) Math.ceil(total_count / Integer.parseInt(SIZE_PER_PAGE));
                            JSONArray developers = response.getJSONArray("items");
                            final int NO_OF_DEVELOPERS = developers.length();
                            contentValues = new ContentValues[NO_OF_DEVELOPERS];

                            for (int i = 0; i < NO_OF_DEVELOPERS; i++) {
                                contentValues[i] = new ContentValues();
                                JSONObject currentDeveloper = developers.getJSONObject(i);
                                String username = currentDeveloper.getString(GITHUB_USERNAME_KEY);
                                String imageUrl = currentDeveloper.getString(GITHUB_IMAGE_URL_KEY);
                                String profileUrl = currentDeveloper.getString(GITHUB_PROFILE_URL_KEY);

                                contentValues[i].put(DeveloperEntry.COLUMN_DEVELOPER_USERNAME, username);
                                contentValues[i].put(DeveloperEntry.COLUMN_DEVELOPER_GITHUB_PROFILE_URL, profileUrl);
                                contentValues[i].put(DeveloperEntry.COLUMN_DEVELOPER_IMAGE_URL, imageUrl);

                            }
                            // Insert Data into DB
                            int n = getContentResolver().bulkInsert(DeveloperEntry.CONTENT_URI, contentValues);

                            callback.onSuccess();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "An error occurred while parsing JSON: " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                }
        );
        //Add the Request to the Request Queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    private ArrayList<Developer> fetchDataFromDatabase() {

        ArrayList<Developer> developers = new ArrayList<>();

        /* Query List On UI thread, since query is not large,
                 Cant affect UI thread
         */

        Cursor cursor = getContentResolver()
                .query(DeveloperEntry.CONTENT_URI, null, null, null, null);

        int usernameColumnIndex = cursor.getColumnIndex(DeveloperEntry.COLUMN_DEVELOPER_USERNAME);
        int imageUrlColumnIndex = cursor.getColumnIndex(DeveloperEntry.COLUMN_DEVELOPER_IMAGE_URL);
        int profileUrlColumnIndex = cursor.getColumnIndex(DeveloperEntry.COLUMN_DEVELOPER_GITHUB_PROFILE_URL);

        try {

            if (cursor.moveToFirst()) {
                do {

                    String username = cursor.getString(usernameColumnIndex);
                    String imageUrl = cursor.getString(imageUrlColumnIndex);
                    String profileUrl = cursor.getString(profileUrlColumnIndex);

                    developers.add(new Developer(username, imageUrl, profileUrl));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return developers;
    }

    private void notifyOffline() {
        if (!isInternetAvailable(this))
            Utils.makeIndefiniteSnackBar(mRecyclerView, "Your device is Currently Offline");
    }


}
