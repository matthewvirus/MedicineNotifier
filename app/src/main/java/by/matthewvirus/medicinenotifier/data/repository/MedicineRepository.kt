package by.matthewvirus.medicinenotifier.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import by.matthewvirus.medicinenotifier.data.database.MedicineNotifierDatabase
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.util.DATABASE_NAME
import java.util.*
import java.util.concurrent.Executors

class MedicineRepository private constructor(context: Context) {

    private val database: MedicineNotifierDatabase = Room.databaseBuilder(
        context.applicationContext,
        MedicineNotifierDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val medicineDao = database.medicineDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getMedicines(): LiveData<List<MedicineDataModel>> = medicineDao.getMedicines()
    fun getMedicine(id: UUID): LiveData<MedicineDataModel?> = medicineDao.getMedicine(id)

    fun addMedicine(medicine: MedicineDataModel) {
        executor.execute {
            medicineDao.addMedicine(medicine)
        }
    }

    companion object {
        private var INSTANCE: MedicineRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MedicineRepository(context)
            }
        }

        fun get(): MedicineRepository {
            return INSTANCE ?:
                throw IllegalStateException("Medicine repository must be initialized!")
        }
    }
}