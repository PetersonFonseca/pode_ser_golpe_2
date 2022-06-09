package com.petersonfonseca.podesergolpe;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class TestService extends Service {
    private static final String TAG = "Token firebase mensage";
    Context context;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TimerTask task3 = new TimerTask() {
            public void run() {
                Log.i(TAG, "onStartCommand");
                verifyApplicationRunning();
            }
        };
        Timer timer3 = new Timer("Timer");

        long delay3 = 10000;
        timer3.schedule(task3, delay3);


        // START_STICKY serve para executar seu serviço até que você pare ele, é reiniciado automaticamente sempre que termina
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void verifyApplicationRunning() {
        String currentApp = "NULL";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)this.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.d("Top App ******", "Current Top Running App is: " + currentApp);
        String app_caixa = "br.com.gabba.Caixa";
        if (currentApp.intern() == app_caixa) {
            Log.i("NOTFIC", "App caixa aberto");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userFirebaseFinal = preferences.getString("userFirebaseFinal", "fail");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            Log.i(TAG, "onStartCommand true");
            myRef.child("usuarios").child(userFirebaseFinal).setValue(true);

            TimerTask task2 = new TimerTask() {
                public void run() {
                    Log.i(TAG, "onStartCommand false");
                    myRef.child("usuarios").child(userFirebaseFinal).setValue(false);
                    //getApplicationContext().stopService(new Intent(context, TestService.class));
                }
            };
            Timer timer2 = new Timer("Timer");

            long delay2 = 15000;
            timer2.schedule(task2, delay2);

        }


    }
}
