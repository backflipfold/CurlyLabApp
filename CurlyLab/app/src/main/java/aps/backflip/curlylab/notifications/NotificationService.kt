package aps.backflip.curlylab.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Title: ${it.title}")
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "VIK"
    }
}
