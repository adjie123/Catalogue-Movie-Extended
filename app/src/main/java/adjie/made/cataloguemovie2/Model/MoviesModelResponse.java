package adjie.made.cataloguemovie2.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesModelResponse {

    @SerializedName("results")
    private List<MoviesModel> results;


    public void setResults(List<MoviesModel> results) {
        this.results = results;
    }

    public List<MoviesModel> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "NowPlayingModel{" +
                        "results = '" + results + '\'' +
                        "}";
    }
}
