package by.matthewvirus.medicinenotifier.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import java.util.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicinedatamodel")
    fun getMedicines(): LiveData<List<MedicineDataModel>>

    @Insert
    fun addMedicine(medicine: MedicineDataModel)

    @Delete
    fun deleteMedicine(medicine: MedicineDataModel)

    @Update
    fun updateMedicine(medicine: MedicineDataModel)
}