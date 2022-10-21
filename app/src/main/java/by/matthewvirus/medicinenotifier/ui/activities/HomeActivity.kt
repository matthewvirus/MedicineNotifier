package by.matthewvirus.medicinenotifier.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.databinding.ActivityHomeBinding
import by.matthewvirus.medicinenotifier.ui.addMedicine.AddMedicineFragment
import by.matthewvirus.medicinenotifier.ui.medicinesList.MedicineListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), MedicineListFragment.Callbacks {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        bottomNavigationView = homeBinding.bottomNavigationView

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        setupWithNavController(homeBinding.bottomNavigationView, navController)
    }

    override fun onFragmentTransition(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    interface Callbacks {
        fun hideBottomNavigationView(flag: Boolean)
    }

    fun hideBottomNavigationView(flag: Boolean) {
        bottomNavigationView.visibility = when(flag) {
            true -> View.GONE
            false -> View.VISIBLE
        }
    }
}