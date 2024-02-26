package com.example.devicecontrols.services

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.devicecontrols.interfaces.NotificationListenerInterface
import com.example.devicecontrols.utils.getAppNameFromPackageName
import com.example.devicecontrols.utils.isBanking

class NotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        Log.d("NotificationListener", "Package Name: $packageName")
        if(isBanking(packageName)) {
            val notification = sbn.notification
            val notificationText = notification?.extras?.getCharSequence("android.text") ?: ""
            mListener?.notificationReceived("Sender: ${getAppNameFromPackageName(applicationContext, packageName)} \nMessage: $notificationText")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    companion object {
        private var mListener: NotificationListenerInterface? = null
        fun bindListener(listener: NotificationListenerInterface?) {
            mListener = listener
        }
    }
}