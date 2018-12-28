package adjie.made.cataloguemovie2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import adjie.made.cataloguemovie2.Database.DatabaseContract;
import adjie.made.cataloguemovie2.Database.FavoriteHelper;
import adjie.made.cataloguemovie2.Model.MoviesModel;

import static adjie.made.cataloguemovie2.Database.DatabaseContract.CONTENT_URI;

public class DetailActiivity extends AppCompatActivity {
    public static String KEY_ITEM = "item";
    private TextView tvDetailJudul, tvDetailOverview, tvDetailRilis;
    private ImageView imgDetailFilm, imgFavorite;

    private MoviesModel moviesModel;
    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actiivity);

        tvDetailJudul = findViewById(R.id.tvDetailJudul);
        tvDetailOverview = findViewById(R.id.tvDetailOverview);
        tvDetailRilis = findViewById(R.id.tvDetailRilis);
        imgDetailFilm = findViewById(R.id.imgDetailFilm);
        imgFavorite = findViewById(R.id.iv_fav);

        moviesModel = getIntent().getParcelableExtra(KEY_ITEM);
        tvDetailJudul.setText(moviesModel.getTitle());
        tvDetailOverview.setText(moviesModel.getOverview());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.name_bar_detail));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date tanggal = formatTanggal.parse(moviesModel.getRelease_date());
            SimpleDateFormat newFormatDate = new SimpleDateFormat("EEEE, dd MMM yyyy");
            tvDetailRilis.setText(newFormatDate.format(tanggal));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(this).load("https://image.tmdb.org/t/p/w185" + moviesModel.getPoster_path()).into(imgDetailFilm);


        // Load SQLite
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + moviesModel.getId()), null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }

        favoriteSet();

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) removeFromFavorite();
                else addToFavorite();

                isFavorite = !isFavorite;
                favoriteSet();
            }
        });
    }

    private void favoriteSet() {
        if (isFavorite) imgFavorite.setImageResource(R.drawable.ic_star_black);
        else imgFavorite.setImageResource(R.drawable.ic_star_border_black);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite() {
        //Log.d("TAG", "FavoriteSave: " + item.getId());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.FavoritesColumn._ID, moviesModel.getId());
        cv.put(DatabaseContract.FavoritesColumn.TITLE, moviesModel.getTitle());
        cv.put(DatabaseContract.FavoritesColumn.POSTER_PATH, moviesModel.getPoster_path());
        cv.put(DatabaseContract.FavoritesColumn.OVERVIEW, moviesModel.getOverview());
        cv.put(DatabaseContract.FavoritesColumn.RELEASE_DATE, moviesModel.getRelease_date());

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(this, "Data Disimpan", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + moviesModel.getId()),
                null,
                null
        );
        Toast.makeText(this, "Data Dihapus", Toast.LENGTH_SHORT).show();
    }

}
