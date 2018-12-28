package adjie.made.cataloguemovie2.NowPlaying;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import adjie.made.cataloguemovie2.Adapter.NowPlayingAdapter;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.Model.MoviesModelResponse;
import adjie.made.cataloguemovie2.Network.ApiNowPlaying;
import adjie.made.cataloguemovie2.R;
import adjie.made.cataloguemovie2.Utils.Language;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment{
    RecyclerView rvFilm;
    private NowPlayingAdapter nowPlayingAdapter;
    private Call<MoviesModelResponse> apiCall;
    private ApiNowPlaying apiNowPlaying = new ApiNowPlaying();
    private ArrayList<MoviesModel> list = new ArrayList<>();
    private Context context;

     public NowPlayingFragment() {
        // Required empty public constructor
    }
    View nowPalyingView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nowPalyingView = inflater.inflate(R.layout.now_playing, container, false);
        if (getActivity() != null){
            getActivity().setTitle(getResources().getString(R.string.appbar_nowplaying));
        }

        initAdapter();
        if(savedInstanceState!=null){
            list = savedInstanceState.getParcelableArrayList("movie");
            nowPlayingAdapter.setInstance(list);
        }else{
            loadData();
        }

        return nowPalyingView;
    }



    private void initAdapter(){

        nowPlayingAdapter = new NowPlayingAdapter(context);
        rvFilm = nowPalyingView.findViewById(R.id.rv_category);
        rvFilm.setLayoutManager(new LinearLayoutManager(context));
        rvFilm.setHasFixedSize(true);
        rvFilm.setAdapter(nowPlayingAdapter);
    }

    private void loadData(){
        apiCall = apiNowPlaying.getService().getNowPlaying(Language.getCountry());
        apiCall.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                nowPlayingAdapter.replaceAll(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.err_load_failed, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie", new ArrayList<>(nowPlayingAdapter.getList()));
    }

}
