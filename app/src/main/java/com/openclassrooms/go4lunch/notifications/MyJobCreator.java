package com.openclassrooms.go4lunch.notifications;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;

public class MyJobCreator implements com.evernote.android.job.JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {

        switch (tag) {
            case NotificationJob.TAG:
                return new NotificationJob();
            default:
                return null;
        }
    }
}
