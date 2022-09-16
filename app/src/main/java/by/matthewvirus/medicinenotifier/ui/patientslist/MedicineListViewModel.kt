package by.matthewvirus.medicinenotifier.ui.patientslist

import androidx.lifecycle.ViewModel
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.data.repository.MedicineRepository

class MedicineListViewModel : ViewModel() {

//    var medicines = mutableListOf<MedicineDataModel>()
//
//    init {
//        for (i in 0 until 10) {
//            val medicine = MedicineDataModel()
//            medicine.medicineName = "Medicine $i"
//            medicine.medicineStatus = i % 2 == 0
//            medicine.medicineNumberInContainer = i
//            medicines += medicine
//        }
//    }

    private val medicineRepository = MedicineRepository.get()
    val medicinesLiveData = medicineRepository.getMedicines()

}