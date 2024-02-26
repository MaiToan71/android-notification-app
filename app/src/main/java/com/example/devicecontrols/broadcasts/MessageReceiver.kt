package com.example.devicecontrols.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.devicecontrols.interfaces.MessageListenerInterface
import com.example.devicecontrols.utils.isBanking

class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
//            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
//            var address = ""
//            var message = "\n + Message:"
//            for (smsMessage in messages) {
//                address = smsMessage.displayOriginatingAddress
//                message += smsMessage.displayMessageBody
//            }
//            Log.e("Banking", address)
//            if (isBanking(address)) {
//                address = "Sender : $address"
//                mListener?.messageReceived(address + message)
//            }
        }
    }

    companion object {
        private var mListener: MessageListenerInterface? = null
        fun bindListener(listener: MessageListenerInterface?) {
            mListener = listener
        }
    }
}
