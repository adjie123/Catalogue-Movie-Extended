package adjie.com.myfavoritesmovie;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import adjie.com.myfavoritesmovie.Model.MoviesModel;

public class DetailActiivity extends AppCompatActivity {
    public static String KEY_ITEM = "item";
    private TextView tvDetailJudul, tvDetailOverview, tvDetailRilis;
    private ImageView imgDetailFilm;

    private MoviesModel moviesModel= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actiivity);

        tvDetailJudul = findViewById(R.id.tvDetailJudul);
        tvDetailOverview = findViewById(R.id.tvDetailOverview);
        tvDetailRilis = findViewById(R.id.tvDetailRilis);
        imgDetailFilm = findViewById(R.id.imgDetailFilm);

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){

                if(cursor.moveToFirst()) moviesModel = new MoviesModel(cursor);
                cursor.close();
            }
        }

        tvDetailJudul.setText(moviesModel.getTitle());
        tvDetailOverview.setText(moviesModel.getOverview());

        getSupportActionBar().setTitle(getResources().getString(R.string.name_bar_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date tanggal = formatTanggal.parse(moviesModel.getRelease_date());
            SimpleDateFormat newFormatDate =  new SimpleDateFormat("EEEE, dd MMM yyyy");
            tvDetailRilis.setText(newFormatDate.format(tanggal));
        }catch (ParseException e){
            e.printStackTrace();
        }

        Glide.with(this).load("http://image.tmdb.org/t/p/w185"+moviesModel.getPoster_path()).into(imgDetailFilm);


    }



}
