package com.petersonfonseca.podesergolpe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.core.app.NotificationCompat;

import java.text.Normalizer;
import java.util.Random;

public class ReceiveSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String CHANNEL_ID = "golpe";
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            Integer countFromAlert = 0;
            String[] patterns = {"pix", "regularize", "premio", "receber", "sorteado", "ganhar", "ganhou", "itau", "nubank", "bradesco", "santander", "bloqueio", "bloqueado", "bloqueada", "supensao", "suspencao", "suspençao", "atualize ja", "reconhecer", "cancelar", "cancele", "fraude", "fraudes", "conta", "seguro", "segurança", "invadida", "caixa", "cancelar", "banco", "sequestro", "cef", "ted", "doc", "sequestramos", "filho", "link", "site", "filhos", "dinheiro", "transferencia", "reconhece", "desbloquear"};
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBodyWithCL = msgs[i].getMessageBody().toLowerCase();
                        String msgBody = Normalizer.normalize(msgBodyWithCL, Normalizer.Form.NFD);
                        msgBody = msgBody.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                        for (int p = 0; p < patterns.length; p++) {
                            if (msgBody.contains(patterns[p])) {
                                countFromAlert++;
                            }
                        }

                        if (countFromAlert != 0) {
                            int NOTIFICATION_ID = new Random().nextInt();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                CHANNEL_ID = "my_channel_02";
                                CharSequence name = "my_channel";
                                String Description = "This is my channel";
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                mChannel.setDescription(Description);
                                mChannel.enableLights(true);
                                mChannel.enableVibration(true);
                                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                mChannel.setShowBadge(false);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Atenção - Pode ser golpe")
                                    .setContentText("A SMS recebida de: " + msg_from + " parece ser uma tentativa de golpe." + " Cuidado.");

                            Intent resultIntent = new Intent(context, ShowMessageActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                            resultIntent.putExtra("numberTelephone", msg_from);
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(resultPendingIntent);
                            notificationManager.notify(new Random().nextInt(), builder.build());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
