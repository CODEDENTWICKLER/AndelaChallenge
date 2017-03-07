package com.example.andelachallenge.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.andelachallenge.AppController;
import com.example.andelachallenge.R;
import com.example.andelachallenge.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DeveloperDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DeveloperListActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "866090ebefadc33ef048968ae2715d3a61829669";

    private static final String ACCESS_TOKEN_PARAMS = "access_token";

    private static final String PAGE_NO_PARAMS = "page";

    private static final String SIZE_PER_PAGE_PARAMS = "per_page";

    private static final String NO_OF_PAGES = "1";

    private static final String SIZE_PER_PAGE = "100";


    private static final String GITHUB_QUERY_URL =
            "https://api.github.com/search/users?q=language:java+location:lagos";

    private static final String GITHUB_QUERY_URL_WITH_PAGINATION =
            Uri.parse(GITHUB_QUERY_URL).buildUpon().
                    appendQueryParameter(PAGE_NO_PARAMS,NO_OF_PAGES).
                    appendQueryParameter(SIZE_PER_PAGE_PARAMS, SIZE_PER_PAGE).toString();


    private static final String GITHUB_QUERY_URL_WITH_ACCESS_TOKEN =
            Uri.parse(GITHUB_QUERY_URL_WITH_PAGINATION).buildUpon().
                    appendQueryParameter(ACCESS_TOKEN_PARAMS, ACCESS_TOKEN).toString();


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Developer> mDeveloperList;
    private DevelopersAdapter mDeveloperAdapter;

    private static final String TAG = DeveloperListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        mDeveloperList = new ArrayList<>();


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")) {

            fetchList();
        }
        else {
            mDeveloperList = savedInstanceState.getParcelableArrayList("key");
            if (mDeveloperList.isEmpty())
                fetchList();
        }
        setupRecyclerView((RecyclerView) recyclerView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.developer_activity_list_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                ArrayList<Developer> newList = new ArrayList<Developer>();

                for (Developer developer: mDeveloperList) {
                    String username = developer.getUsername();

                    if (username.contains(newText))
                        newList.add(developer);
                }


                mDeveloperAdapter.setFilter(newList);

                return true;

            }
        });
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) mDeveloperList);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mDeveloperAdapter = new DevelopersAdapter(this);
        recyclerView.setAdapter(mDeveloperAdapter);
    }


    public class DevelopersAdapter extends RecyclerView.Adapter<DevelopersAdapter.DeveloperViewHolder> {

        private Context mContext;

        DevelopersAdapter(Context context) {
            this.mContext  = context;
        }

        void sortList(){

            Collections.sort(mDeveloperList, new Comparator<Developer>() {
                @Override
                public int compare(Developer dev1, Developer dev2) {
                    return dev1.getUsername().compareToIgnoreCase(dev2.getUsername());
                }
            });

            notifyDataSetChanged();
        }

        void setFilter(ArrayList<Developer> newList){

            mDeveloperList = new ArrayList<>();
            mDeveloperList.addAll(newList);
            notifyDataSetChanged();

        }

        /**
         *  This DeveloperViewHolder initialize the views that belong to the items of our RecyclerView.
         */

        class DeveloperViewHolder extends RecyclerView.ViewHolder {

            TextView devUsernameTextView;
            CircleImageView devImageView;
            Developer mDeveloper;
            View mView;


            DeveloperViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                devUsernameTextView = (TextView) itemView.findViewById(R.id.username_textView);
                devImageView = (CircleImageView) itemView.findViewById(R.id.list_imageView);
            }
        }

        /**
         * @param parent
         * @param viewType
         * @return a new view for each item in our recycler view
         */

        @Override
        public DeveloperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.developer_list_content,parent,false);

            return new DeveloperViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final DeveloperViewHolder holder, int position) {

            holder.mDeveloper = mDeveloperList.get(position);
            holder.devUsernameTextView.setText(holder.mDeveloper.getUsername());

            String imageUrl = holder.mDeveloper.getImageUrl();
            if (imageUrl.equals(null) || imageUrl.equals("")|| imageUrl.isEmpty())
                holder.devImageView.setImageResource(R.drawable.ic_person_outline_black_32dp);
            else
                Glide.with(mContext)
                        .load(holder.mDeveloper.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter().dontAnimate()
                        .placeholder(R.drawable.ic_person_outline_black_32dp)
                        .into(holder.devImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);
                        DeveloperDetailFragment fragment = new DeveloperDetailFragment();
                        fragment.setArguments(arguments);
                        ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DeveloperDetailActivity.class);
                        intent.putExtra(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);

                        context.startActivity(intent);
                    }
                }
            });

        }

        /**
         * @return the number of items present in the data.
         */

        @Override
        public int getItemCount() {
            if (mDeveloperList != null)
                return mDeveloperList.size();
            else
                return 0;
        }
    }
    private void fetchList(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_QUERY_URL_WITH_ACCESS_TOKEN, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mDeveloperList.clear();
                        Log.d(TAG,GITHUB_QUERY_URL_WITH_ACCESS_TOKEN);

                        try {
                            JSONArray developers = response.getJSONArray("items");
                            for (int i = 0; i < developers.length();i++){
                                JSONObject currentDeveloper = developers.getJSONObject(i);
                                String username = currentDeveloper.getString("login");
                                String imageUrl = currentDeveloper.getString("avatar_url");
                                String profileUrl = currentDeveloper.getString("url");

                                mDeveloperList.add(new Developer(username,imageUrl,profileUrl));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,"An error occurred while parsing JSON: "+e.getMessage());
                        }

                        mDeveloperAdapter.sortList();
                        mDeveloperAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Error: "+error.getMessage());
                    }
                }
        );

        //Add the Request to the Request Queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
