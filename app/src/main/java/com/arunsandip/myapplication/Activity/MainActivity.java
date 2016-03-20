package com.arunsandip.myapplication.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arunsandip.myapplication.Adapter.MovieSearchAdapter;
import com.arunsandip.myapplication.AppController;
import com.arunsandip.myapplication.Util.MovieSearchPojo;
import com.arunsandip.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    DrawerLayout mDrawer = null;
    SearchView mSearchView = null;

    private ListView listViewMovies;

    private int mTheme = SearchCodes.THEME_LIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        listViewMovies = (ListView) findViewById(R.id.listViewMovies);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mSearchView.setDivider(true);
        mSearchView.setHint("Search");;
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistoryDatabase.addItem(new SearchItem(query));
        showSearchView(query);
        mSearchView.hide(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final List<SearchItem> mResultsList = new ArrayList<>();
        final SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, mTheme);

        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                mSuggestionsList.clear();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                showSearchView(text.toString());

//                mSearchView.setVisibility(View.GONE);


            }
        });

        mSearchView.setAdapter(mSearchAdapter);
        showSearchView("");  // TODO RELOAD
    }

    private void showSearchView(String text) {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        if (text != null) {
            if (text.length() > 0) {
                //http://www.omdbapi.com/?s=pk
                String searchUrl = "http://www.omdbapi.com/?s=" + text;
                makeJsonReqForMovie(searchUrl);
            }
        }
     }

    private void makeJsonReqForMovie(String searchUrl) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,searchUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                //  Toast.makeText(getApplicationContext(),"res" + response.toString(),Toast.LENGTH_LONG).show();

                GsonBuilder gsonBuilder = new GsonBuilder();
                //  gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                //  List<MovieSearchPojo> posts = new ArrayList<>();
                //  posts = Arrays.asList(gson.fromJson(reader, Post[].class));
                try {
                    JSONArray jsonArray = new JSONArray(response.get("Search").toString());
                    // Toast.makeText(getApplicationContext(),"jsonArray " + jsonArray.toString(),Toast.LENGTH_LONG).show();

                    if(jsonArray == null){

                        Toast.makeText(getApplicationContext(),"No DATA" , Toast.LENGTH_LONG ).show();
                    }

                    ArrayList<MovieSearchPojo> listType = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MovieSearchPojo>>() {
                    }.getType());
                    loadListView(listType);



                } catch (Exception io) {

                    //   Toast.makeText(getApplicationContext(),"res " + io.getMessage().toString(),Toast.LENGTH_LONG).show();
                    //   Log.e(TAG,io.getMessage()+"");

                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //     VolleyLog.d(TAG, "Error: " + error.getMessage());
                //      Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void loadListView(ArrayList<MovieSearchPojo> alMovies) {

        MovieSearchAdapter listAdapter = new MovieSearchAdapter(MainActivity.this, alMovies);
        listViewMovies.setAdapter(listAdapter);

    }

}

