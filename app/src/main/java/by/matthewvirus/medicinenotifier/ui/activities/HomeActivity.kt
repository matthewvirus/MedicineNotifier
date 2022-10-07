package by.matthewvirus.medicinenotifier.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.databinding.ActivityHomeBinding
import by.matthewvirus.medicinenotifier.ui.addMedicine.AddMedicineFragment
import by.matthewvirus.medicinenotifier.ui.medicinesList.MedicineListFragment

class HomeActivity : AppCompatActivity(), MedicineListFragment.Callbacks {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        setupWithNavController(homeBinding.bottomNavigationView, navController)
//
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment)
//
//        if (currentFragment == null) {
//            val fragment = MedicineListFragment.newInstance()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.activity_main_nav_host_fragment, fragment)
//                .commit()
//        }
    }

    override fun onPatientGonnaBeAdded() {
        val fragment = AddMedicineFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}