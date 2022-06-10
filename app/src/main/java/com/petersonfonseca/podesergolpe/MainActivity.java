package com.petersonfonseca.podesergolpe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Context context;

    //String userDb;
    private static final String TAG = "MainActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String userFirebaseFinal7;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica usuário
        SharedPreferences preferences7 = PreferenceManager.getDefaultSharedPreferences(this);
        userFirebaseFinal7 = preferences7.getString("userFirebaseFinal7", "fail");
        Log.i("Endereco: 1 => ", userFirebaseFinal7);
        if (userFirebaseFinal7 == "fail") {
            SharedPreferences preferences3 = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences3.edit();
            Random aleatorio = new Random();
            int valor = aleatorio.nextInt(10000) + 1;
            String valorString = Integer.toString(valor);
            editor.putString("userFirebaseFinal7", valorString);
            editor.apply();
            myRef.child("usuarios").child(valorString).setValue(false);
            FirebaseMessaging.getInstance().subscribeToTopic(valorString);
            Log.i("Endereco: 2 => ", valorString);

            Log.i("SHA2 - Usuario criado", userFirebaseFinal7);
        } else {
            Log.i("SHA1 - Usuario ja existia", userFirebaseFinal7);
        }

        checkIfAppUsageAccess();

        AppCenter.start(getApplication(), "133f04d1-ebd5-4508-9a9e-add81cfa0a4e",
                Analytics.class, Crashes.class);

        ImageView imageViewSeg = findViewById(R.id.seguranca_ic);
        TextView textSeg = findViewById(R.id.seguranca_text);
        ImageView imageViewFeb = findViewById(R.id.febraban_ic);
        TextView textFeb = findViewById(R.id.febraban_text);

        imageViewSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http.s://www.caixa.gov.br/seguranca";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.caixa.gov.br/seguranca";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        imageViewFeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://antifraudes.febraban.org.br/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textFeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://antifraudes.febraban.org.br/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void checkIfAppUsageAccess(){
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        if (Build.VERSION.SDK_INT >= 21 && !granted) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

            if (stats == null || stats.isEmpty()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}