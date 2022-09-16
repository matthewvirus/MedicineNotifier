package by.matthewvirus.medicinenotifier.data.database.dataconverters

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

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(string: String?): UUID? {
        return UUID.fromString(string)
    }

}