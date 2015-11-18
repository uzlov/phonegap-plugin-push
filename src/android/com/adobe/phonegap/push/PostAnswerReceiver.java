package com.adobe.phonegap.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PostAnswerReceiver extends BroadcastReceiver implements PushConstants {
    private static String LOG_TAG = "PushPlugin_PostAnswerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "onReceive PostAnswer action");
        String appName = intent.getStringExtra(APP_NAME);
        int notId = intent.getIntExtra(NOT_ID, -1);

        Intent PostAnswerService = new Intent(context, PostAnswerService.class);
        PostAnswerService.putExtra("servicesUrl", intent.getStringExtra("servicesUrl"));
        PostAnswerService.putExtra("servicesToken", intent.getStringExtra("servicesToken"));
        PostAnswerService.putExtra("questionNid", intent.getStringExtra("questionNid"));
        PostAnswerService.putExtra("answerTid", intent.getStringExtra("answerTid"));
        PostAnswerService.putExtra("data", intent.getStringExtra("data"));
        context.startService(PostAnswerService);

        // Cancel notification.
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(appName, notId);
    }
}
