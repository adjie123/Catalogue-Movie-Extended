package adjie.made.cataloguemovie2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import adjie.made.cataloguemovie2.DetailActiivity;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.R;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {
    private Cursor listFavorites;

    public FavoritesAdapter(Cursor items){
        replaceAll(items);
    }


    public void replaceAll(Cursor items) {
        listFavorites = items;
        notifyDataSetChanged();
    }

    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list, parent, false);

        return new FavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
        Context img = holder.imgPosterPath.getContext().getApplicationContext();
        final MoviesModel moviesModel= getItem(position);
        Glide.with(img)
                .load("http://image.tmdb.org/t/p/w185"+moviesModel.getPoster_path())
                .into(holder.imgPosterPath);

        holder.tvTitle.setText(moviesModel.getTitle());
        holder.tvOverview.setText(moviesModel.getOverview());
        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date tanggal = formatTanggal.parse(moviesModel.getRelease_date());
            SimpleDateFormat newFormatDate =  new SimpleDateFormat("EEEE, dd MMM yyyy");
            holder.tvReleaseDate.setText(newFormatDate.format(tanggal));
        }catch (ParseException e){
            e.printStackTrace();
        }

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), DetailActiivity.class);
                intent.putExtra(DetailActiivity.KEY_ITEM, moviesModel);
                view.getContext().startActivity(intent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, moviesModel.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, moviesModel.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, moviesModel.getTitle() + "\n\n" + moviesModel.getOverview());
                view.getContext().startActivity(intent);
            }
        });
    }

    private MoviesModel getItem(int position){
        if (!listFavorites.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MoviesModel(listFavorites);
    }

    @Override
    public int getItemCount() {
        if (listFavorites == null)return 0;
        return listFavorites.getCount();
    }

    class FavoritesHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvOverview, tvReleaseDate;
        ImageView imgPosterPath;
        Button btnDetail, btnShare;
        public FavoritesHolder(View itemView) {
            super(itemView);
            imgPosterPath = itemView.findViewById(R.id.img_film_favorite);
            tvTitle = itemView.findViewById(R.id.tv_judul_favorite);
            tvOverview = itemView.findViewById(R.id.tv_overview_favorite);
            tvReleaseDate = itemView.findViewById(R.id.tv_rilis_favorite);
            btnDetail = itemView.findViewById(R.id.btn_set_detail_favorite);
            btnShare = itemView.findViewById(R.id.btn_set_share_favorite);
        }
    }
}
