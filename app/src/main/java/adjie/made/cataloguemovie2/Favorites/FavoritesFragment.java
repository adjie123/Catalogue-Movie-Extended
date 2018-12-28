package adjie.made.cataloguemovie2.Favorites;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adjie.made.cataloguemovie2.Adapter.FavoritesAdapter;
import adjie.made.cataloguemovie2.R;

import static adjie.made.cataloguemovie2.Database.DatabaseContract.CONTENT_URI;

public class FavoritesFragment extends Fragment {
    RecyclerView rvFilm;
    private FavoritesAdapter favoritesAdapter;
    private Context context;
    private Cursor listFavorites;

    public FavoritesFragment() {
        // Required empty public constructor
    }
    View favoritesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        favoritesView = inflater.inflate(R.layout.fragment_favorites, container, false);
        context = favoritesView.getContext();

        if (getActivity() != null){
            getActivity().setTitle(getResources().getString(R.string.appbar_favorite));
        }

        initAdapter();
        new LoadFavoritesAsync().execute();
        return favoritesView;
    }

    private void initAdapter(){

        favoritesAdapter = new FavoritesAdapter(listFavorites);
        rvFilm = favoritesView.findViewById(R.id.rv_category_favorite);
        rvFilm.setLayoutManager(new LinearLayoutManager(context));
        rvFilm.setHasFixedSize(true);
        rvFilm.setAdapter(favoritesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadFavoritesAsync().execute();
    }

    private class LoadFavoritesAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(CONTENT_URI,null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor list) {
            super.onPostExecute(list);

            listFavorites = list;

            favoritesAdapter.replaceAll(listFavorites);
            favoritesAdapter.notifyDataSetChanged();

        }
    }



}
