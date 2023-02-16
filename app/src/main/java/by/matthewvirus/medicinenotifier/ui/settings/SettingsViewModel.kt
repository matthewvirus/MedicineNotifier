package by.matthewvirus.medicinenotifier.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import by.matthewvirus.medicinenotifier.data.domain.MedicineNotifierDatabase
import by.matthewvirus.medicinenotifier.data.domain.repository.SettingsRepository
import by.matthewvirus.medicinenotifier.data.model.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository: SettingsRepository
    private val readSettings: LiveData<Settings>

    init {
        val medicineDB = MedicineNotifierDatabase.getDatabase(application)
        settingsRepository = SettingsRepository(medicineDB.settingsDao())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                settingsRepository.createDefaultSettingsObject()
            } catch (_: Exception) {
                
            }
        }
        readSettings = settingsRepository.getSettings()
    }

    fun updateSettings(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateSettings(settings)
        }
    }

    fun getSettings(): LiveData<Settings> =
        readSettings
}