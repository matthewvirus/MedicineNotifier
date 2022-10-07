package by.matthewvirus.medicinenotifier.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MedicineDataModel(
    @PrimaryKey
    var medicineId: UUID = UUID.randomUUID(),
    var medicineName: String = "",
    var medicineStatus: Boolean = false,
    var medicineType: String = "",
    var medicineNumberInContainer: Int = 0,
    var medicineMinNumberRemind: Int = 0,
    var medicineUseTimesPerDay: String = "",
    var medicineTakingFirstTime: Date = Date(),
    var isEndedForToday: Boolean = false
)