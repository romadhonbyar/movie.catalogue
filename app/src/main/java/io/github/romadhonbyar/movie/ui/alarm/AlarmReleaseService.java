package io.github.romadhonbyar.movie.ui.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.github.romadhonbyar.movie.MainActivity;
import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.api.RetrofitClient;
import io.github.romadhonbyar.movie.ui.alarm.model.MovieReleaseModel;
import io.github.romadhonbyar.movie.ui.alarm.model.MovieReleaseResultModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.romadhonbyar.movie.BuildConfig.API_KEY;
import static io.github.romadhonbyar.movie.helper.FormatData.dateFormat;
import static io.github.romadhonbyar.movie.ui.alarm.AlarmReleaseReceiver.ID_REPEATING_release;

public class AlarmReleaseService extends JobService {
    private static int ID_REPEATING_release = 111;
    private List<MovieReleaseResultModel> mData = new ArrayList<>();

    private void loadData(JobParameters job) {
        String dateNow = dateFormat();
        Log.e("Hai", "has been release today!" + dateNow);

        RetrofitClient.getInstance().getApi().getMovieRelease(API_KEY, dateNow, dateNow).enqueue(new Callback<MovieReleaseModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieReleaseModel> call, @NonNull Response<MovieReleaseModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    MovieReleaseModel myData = Objects.requireNonNull(response.body());
                    try {
                        mData = myData.getResults();
                        String message_desc = mData.get(0).getOriginalTitle() + " has been release today!";
                        showAlarmNotification(getApplicationContext(), mData.get(0).getOriginalTitle(), message_desc, ID_REPEATING_release);
                        jobFinished(job, false);
                    }catch (Exception e){
                        jobFinished(job, true);
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReleaseModel> call, @NonNull Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    public void showAlarmNotification(Context context, String title, String message, int id_daily) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Release channel";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id_daily, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(id_daily, notification);
        }
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        loadData(job);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
