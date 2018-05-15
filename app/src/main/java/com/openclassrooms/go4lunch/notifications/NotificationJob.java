package com.openclassrooms.go4lunch.notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.openclassrooms.go4lunch.controllers.activities.DetailActivity;

import java.util.concurrent.TimeUnit;

public class NotificationJob extends Job {

    static final String TAG = "notification_job_tag";
    private static final int notificationID = 1234;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        PersistableBundleCompat extras = params.getExtras();
        String placeId = extras.getString("PLACE_ID", "");

        sendNotification(placeId);

        return Result.SUCCESS;
    }

    public static void showAtNoon(String placeId) {

        PersistableBundleCompat bundleCompat = new PersistableBundleCompat();
        bundleCompat.putString("PLACE_ID", placeId);

        new JobRequest.Builder(NotificationJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(12), TimeUnit.HOURS.toMillis(13))
                .setExtras(bundleCompat)
                .build()
                .schedule();
    }


    private void sendNotification(String selectedRestaurantId){

        // Create intent for DisplaySearchActivity
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("PLACE_ID", selectedRestaurantId);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                        getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification
        NotificationHelper notificationHelper =
                new NotificationHelper(getContext());
        NotificationCompat.Builder builder =
                notificationHelper.getNotificationBuilder(
                        "Go 4 Lunch",
                        "See where to lunch",
                        pendingIntent);
        notificationHelper.getNotificationManager().notify(notificationID, builder.build());

    }
}
