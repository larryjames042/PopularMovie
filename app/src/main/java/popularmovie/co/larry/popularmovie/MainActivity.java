package popularmovie.co.larry.popularmovie;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import popularmovie.co.larry.popularmovie.Adapter.MoviesItemsAdapter;
import popularmovie.co.larry.popularmovie.Model.MoviesItems;

public class MainActivity extends AppCompatActivity {


    public static final String API_KEY = BuildConfig.THE_MOVIEDB_API_KEY;
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String MOVIE_APPEND_TO_RESPONSE = "append_to_response";
    public static final String TRAILER_BASE_URL = "https://www.youtube.com";
    public static final String TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String UPCOMING = "upcoming";

    private List<MoviesItems> moviesItemsList;
    RecyclerView mMovieRecyclerView;
    MoviesItemsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get an instance of recyclerView
        mMovieRecyclerView = findViewById(R.id.rv_movies);
        mMovieRecyclerView.setHasFixedSize(true);

        // change the grid column count at screen orientation
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        moviesItemsList = new ArrayList<>();
        loadMovieData(buildUrl(UPCOMING));
    }

    public final void loadMovieData(String url){
        // show dialog while fetching data from internet

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    moviesItemsList.clear();

                    JSONObject root = new JSONObject(response);
                    JSONArray moviesArray = root.getJSONArray("results");
                    for(int i=0 ; i < moviesArray.length(); i++){
                        JSONObject mObj = moviesArray.getJSONObject(i);

                        MoviesItems items = new MoviesItems(
                                mObj.getString("poster_path"),
                                mObj.getInt("id"),
                                mObj.getString("title"));

                        moviesItemsList.add(items);
                    }

                    mAdapter = new MoviesItemsAdapter(moviesItemsList, getApplicationContext());
                    mMovieRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.json_error) , Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // helper method to build "sort by" url
    public String buildUrl(String sortBy){
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter("api_key", API_KEY)
                .build();

        return  uri.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId){
            case R.id.fav_menu:
                return true;
            case R.id.menu_popular:
                loadMovieData(buildUrl(POPULAR));
                return true;
            case R.id.menu_upcoming:
                loadMovieData(buildUrl(UPCOMING));
               return true;
            case R.id.menu_top_rated:
                loadMovieData(buildUrl(TOP_RATED));
              return true;
              default:
                  return super.onOptionsItemSelected(item);

        }

    }
}
