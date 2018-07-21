package popularmovie.co.larry.popularmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import popularmovie.co.larry.popularmovie.MainActivity;
import popularmovie.co.larry.popularmovie.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    Context mContext;
    List<String> mList;
    public TrailerAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String key = mList.get(position);
        Picasso.with(mContext).load(buildTrailerImageUrl(key)).into(holder.trailerImage);
        holder.no.setText("Trailer : " + (position+1));
        holder.trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.TRAILER_BASE_URL).buildUpon().appendPath("watch").appendQueryParameter("v", key).build());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView trailerImage;
        TextView no;
        public ViewHolder(View itemView) {
            super(itemView);
            trailerImage = itemView.findViewById(R.id.trailer_imageview);
            no = itemView.findViewById(R.id.trailer_no);
        }
    }

    private String buildTrailerVideoUrl(String key){
        Uri uri = Uri.parse(MainActivity.TRAILER_BASE_URL)
                .buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", key)
                .build();

        return uri.toString();
    }

    private String buildTrailerImageUrl(String key){
        Uri uri = Uri.parse(MainActivity.TRAILER_THUMBNAIL_BASE_URL)
                .buildUpon()
                .appendPath("vi")
                .appendPath(key)
                .appendPath("0.jpg")
                .build();

        return uri.toString();
    }
}
