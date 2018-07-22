package popularmovie.co.larry.popularmovie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import popularmovie.co.larry.popularmovie.Adapter.ReviewAdapter;
import popularmovie.co.larry.popularmovie.MainActivity;
import popularmovie.co.larry.popularmovie.Model.Review;
import popularmovie.co.larry.popularmovie.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView reviewRecyclerView;
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment1 newInstance(String param1, String param2) {
        TabFragment1 fragment = new TabFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        reviewRecyclerView = view.findViewById(R.id.rv_reviews);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewList = new ArrayList<>();

            String id = getArguments().getString("id");
            loadReviews(buildMovieDetailUrl(id));

//        loadReviews("https://api.themoviedb.org/3/movie/157336/reviews?api_key=5fa2eaf4020be641353707ce7a4a8cce");


        return view;
    }

    private void loadReviews(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET
                , url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //reviewList.clear();
                try {
                    JSONObject root = new JSONObject(response);
                    // reviews

                    JSONArray results = root.getJSONArray("results");
                    for(int i = 0 ; i < results.length() ; i++){
                        JSONObject mObj = results.getJSONObject(i);
                        String author, content;
                        author = mObj.getString("author");
                        content = mObj.getString("content");
                        reviewList.add(new Review(author, content));
                    }

                    reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
                    reviewRecyclerView.setAdapter(reviewAdapter);

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error occurs while fetching JSON", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String buildMovieDetailUrl(String id){
//        Uri uri  = Uri.parse(MainActivity.MOVIE_BASE_URL).buildUpon()
//                .appendPath("movie")
//                .appendPath(id)
//                .appendPath("reviews")
//                .appendQueryParameter("api_key", MainActivity.API_KEY)
//                .build();
        Uri uri = Uri.parse(MainActivity.MOVIE_BASE_URL).buildUpon()
             //   .appendPath("movie")
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", MainActivity.API_KEY)
                .build();

        return uri.toString();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
