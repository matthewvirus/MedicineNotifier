package by.matthewvirus.medicinenotifier.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Doctor (

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var status: String,
    var email: String
)
