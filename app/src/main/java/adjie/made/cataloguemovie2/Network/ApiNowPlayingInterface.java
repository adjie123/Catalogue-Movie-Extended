package adjie.made.cataloguemovie2.Network;

import adjie.made.cataloguemovie2.Model.MoviesModelResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNowPlayingInterface {
    @GET("movie/now_playing")
    Call<MoviesModelResponse> getNowPlaying(@Query("language") String language);

    @GET("movie/upcoming")
    Call<MoviesModelResponse> getUpComing(@Query("language") String language);

    @GET("search/movie")
    Call<MoviesModelResponse> getSearchMovie(@Query("query") String query, @Query("language") String language);
}
