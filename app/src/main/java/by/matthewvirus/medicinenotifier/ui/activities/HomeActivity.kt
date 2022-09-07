package by.matthewvirus.medicinenotifier.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.patientslist.PatientsListFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = PatientsListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}