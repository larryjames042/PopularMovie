package popularmovie.co.larry.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import popularmovie.co.larry.popularmovie.Adapter.MoviesItemsAdapter;
import popularmovie.co.larry.popularmovie.Adapter.ReviewAdapter;
import popularmovie.co.larry.popularmovie.Data.FavContract;
import popularmovie.co.larry.popularmovie.Fragments.TabFragment1;
import popularmovie.co.larry.popularmovie.Fragments.TabFragment2;
import popularmovie.co.larry.popularmovie.Model.Review;

public class MovieDetailActivity extends AppCompatActivity implements
        TabFragment1.OnFragmentInteractionListener,
        TabFragment2.OnFragmentInteractionListener {

    // trailer path
    public static final String MOVIE_VIDEOS = "videos";
    // reviews path
    public static final String MOVIE_REVIEWS = "reviews";
    CollapsingToolbarLayout ctl;
    ImageView mPosterImage;
    ImageView mBackdrop;
    TextView mReleaseDate, mDuration, mVoteAverage;
    Boolean isChecked = false;

    int movieId;
    String backdrop,title,poster, releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBackdrop = findViewById(R.id.backdrop_image);
        mPosterImage = findViewById(R.id.movie_poster);
        mReleaseDate = findViewById(R.id.movie_release_date);
        mDuration = findViewById(R.id.movie_duration);
        mVoteAverage = findViewById(R.id.vote_average);


        // get the id extra from the intent that started this activity
        View view = findViewById(R.id.coordinator);
        if(getIntent().getExtras()!=null){
            Intent intentThatStartedThisActivity = getIntent();
            if(intentThatStartedThisActivity.hasExtra(MoviesItemsAdapter.MOVIE_ID_EXTRA)){
                movieId = intentThatStartedThisActivity.getIntExtra(MoviesItemsAdapter.MOVIE_ID_EXTRA, 1);
                loadMovieDetail(buildMovieDetailUrl(String.valueOf(movieId)));
//                // send id to child fragment
//                Bundle bundle = new Bundle();
//                bundle.putString("id", String.valueOf(movieId));
//                TabFragment1 fragment1 = new TabFragment1();
//                fragment1.setArguments(bundle);

            }
        }



        // setting up collasping toolbar layout
         ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctl.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_unfav);

        // Favourite floating button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ContentValues values = new ContentValues();
//                values.put(FavContract.FavEntry.COLUMN_MOVIE_TITLE, ctl.getTitle().toString() );
//                values.put(FavContract.FavEntry.COLUMN_MOVIE_ID, movieId );
//                values.put(FavContract.FavEntry.COLUMN_MOVIE_POSTER, poster);
//
//
//                Uri uri = getContentResolver().insert(FavContract.FavEntry.CONTENT_URI, values);
//                if(uri != null){
//                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
//                }
//
//                if(isChecked){
//                    fab.setImageResource(
//                            R.drawable.ic_unfavourite);
//                    isChecked = false;
//                }else{
//                    fab.setImageResource(
//                            R.drawable.ic_favourite);
//                    isChecked = true;
//                }

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Create and instance of Tab layout

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_review));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_trailer));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Create and instance of Viewpager

        final ViewPager viewPager = findViewById(R.id.view_pager);
        PagerAdapter adapter = new popularmovie.co.larry.popularmovie.Adapter.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // setting a listener for click
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadMovieDetail(String url){
    StringRequest request = new StringRequest(Request.Method.GET,
            url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject root = new JSONObject(response);
                         backdrop = root.getString("backdrop_path").toString();
                         title = root.getString("title").toString();
                         poster = root.getString("poster_path");
                         releaseDate = root.getString("release_date");
                        int duration = root.getInt("runtime");
                        Double voteAverage = root.getDouble("vote_average");
                        if(title!=null){
                            ctl.setTitle(title);
                        }
                        if(poster!=null){
                            final String imagePath = MoviesItemsAdapter.IMAGE_BASE_URL + MoviesItemsAdapter.IMAGE_SIZE + poster;
                            Picasso.with(getApplicationContext())
                                    .load(imagePath)
                                    .into(mPosterImage);
                        }
                        if(backdrop!=null){
                            final String imagePath = MoviesItemsAdapter.IMAGE_BASE_URL + MoviesItemsAdapter.IMAGE_SIZE + backdrop;
                            Picasso.with(getApplicationContext())
                                    .load(imagePath)
                                    .into( mBackdrop);
                        }
                        if(releaseDate!=null){
                            mReleaseDate.setText(releaseDate);
                        }
                        if(duration>0){
                            mDuration.setText(String.valueOf(duration));
                        }
                        if(voteAverage>0){
                            mVoteAverage.setText(String.valueOf(voteAverage));
                        }



                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                   // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error occurs while fetching JSON", Toast.LENGTH_SHORT).show();
                }
            }
    );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }




    public  String buildMovieDetailUrl(String id){
        Uri uri = Uri.parse(MainActivity.MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter("api_key", MainActivity.API_KEY)
                .appendQueryParameter(MainActivity.MOVIE_APPEND_TO_RESPONSE, MOVIE_VIDEOS + "," + MOVIE_REVIEWS )
                .build();

        return uri.toString();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
