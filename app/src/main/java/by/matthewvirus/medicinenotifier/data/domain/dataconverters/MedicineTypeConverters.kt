package by.matthewvirus.medicinenotifier.data.domain.dataconverters

import androidx.room.TypeConverter
import java.util.*

class MedicineTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}