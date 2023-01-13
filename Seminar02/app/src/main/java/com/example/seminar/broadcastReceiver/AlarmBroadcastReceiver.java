package com.example.seminar.broadcastReceiver;

/* Có 2 bước để tạo Alarm Manager trong Android
 * B1. Tạo 1 BroadcastReceiver bằng cách kế thừa nó
 * B2. Đăng ký alarm với Alarm hệ thống ALARM_SERVICE thông qua PendingItent kèm thời gian đặt trước
 * trong Android Manifest
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.seminar.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {


    String title, desc, date, time;
    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");

        // action intent là system event được định nghĩa
        // BOOT_COMPLETED: tín hiệu thông báo sau khi hệ thống kết thức boot
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Toast.makeText(context, "Alarm just rang...", Toast.LENGTH_SHORT).show();
//        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("201", "Channel1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "201")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(desc)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        ................
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(200, notification.build());
        notificationManager.notify(200, notification.build());


        Toast.makeText(context, "Broadcast receiver called", Toast.LENGTH_SHORT).show();
    }
}
