package by.matthewvirus.medicinenotifier.data.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import by.matthewvirus.medicinenotifier.data.model.Settings

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE id=1")
    fun getSettings(): LiveData<Settings>

    @Query("INSERT INTO settings(id, mqttServer, mqttServerPort, isEnabled) VALUES (1, 'mqtt.broker', 8883, false)")
    fun createDefaultSettingsObject()

    @Update
    fun updateSettings(settings: Settings)
}