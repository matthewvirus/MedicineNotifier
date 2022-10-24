package by.matthewvirus.medicinenotifier.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.addMedicine.AddMedicineFragment
import by.matthewvirus.medicinenotifier.util.*

class AlarmReceiver: BroadcastReceiver() {

    private var flag = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, AddMedicineFragment::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0, notificationIntent, flag)
        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_treatment)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}