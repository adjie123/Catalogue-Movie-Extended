package adjie.made.cataloguemovie2.Adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adjie.made.cataloguemovie2.DetailActiivity;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.R;

public class UpComingAdapter  extends RecyclerView.Adapter<UpComingAdapter.UpComingHolder> {
    private static String KEY_ITEM = "item";
    private List<MoviesModel> upComingModels = new ArrayList<>();
    private Context context;

    public void setInstance(List<MoviesModel> items){
        this.upComingModels = items;
    }

    public List<MoviesModel> getList(){
        return upComingModels;
    }

    public void replaceAll(List<MoviesModel> items) {
        upComingModels.clear();
        upComingModels = items;
        notifyDataSetChanged();
    }
    public void setData(List<MoviesModel> items){
        upComingModels.clear();
        upComingModels = items;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public UpComingHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.up_coming_list, parent, false);

        return new UpComingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingHolder holder, final int position) {
        Context context = holder.imgPosterPath.getContext().getApplicationContext();
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185"+upComingModels.get(position).getPoster_path())
                .into(holder.imgPosterPath);

        holder.tvTitle.setText(upComingModels.get(position).getTitle());
        holder.tvOverview.setText(upComingModels.get(position).getOverview());
        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date tanggal = formatTanggal.parse(upComingModels.get(position).getRelease_date());
            SimpleDateFormat newFormatDate =  new SimpleDateFormat("EEEE, dd MMM yyyy");
            holder.tvReleaseDate.setText(newFormatDate.format(tanggal));
        }catch (ParseException e){
            e.printStackTrace();
        }

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoviesModel moviesModel = upComingModels.get(position);

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
                intent.putExtra(Intent.EXTRA_TITLE, upComingModels.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, upComingModels.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, upComingModels.get(position).getTitle() + "\n\n" + upComingModels.get(position).getOverview());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (upComingModels == null)return 0;
        return upComingModels.size();
    }

    class UpComingHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvOverview, tvReleaseDate;
        ImageView imgPosterPath;
        Button btnDetail, btnShare;
        public UpComingHolder(View itemView) {
            super(itemView);
            imgPosterPath = itemView.findViewById(R.id.img_film_coming);
            tvTitle = itemView.findViewById(R.id.tv_judul_coming);
            tvOverview = itemView.findViewById(R.id.tv_overview_coming);
            tvReleaseDate = itemView.findViewById(R.id.tv_rilis_coming);
            btnDetail = itemView.findViewById(R.id.btn_set_detail_coming);
            btnShare = itemView.findViewById(R.id.btn_set_share_coming);
        }
    }
}
