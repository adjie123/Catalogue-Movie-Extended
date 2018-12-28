package adjie.made.cataloguemovie2.SearchMovie;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class SearchMovieFragment extends Fragment implements View.OnClickListener {
    private NowPlayingAdapter movieAdapter;
    RecyclerView rvFilm;
    private ArrayList<MoviesModel> list = new ArrayList<>();
    private Call<MoviesModelResponse> apiCall;
    private Context context;
    private ApiNowPlaying apiNowPlaying = new ApiNowPlaying();

    private EditText edtCari;
    private Button btnCari;

    public SearchMovieFragment() {
        // Required empty public constructor
    }
    View searchMovie;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchMovie = inflater.inflate(R.layout.activity_search, container, false);
        getActivity().setTitle(getResources().getString(R.string.appbar_search));
        edtCari = searchMovie.findViewById(R.id.edt_cari);
        btnCari = searchMovie.findViewById(R.id.btn_cari);
        btnCari.setOnClickListener(this);

        if(savedInstanceState!=null){
            initAdapter();
            list = savedInstanceState.getParcelableArrayList("movie");
            movieAdapter.setInstance(list);
        }else{
            String title = edtCari.getText().toString();
            loadData(title);
        }

        return searchMovie;
    }

    private void initAdapter(){
        movieAdapter = new NowPlayingAdapter(context);
        rvFilm = searchMovie.findViewById(R.id.rv_search);
        rvFilm.setLayoutManager(new LinearLayoutManager(context));
        rvFilm.setHasFixedSize(true);
        rvFilm.setAdapter(movieAdapter);
    }

    private void loadData(String title) {
        apiCall = apiNowPlaying.getService().getSearchMovie(title, Language.getCountry());
        apiCall.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                if (response.isSuccessful()) {
                    movieAdapter.replaceAll(response.body().getResults());
                }else{
                    Toast.makeText(getActivity(), R.string.err_load_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.err_load_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String title = edtCari.getText().toString();
        loadData(title);
        initAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie", new ArrayList<>(movieAdapter.getList()));
    }
}
