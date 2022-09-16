package by.matthewvirus.medicinenotifier

import android.app.Application
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class MedicineNotifierApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MedicineRepository.initialize(this)
    }

}