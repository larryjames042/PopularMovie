package popularmovie.co.larry.popularmovie.Model;

public class MoviesItems {
    private String mPosterPath;
    private int mId;
    private String mTitle;

    public MoviesItems(String mPosterPath, int mId, String mTitle) {
        this.mPosterPath = mPosterPath;
        this.mId = mId;
        this.mTitle = mTitle;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
