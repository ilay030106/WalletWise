package com.example.walletwise.Spendings.SpendingScreens;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.walletwise.R;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;

public class MonthlyExpService extends Service {
    String CHANNEL_ID="Monthly Expense";
    String CHANNEL_NAME="Monthly Expense Notification";
    String desc=" ",day=" ",date=" ",message=" ";
    //יום בחודש =day
    //date = חודש ושנה



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "OnCreate");


        // we will add a notification here later
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        desc = intent.getStringExtra("desc");
        date  = intent.getStringExtra("monthAndYear");
        day  = intent.getStringExtra("dayOfMonth");
        message="ההוצאה שלך על " + desc +  "ב " + day + "בכל חודש נוספה למערכת לחודש "+ date;
        getNotification("לא שכחת ממני נכון? זאת ההוצאה החודשית שלך ",message,false);
        return super.onStartCommand(intent, flags, startId);
    }

    public void createNotificationChannel(){
        if(Build.VERSION_CODES.O<=Build.VERSION.SDK_INT){
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    private Notification getNotification(String title,String message, boolean usedForStartForeground){
        createNotificationChannel();
        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.spending_tracker_icon)
                        .setContentTitle(title)
                        .setContentText(message);
                        //.setWhen(System.currentTimeMillis());

        ;

        Intent notifiactionIntent= new Intent(this, AppScreen.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,notifiactionIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        if(!usedForStartForeground){
            NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());
        }
        return builder.build();


    }
}
