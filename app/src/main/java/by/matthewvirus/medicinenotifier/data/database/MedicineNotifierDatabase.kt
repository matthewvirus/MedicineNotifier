package by.matthewvirus.medicinenotifier.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.matthewvirus.medicinenotifier.data.database.dao.MedicineDao
import by.matthewvirus.medicinenotifier.data.database.dataconverters.MedicineTypeConverters
import by.matthewvirus.medicinenotifier.data.datamodel.Medicine

@Database(entities = [Medicine::class], version = 1)
@TypeConverters(MedicineTypeConverters::class)
abstract class MedicineNotifierDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
}