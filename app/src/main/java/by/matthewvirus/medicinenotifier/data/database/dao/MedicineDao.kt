package by.matthewvirus.medicinenotifier.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import java.util.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicinedatamodel")
    fun getMedicines(): LiveData<List<MedicineDataModel>>

    @Query("SELECT * FROM medicinedatamodel WHERE medicineId=(:id)")
    fun getMedicine(id: Int): LiveData<MedicineDataModel?>

    @Insert
    fun addMedicine(medicine: MedicineDataModel)

    @Query("DELETE FROM medicinedatamodel WHERE medicineId=(:id)")
    fun deleteMedicine(id: Int)

    @Query("UPDATE medicinedatamodel SET medicineName = :medicineName, medicineMinNumberRemind = :medicineMinNumberRemind, medicineDose = :medicineDose, medicineUseTimesPerDay = :medicineUseTimesPerDay, medicineTakingFirstTime = :medicineTakingFirstTime WHERE medicineId = :medicineId")
    fun updateMedicine(
        medicineName: String,
        medicineMinNumberRemind: Int,
        medicineDose: Int,
        medicineUseTimesPerDay: String,
        medicineTakingFirstTime: Date,
        medicineId: Int
    )
}