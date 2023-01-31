package by.matthewvirus.medicinenotifier.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.matthewvirus.medicinenotifier.data.database.dao.MedicineDao
import by.matthewvirus.medicinenotifier.data.database.dataconverters.MedicineTypeConverters
import by.matthewvirus.medicinenotifier.data.datamodel.Doctor
import by.matthewvirus.medicinenotifier.data.datamodel.Medicine
import by.matthewvirus.medicinenotifier.data.datamodel.Patient
import by.matthewvirus.medicinenotifier.util.DATABASE_NAME

@Database(entities = [Medicine::class, Patient::class, Doctor::class], version = 1)
@TypeConverters(MedicineTypeConverters::class)
abstract class MedicineNotifierDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao

    companion object {

        @Volatile
        private var INSTANCE: MedicineNotifierDatabase? = null

        fun getDatabase(context: Context) : MedicineNotifierDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicineNotifierDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}