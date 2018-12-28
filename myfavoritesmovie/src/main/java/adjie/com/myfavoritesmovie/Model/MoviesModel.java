package adjie.com.myfavoritesmovie.Model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import adjie.com.myfavoritesmovie.Database.DatabaseContract;

import static adjie.com.myfavoritesmovie.Database.DatabaseContract.getColumnInt;
import static adjie.com.myfavoritesmovie.Database.DatabaseContract.getColumnString;
import static android.provider.BaseColumns._ID;

public class MoviesModel implements Parcelable {
    private int id;
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;

    public MoviesModel(){

    }
    public MoviesModel(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, DatabaseContract.FavoritesColumn.TITLE);
        this.poster_path = getColumnString(cursor, DatabaseContract.FavoritesColumn.POSTER_PATH);
        this.overview = getColumnString(cursor, DatabaseContract.FavoritesColumn.OVERVIEW);
        this.release_date = getColumnString(cursor, DatabaseContract.FavoritesColumn.RELEASE_DATE);
    }
    protected MoviesModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MoviesModel> CREATOR = new Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel in) {
            return new MoviesModel(in);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
