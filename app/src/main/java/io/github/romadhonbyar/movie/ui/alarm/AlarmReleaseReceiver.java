package io.github.romadhonbyar.movie.ui.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;

import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.api.Global;

public class AlarmReleaseReceiver extends BroadcastReceiver {
    public static int ID_REPEATING_release = 111;
    private FirebaseJobDispatcher mDispatcher;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        startDispatcher();
    }

    public void startDispatcher() {
        String DISPATCHER_TAG = "mydispatcher";
        Job myJob = mDispatcher.newJobBuilder()
                .setService(AlarmReleaseService.class)
                .setTag(DISPATCHER_TAG)
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0, 5))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        mDispatcher.mustSchedule(myJob);
    }

    public void setRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReleaseReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Global.HOUR_OF_DAY_release);
        calendar.set(Calendar.MINUTE, Global.MINUTE_release);
        calendar.set(Calendar.SECOND, Global.SECOND_release);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING_release, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.message_daily_repeat) + calendar.getTime(), Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReleaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING_release, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}