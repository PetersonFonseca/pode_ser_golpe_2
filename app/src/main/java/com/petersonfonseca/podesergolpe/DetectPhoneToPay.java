package com.petersonfonseca.podesergolpe;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DetectPhoneToPay extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            //String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if((state.equals(TelephonyManager.EXTRA_STATE_RINGING))){
                String fromNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if(fromNumber != null) {
                    Log.i("Numero chamando", fromNumber);

                    String contactName = null;
                    if (fromNumber.startsWith("+"))
                    {
                        fromNumber = "0" + fromNumber.substring(3);
                    }
                    ContentResolver contentResolver = context.getContentResolver();

                    Uri uri = ContactsContract.Data.CONTENT_URI;
                    String[] projection = new String[] {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
                    String selection = "REPLACE (" + ContactsContract.CommonDataKinds.Phone.NUMBER + ", \" \" , \"\" ) = ?";
                    String[] selectionArgs = { fromNumber };
                    Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);

                    if (cursor.moveToFirst())
                    {
                        contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        cursor.close();
                        Log.i("NOME_CONTATO", contactName);
                    }
                    else
                    {
                        cursor.close();
                        verifyApplicationRunning2(context);
                        Log.i("NOME_CONTATO", "Não encontrado");
                    }
                }
            }
        }
    }

    public void verifyApplicationRunning2(Context context) {
        context.startForegroundService(new Intent(context, TestService.class));
    }
}
