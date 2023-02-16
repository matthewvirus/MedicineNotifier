package by.matthewvirus.medicinenotifier.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Medicine (

    @PrimaryKey(autoGenerate = true)
    var medicineId: Int = 0,

    var medicineName: String = "",
    var medicineDose: Int = 0,
    var medicineUseTimesPerDay: String = "",
    var medicineUseTimesPerDayInt: Int = 0,
    var medicineTakingFirstTime: Date = Date(),
    var currentlyTaken: Int = 0,

    var medicineStatus: Int = 1,

    var isStoredInContainer: Boolean = false,
    var medicineNumberInContainer: Int = 0,
    var medicineMinNumberRemind: Int = 0,
    var cellNumber: Int = 0
)