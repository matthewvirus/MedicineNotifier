package by.matthewvirus.medicinenotifier.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (

    @PrimaryKey
    var id: Int = 1,
    var mqttServer: String = "",
    var mqttServerPort: Int = 0,
    var isEnabled: Boolean = false
)