package com.example.andelachallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andelachallenge.adapter.DevelopersAdapter;
import com.example.andelachallenge.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Developer> mDevelopersList;
    private static final String TAG = MainActivity.class.getSimpleName();


    private  DevelopersAdapter mDevelopersAdapter;

    private RecyclerView mDevsRecyclerView;
    private static final String GITHUB_QUERY_URL = "https://api.github.com/search/users?q=language:java+location:lagos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDevsRecyclerView = (RecyclerView) findViewById(R.id.developers_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDevsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mDevsRecyclerView.getContext(), layoutManager.getOrientation());
        mDevsRecyclerView.addItemDecoration(dividerItemDecoration);
        mDevelopersList = new ArrayList<>();

        mDevelopersList.add(new Developer("Moyheen","","jhfjkhfj"));
        mDevelopersList.add(new Developer("Jack Burton","","khkfagkjfa"));
        mDevelopersList.add(new Developer("Segun Famisa","","jhgag"));

        mDevelopersAdapter = new DevelopersAdapter(this, mDevelopersList);
        mDevsRecyclerView.setAdapter(mDevelopersAdapter);

        fetchData();
    }

    private void fetchData(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_QUERY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mDevelopersList.clear();

                        try {
                            JSONArray developers = response.getJSONArray("items");
                            for (int i = 0; i < developers.length();i++){
                                JSONObject currentDeveloper = developers.getJSONObject(i);
                                String username = currentDeveloper.getString("login");
                                String imageUrl = currentDeveloper.getString("avatar_url");
                                String profileUrl = currentDeveloper.getString("url");

                                mDevelopersList.add(new Developer(username,imageUrl,profileUrl));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,"An error occurred while parsing JSON: "+e.getMessage());
                        }

                        mDevelopersAdapter.notifyDataSetChanged();
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
