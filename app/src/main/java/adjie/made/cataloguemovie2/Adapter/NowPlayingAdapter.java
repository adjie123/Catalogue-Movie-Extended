package adjie.made.cataloguemovie2.Adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adjie.made.cataloguemovie2.DetailActiivity;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.R;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.NowPlayingHolder>{
    private List<MoviesModel> nowPlayingModels = new ArrayList<>();
    private Context context;

    public NowPlayingAdapter(Context context){
        this.context = context;
    }

    public void setInstance(List<MoviesModel> items){
        this.nowPlayingModels = items;
    }

    public List<MoviesModel> getList(){
        return nowPlayingModels;
    }

    public void replaceAll(List<MoviesModel> items) {
        nowPlayingModels.clear();
        nowPlayingModels = items;
        notifyDataSetChanged();
    }
    public void setData(List<MoviesModel> items){
        nowPlayingModels.clear();
        nowPlayingModels = items;
        notifyDataSetChanged();
    }


    @Override
    public NowPlayingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_playing_list, parent, false);

        return new NowPlayingHolder(view);
    }

    @Override
    public void onBindViewHolder(final NowPlayingHolder holder, final int position) {
        Context img = holder.imgPosterPath.getContext().getApplicationContext();
      Glide.with(img)
                .load("https://image.tmdb.org/t/p/w185"+nowPlayingModels.get(position).getPoster_path())
                .into(holder.imgPosterPath);

        holder.tvTitle.setText(nowPlayingModels.get(position).getTitle());
        holder.tvOverview.setText(nowPlayingModels.get(position).getOverview());
        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date tanggal = formatTanggal.parse(nowPlayingModels.get(position).getRelease_date());
            SimpleDateFormat newFormatDate =  new SimpleDateFormat("EEEE, dd MMM yyyy");
            holder.tvReleaseDate.setText(newFormatDate.format(tanggal));
        }catch (ParseException e){
            e.printStackTrace();
        }

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                MoviesModel moviesModel = nowPlayingModels.get(position);

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
                intent.putExtra(Intent.EXTRA_TITLE, nowPlayingModels.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, nowPlayingModels.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, nowPlayingModels.get(position).getTitle() + "\n\n" + nowPlayingModels.get(position).getOverview());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (nowPlayingModels == null)return 0;
        return nowPlayingModels.size();
    }

    class NowPlayingHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvOverview, tvReleaseDate;
        ImageView imgPosterPath;
        Button btnDetail, btnShare;
        public NowPlayingHolder(View itemView) {
            super(itemView);
            imgPosterPath = itemView.findViewById(R.id.img_film);
            tvTitle = itemView.findViewById(R.id.tv_judul);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvReleaseDate = itemView.findViewById(R.id.tv_rilis);
            btnDetail = itemView.findViewById(R.id.btn_set_detail);
            btnShare = itemView.findViewById(R.id.btn_set_share);
        }
    }
}

