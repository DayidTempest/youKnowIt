package com.bluescript.youknowit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ManageUserReply extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Check user answer
        NotificationsTemplates.answerNotification(context, "Prawidłowa odpowiedź", "Super!");
    }
}
