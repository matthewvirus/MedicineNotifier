package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.util.INDEX_ARGUMENT

class AboutMedicineFragment :
    Fragment(),
    HomeActivity.Callbacks
{

    private var index: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        hideBottomNavigationView(true)
        index = arguments?.getInt(INDEX_ARGUMENT)
        Log.d("Tag", index.toString())
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