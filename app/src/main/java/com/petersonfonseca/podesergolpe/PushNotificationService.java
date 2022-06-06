package com.petersonfonseca.podesergolpe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
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

public class PushNotificationService extends FirebaseMessagingService {
    private String CHANNEL_ID = "1";
    private String CHANNEL_ID_DOIS = "2";

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

        }
    }

    private void showNotificationSom() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID_DOIS)
                        .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                        .setContentTitle("Alerta de Possível golpe.")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("Você recebeu uma ligação de um número que não esta na sua agenda e agora abriu o app da Caixa. Cuidado. Pode ser golpe.");
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

}
