package by.matthewvirus.medicinenotifier.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Medicine (

    @PrimaryKey(autoGenerate = true)
    var medicineId: Int = 0,
    var medicineName: String = "",
    var medicineNumberInContainer: Int = 0,
    var medicineMinNumberRemind: Int = 0,
    var medicineDose: Int = 0,
    var medicineUseTimesPerDay: String = "",
    var medicineUseTimesPerDayInt: Int = 0,
    var medicineStatus: Int = 0,
    var medicineTakingFirstTime: Date = Date(),
    var isStoredInContainer: Boolean = false,
    var cellNumber: Int = 0
)