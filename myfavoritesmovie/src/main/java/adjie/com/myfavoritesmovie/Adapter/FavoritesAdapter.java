package adjie.com.myfavoritesmovie.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import adjie.com.myfavoritesmovie.R;

import static adjie.com.myfavoritesmovie.Database.DatabaseContract.FavoritesColumn.OVERVIEW;
import static adjie.com.myfavoritesmovie.Database.DatabaseContract.FavoritesColumn.POSTER_PATH;
import static adjie.com.myfavoritesmovie.Database.DatabaseContract.FavoritesColumn.RELEASE_DATE;
import static adjie.com.myfavoritesmovie.Database.DatabaseContract.FavoritesColumn.TITLE;
import static adjie.com.myfavoritesmovie.Database.DatabaseContract.getColumnString;

public class FavoritesAdapter extends CursorAdapter{
    public FavoritesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }



    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_list, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle, tvOverview, tvReleaseDate;
        ImageView imgPosterPath;
        if (cursor != null){
            imgPosterPath = view.findViewById(R.id.img_film_favorite);
            tvTitle = view.findViewById(R.id.tv_judul_favorite);
            tvOverview = view.findViewById(R.id.tv_overview_favorite);
            tvReleaseDate = view.findViewById(R.id.tv_rilis_favorite);

            tvTitle.setText(getColumnString(cursor, TITLE));
            Glide.with(context)
                    .load("http://image.tmdb.org/t/p/w185"+getColumnString(cursor, POSTER_PATH))
                    .into(imgPosterPath);
            tvOverview.setText(getColumnString(cursor, OVERVIEW));

            SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date tanggal = formatTanggal.parse(getColumnString(cursor, RELEASE_DATE));
                SimpleDateFormat newFormatDate =  new SimpleDateFormat("EEEE, dd MMM yyyy");
                tvReleaseDate.setText(newFormatDate.format(tanggal));
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
    }

}
