package com.petersonfonseca.podesergolpe;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import java.util.List;

public class JobDetectCallOut extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        doBackgroundWork(jobParameters);

        return true;
    }

    private void doBackgroundWork(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                verifyApplicationRunning();
                Log.d(TAG, "run: ");




                if (jobCancelled) {
                    return;
                }
                Log.d(TAG, "Job finished");
                jobFinished(jobParameters, true);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
    public void verifyApplicationRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        String nomeDosApps;
        for (int i = 0; i < procInfos.size(); i++) {
            nomeDosApps = procInfos.get(i).processName.toString();
            Log.i("Nomes2", nomeDosApps);
            //Log.i("NUM_PROC", Integer.toString(procInfos.size()));


            if (procInfos.get(i).processName.equals("com.caixa.podesergolpe_sms")) {
                //Log.i("APP_BANCO", "Um aplicativo bancario foi aberto");
            }
        }
    }
}
