package by.matthewvirus.medicinenotifier.receivers

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.util.*

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, HomeActivity::class.java)
        val medicineName = intent?.extras?.getString("id")
        val medicineCellNumber = intent?.extras?.getInt("cell")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context!!,0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = when (medicineCellNumber) {
            0 -> createNotification(context, medicineName, pendingIntent)
            else -> createNotificationForContainer(context, medicineName, medicineCellNumber, pendingIntent)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(context: Context?,
                                         name: String?,
                                         intent: PendingIntent?
    ) : Notification {
        return NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_message) + " " + name)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .build()
    }

    private fun createNotificationForContainer(context: Context,
                                               name: String?,
                                               number: Int?,
                                               intent: PendingIntent?
    ) : Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_message) + " " + name?.trim() + "\n" + context.getString(R.string.cell_number) + " " + number)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .build()
    }
}