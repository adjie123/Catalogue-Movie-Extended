package adjie.made.cataloguemovie2.UpComing;


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

import adjie.made.cataloguemovie2.Adapter.UpComingAdapter;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.Model.MoviesModelResponse;
import adjie.made.cataloguemovie2.Network.ApiNowPlaying;
import adjie.made.cataloguemovie2.R;
import adjie.made.cataloguemovie2.Utils.Language;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpComingFragment extends Fragment {
    RecyclerView rvFilm;
    private UpComingAdapter upComingAdapter;
    private Context context;
    private ArrayList<MoviesModel> list = new ArrayList<>();
    private Call<MoviesModelResponse> apiCall;
    private ApiNowPlaying apiNowPlaying = new ApiNowPlaying();

    public UpComingFragment() {
        // Required empty public constructor
}
    View upComingView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        upComingView = inflater.inflate(R.layout.up_coming, container, false);
        if (getActivity() != null){
            getActivity().setTitle(getResources().getString(R.string.appbar_upcoming));
        }

        initAdapter();
        if(savedInstanceState!=null){
            list = savedInstanceState.getParcelableArrayList("movie");
            upComingAdapter.setInstance(list);
        }else{
            loadData();
        }
        return upComingView;
    }

    private void initAdapter(){

        upComingAdapter = new UpComingAdapter();
        rvFilm = upComingView.findViewById(R.id.rv_soon);
        rvFilm.setLayoutManager(new LinearLayoutManager(context));
        rvFilm.setHasFixedSize(true);
        rvFilm.setAdapter(upComingAdapter);
    }

    private void loadData(){
        apiCall = apiNowPlaying.getService().getUpComing(Language.getCountry());
        apiCall.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                upComingAdapter.replaceAll(response.body().getResults());
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
        outState.putParcelableArrayList("movie", new ArrayList<>(upComingAdapter.getList()));
    }

}
