package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity

class AboutMedicineFragment :
    Fragment(),
    HomeActivity.Callbacks
{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        hideBottomNavigationView(true)
        return inflater.inflate(R.layout.fragment_about_medicine, container, false)
    }

    override fun hideBottomNavigationView(flag: Boolean) {
        (requireActivity() as HomeActivity).hideBottomNavigationView(flag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigationView(false)
    }
}