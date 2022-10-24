package by.matthewvirus.medicinenotifier.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import java.util.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicinedatamodel")
    fun getMedicines(): LiveData<List<MedicineDataModel>>

    @Query("SELECT * FROM medicinedatamodel WHERE medicineId=(:id)")
    fun getMedicine(id: UUID): LiveData<MedicineDataModel?>

    @Insert
    fun addMedicine(medicine: MedicineDataModel)

    @Query("DELETE FROM medicinedatamodel WHERE medicineId=(:id)")
    fun deleteMedicine(id: UUID)
}