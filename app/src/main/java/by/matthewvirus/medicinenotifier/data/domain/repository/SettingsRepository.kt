package by.matthewvirus.medicinenotifier.data.domain.repository

import androidx.lifecycle.LiveData
import by.matthewvirus.medicinenotifier.data.domain.dao.SettingsDao
import by.matthewvirus.medicinenotifier.data.model.Settings

class SettingsRepository (private val settingsDao: SettingsDao) {

    fun getSettings(): LiveData<Settings> =
        settingsDao.getSettings()

    fun updateSettings(settings: Settings) =
        settingsDao.updateSettings(settings)

    fun createDefaultSettingsObject() =
        settingsDao.createDefaultSettingsObject()
}