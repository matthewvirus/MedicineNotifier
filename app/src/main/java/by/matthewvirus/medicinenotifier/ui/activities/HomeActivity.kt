package by.matthewvirus.medicinenotifier.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.addpatient.AddMedicineFragment
import by.matthewvirus.medicinenotifier.ui.patientslist.MedicineListFragment

class HomeActivity : AppCompatActivity(), MedicineListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = MedicineListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onPatientGonnaBeAdded() {
        val fragment = AddMedicineFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}