package com.petersonfonseca.podesergolpe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class PushNotificationService extends FirebaseMessagingService {
    private String CHANNEL_IDDD = "11";
    private String CHANNEL_ID_DOIS = "2";
    Context context;

    @Override
    public void onNewToken (String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        String refreshedToken = s;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("token");
        reference.setValue(refreshedToken);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0){
            Map<String, String> payload = remoteMessage.getData();
            showNotificationSom();
            Log.i("RECEBIDA", "recebida");
        }
    }

    private void showNotificationSom() {NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my_channel11";
            String Description = "This is my channel11";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_IDDD, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_IDDD)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Atenção - Pode ser golpe")
                .setContentText("Momentos atrás você recebeu uma ligação de um numero...");

        Intent resultIntent = new Intent();
        resultIntent.setComponent(new ComponentName(this, ShowCallActivity.class));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(new ComponentName(this, ShowCallActivity.class));
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(new Random().nextInt(), builder.build());

    }

}
