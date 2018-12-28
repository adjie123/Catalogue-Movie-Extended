package adjie.made.cataloguemovie2.Service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import adjie.made.cataloguemovie2.DetailActiivity;
import adjie.made.cataloguemovie2.Model.MoviesModel;
import adjie.made.cataloguemovie2.Model.MoviesModelResponse;
import adjie.made.cataloguemovie2.Network.ApiNowPlaying;
import adjie.made.cataloguemovie2.R;
import adjie.made.cataloguemovie2.Utils.Language;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieJobService extends BroadcastReceiver {
    private Call<MoviesModelResponse> movieModel;
    public static String KEY_ITEM = "item";
    public static String TAG_TASK_UPCOMING = "upcoming movies";
    private ApiNowPlaying apiNowPlaying = new ApiNowPlaying();
    private Context mContext;

    private static final String NOTIFICATION_CHANNEL_ID = "120";
    private static int NOTIFICATION_ID = 120;

    @Override
    public void onReceive(final Context context, Intent intent) {
        movieModel = apiNowPlaying.getService().getUpComing(Language.getCountry());
        movieModel.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                List<MoviesModel> movieList = response.body().getResults();
                int position = new Random().nextInt(movieList.size());
                MoviesModel moviesModel = movieList.get(position);

                String title = movieList.get(position).getTitle();
                String message = movieList.get(position).getOverview();

                showNotification(context, title, message, NOTIFICATION_ID, moviesModel);
            }

            @Override
            public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                loadFailed();
            }

        });
    }


    private void loadFailed(){
        Toast.makeText(mContext, R.string.err_load_failed, Toast.LENGTH_SHORT).show();
    }


    private void showNotification(Context context, String title, String message, int notifId, MoviesModel moviesModel) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, DetailActiivity.class);
        intent.putExtra(DetailActiivity.KEY_ITEM, new Gson().toJson(moviesModel));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_update_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notifId, builder.build());
    }

    public void setAlarm(Context context, List<MoviesModel> moviesModels) {
        int notifDelay = 0;

        for (MoviesModel movieResult : moviesModels){
            cancelAlarm(context);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, MovieJobService.class);
            intent.putExtra("movie", new Gson().toJson(movieResult));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + notifDelay,
                        AlarmManager.INTERVAL_DAY,
                        getPendingIntent(context)
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + notifDelay, getPendingIntent(context));
            }
        }

        NOTIFICATION_ID++;
        notifDelay += 5000;


    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }
    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MovieDailyReminder.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
}

