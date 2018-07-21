package popularmovie.co.larry.popularmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import popularmovie.co.larry.popularmovie.Model.MoviesItems;
import popularmovie.co.larry.popularmovie.MovieDetailActivity;
import popularmovie.co.larry.popularmovie.R;

public class MoviesItemsAdapter extends RecyclerView.Adapter<MoviesItemsAdapter.ViewHolder> {

    private List<MoviesItems> moviesItemsList;
    private Context mContext;
    public  static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w342/";
    public static final String MOVIE_ID_EXTRA = "id";
    public MoviesItemsAdapter(List<MoviesItems> list, Context context){
        moviesItemsList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public MoviesItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesItemsAdapter.ViewHolder holder, int position) {
        final MoviesItems moviesItems = moviesItemsList.get(position);

        holder.mTitle.setText(moviesItems.getmTitle());
        final String imagePath = IMAGE_BASE_URL + IMAGE_SIZE + moviesItems.getmPosterPath();
        Picasso.with(mContext)
                .load(imagePath)
                .into(holder.mImage);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MovieDetailActivity.class);
                i.putExtra(MOVIE_ID_EXTRA, moviesItems.getmId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesItemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImage;
        TextView mTitle;
        CardView mContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.movie_image);
            mTitle = itemView.findViewById(R.id.movie_title);
            mContainer = itemView.findViewById(R.id.movie_item_container);
        }
    }
}
