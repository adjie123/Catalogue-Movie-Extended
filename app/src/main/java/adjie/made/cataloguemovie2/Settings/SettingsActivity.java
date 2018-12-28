package adjie.made.cataloguemovie2.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.Model.MoviesModelResponse;
import adjie.made.cataloguemovie2.Network.ApiNowPlaying;
import adjie.made.cataloguemovie2.R;
import adjie.made.cataloguemovie2.Service.MovieDailyReminder;
import adjie.made.cataloguemovie2.Service.MovieJobService;
import adjie.made.cataloguemovie2.Utils.Language;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        SwitchPreference switchPrefReminder, switchPrefRelease;

        MovieDailyReminder movieDailyReminder = new MovieDailyReminder();
        MovieJobService movieJobService = new MovieJobService();

        private List<MoviesModel> moviesModels;
        private List<MoviesModel> moviesModelList;

        private Call<MoviesModelResponse> apiCall;
        private ApiNowPlaying apiNowPlaying = new ApiNowPlaying();

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);

            switchPrefReminder = (SwitchPreference) findPreference(getString(R.string.key_today_reminder));
            switchPrefReminder.setOnPreferenceChangeListener(this);

            switchPrefRelease = (SwitchPreference) findPreference(getString(R.string.key_release_reminder));
            switchPrefRelease.setOnPreferenceChangeListener(this);

            Preference preference = findPreference(getString(R.string.key_lang));
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    return true;
                }
            });
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String key = preference.getKey();
            boolean on = (boolean) o;

            if (key.equals(getString(R.string.key_today_reminder))){
                if (on){
                    movieDailyReminder.setAlarm(getActivity());
                }else{
                    movieDailyReminder.cancelAlarm(getActivity());
                }
            }else {
                if (on){
                    setAlarm();
                }else {
                    movieJobService.cancelAlarm(getActivity());
                }
            }
            return true;
        }

        private void setAlarm(){
            apiCall = apiNowPlaying.getService().getUpComing(Language.getCountry());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String now = dateFormat.format(date);

            apiCall.enqueue(new Callback<MoviesModelResponse>() {

                @Override
                public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                    moviesModels = response.body().getResults();
                    for(MoviesModel movie : moviesModelList){
                        if(movie.getRelease_date().equals(now)){
                            moviesModels.add(movie);
                        }
                    }
                    movieJobService.setAlarm(getActivity(), moviesModelList);
                }

                @Override
                public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.err_load_failed, Toast.LENGTH_SHORT).show();
                }

            });
        }

    }
}
