package com.bluescript.youknowit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ManageNotifications {
    public static Intent serviceIntent = null;
    NotificationsService notificationsService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            NotificationsService.NotificationsServiceBinder binder = (NotificationsService.NotificationsServiceBinder) service;
            notificationsService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private void createAndBindService(Context context) {
        serviceIntent = new Intent(context, NotificationsService.class);
        context.startService(serviceIntent);
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private void startNotificationTask(Context context) {
        notificationsService.createNewNotificationTask("test", 5, context);
    }

    private void stopNotificationTask(String id) {
        notificationsService.removeNotificationTask(id);
    }
}
