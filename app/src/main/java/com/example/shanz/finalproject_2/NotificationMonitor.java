package com.example.shanz.finalproject_2;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;
import android.widget.Toast;
import static com.example.shanz.finalproject_2.SecondActivity.toReply;

public class NotificationMonitor extends NotificationListenerService {
    private static int count = 0;
    String phoneNumber = null;
    @Override
    public void onListenerConnected() {
        Toast.makeText(getApplicationContext(), "System Ready!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!MainActivity.doNotDisturb) {
            return;
        }
        count++;
        if (count != 1) {
            count = 0;
            return;
        }
//        Toast.makeText(getApplicationContext(), sbn.getPackageName(), Toast.LENGTH_SHORT).show();
        if ("com.android.mms".equals(sbn.getPackageName())) {
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = "";
            String content = null;
            if (extras != null) {
                title = extras.getString(Notification.EXTRA_TITLE, "");
//                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                content = extras.getString(Notification.EXTRA_TEXT, "");
            }
            // ! Cursor part Credit : https://blog.csdn.net/wangjiangjun0815/article/details/56488709.
            Cursor cursor = getApplicationContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (title.equals(contactName)) {
                    Cursor phone = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    if (phone.moveToNext()) {
                        phoneNumber
                                = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;
                    }
                } else {
                    phoneNumber = title;
                }
            }
           sendSms();
//            if (phoneNumber != null) {
//                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0,
//                        new Intent("SMS_SENT_ACTION"), 0);
//
//                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0,
//                        new Intent("SMS_DELIVERED_ACTION"), 0);
//                smsManager.sendTextMessage(phoneNumber, null, "Hi!!!!!!!!!!!", sentIntent, deliveredIntent);
//            }
        }
    }

//    public void onNotificationRemoved(StatusBarNotification sbn) {
//        return;
//    }
    private void sendSms() {
//        String SENT = "SMS_SENT";
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        SmsManager smsMgr = SmsManager.getDefault();
        try {
            smsMgr.sendTextMessage(phoneNumber, null, toReply, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*count++;
            try {
                String SENT = "SMS_SENT";

                PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        int resultCode = getResultCode();
                        switch (resultCode) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                SmsManager smsMgr = SmsManager.getDefault();
                smsMgr.sendTextMessage(phoneNumber, null, "Master is busy right now,sry.", sentPI, null);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage() + "!\n" + "Failed to send SMS", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }*/
    }
}