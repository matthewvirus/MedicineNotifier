package by.matthewvirus.medicinenotifier.data.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.matthewvirus.medicinenotifier.data.domain.dao.MedicineDao
import by.matthewvirus.medicinenotifier.data.domain.dao.SettingsDao
import by.matthewvirus.medicinenotifier.data.domain.dataconverters.Converters
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.data.model.Settings
import by.matthewvirus.medicinenotifier.util.DATABASE_NAME

@Database(entities = [Medicine::class, Settings::class], version = 1)
@TypeConverters(Converters::class)
abstract class MedicineNotifierDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun settingsDao(): SettingsDao

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