package by.matthewvirus.medicinenotifier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import by.matthewvirus.medicinenotifier.util.CHANNEL_DESCRIPTION
import by.matthewvirus.medicinenotifier.util.CHANNEL_ID
import by.matthewvirus.medicinenotifier.util.CHANNEL_NAME

class MedicineNotifierApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = CHANNEL_DESCRIPTION
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}