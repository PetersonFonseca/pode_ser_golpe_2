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
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String[] patterns = {"0800", "pix", "urgente", "reconhece", "transacao", "regularize", "premio", "receber", "sorteado", "ganhar", "ganhou", "itau", "nubank", "bradesco", "santander", "bloqueio", "bloqueado", "bloqueada", "supensao", "suspencao", "suspençao", "atualize ja", "reconhecer", "cancelar", "cancele", "fraude", "fraudes", "conta", "seguro", "segurança", "invadida", "caixa", "cancelar", "banco", "sequestro", "cef", "ted", "doc", "sequestramos", "filho", "link", "site", "filhos", "dinheiro", "transferencia", "reconhece", "desbloquear"};
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
                                if (msgBody.contains("0800")) {
                                    countFromAlert++;
                                }
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
                                    .setContentText("O SMS recebido de: " + msg_from + " contém palavras suspeitas." + " Cuidado, pode ser golpe.");

                            Intent resultIntent = new Intent(context, ShowMessageActivity2.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                            resultIntent.putExtra("numberTelephone", msg_from);
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(resultPendingIntent);
                            if(msg_from != "29193" && msg_from != "29194" && msg_from != "29196" && msg_from != "29015" && msg_from != "29111" && msg_from != "29197") {
                                notificationManager.notify(new Random().nextInt(), builder.build());
                            }

                        } else {
                            Log.i("SEG", "ELSE");
                            List<String> extractedUrls = extractUrls(msgBody);

                            for (String url : extractedUrls)
                            {
                                Log.i("SEG", url);
                                if (url.length() > 3) {
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
                                            .setContentText("O SMS recebido de: " + msg_from + " contém um link." + " Cuidado, pode ser golpe.");

                                    Intent resultIntent = new Intent(context, ShowMessageActivity.class);
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                                    resultIntent.putExtra("numberTelephone", msg_from);
                                    stackBuilder.addParentStack(MainActivity.class);
                                    stackBuilder.addNextIntent(resultIntent);
                                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(resultPendingIntent);
                                    if(msg_from != "29193" && msg_from != "29194" && msg_from != "29196" && msg_from != "29015" && msg_from != "29111" && msg_from != "29197") {
                                        notificationManager.notify(new Random().nextInt(), builder.build());
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

}
