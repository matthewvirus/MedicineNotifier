package by.matthewvirus.medicinenotifier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository
import by.matthewvirus.medicinenotifier.util.CHANNEL_DESCRIPTION
import by.matthewvirus.medicinenotifier.util.CHANNEL_ID
import by.matthewvirus.medicinenotifier.util.CHANNEL_NAME

class MedicineNotifierApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MedicineRepository.initialize(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = CHANNEL_NAME
        val description = CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}