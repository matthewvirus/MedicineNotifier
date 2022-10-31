package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AboutMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.util.INDEX_ARGUMENT
import com.google.android.material.snackbar.Snackbar

class AboutMedicineFragment :
    Fragment(),
    HomeActivity.Callbacks
{

    private var index: Int? = 0
    private lateinit var bindingAboutMedicineFragment: AboutMedicineFragmentBinding
    private var medicine = MedicineDataModel()

    private val aboutMedicineViewModel by lazy {
        ViewModelProvider(this)[AboutMedicineViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setUpMenuProvider()
        hideBottomNavigationView(true)
        getIndexFromParentFragment()
        setUpBinding(inflater, container)
        setUpUpdateMedicineButton()
        return bindingAboutMedicineFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutMedicineViewModel.getMedicines().observe(
            viewLifecycleOwner
        ) {
            medicines ->
            medicines?.let {
                medicine = medicines[index!!]
                updateUI(medicine)
            }
        }
    }

    override fun hideBottomNavigationView(flag: Boolean) {
        (requireActivity() as HomeActivity).hideBottomNavigationView(flag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigationView(false)
    }

    private fun setUpMenuProvider() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_pause -> {
                        pauseNotification()
                        true
                    }
                    R.id.action_delete -> {
                        stopNotification()
                        deleteMedicine()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUI(medicine: MedicineDataModel) {
        bindingAboutMedicineFragment.medicineNameInput.setText(medicine.medicineName)
        bindingAboutMedicineFragment.medicineCriticalNumberInput.setText(medicine.medicineMinNumberRemind.toString())
        bindingAboutMedicineFragment.medicineDoseInput.setText(medicine.medicineDose.toString())
    }

    private fun setUpBinding(inflater: LayoutInflater, container: ViewGroup?) {
        bindingAboutMedicineFragment = AboutMedicineFragmentBinding.inflate(inflater, container, false)
    }

    private fun createMedicineToUpdate(): MedicineDataModel {
        medicine.medicineName = bindingAboutMedicineFragment.medicineNameInput.text.toString()
        medicine.medicineMinNumberRemind = bindingAboutMedicineFragment.medicineCriticalNumberInput.text.toString().toInt()
        medicine.medicineDose = bindingAboutMedicineFragment.medicineDoseInput.text.toString().toInt()
        return medicine
    }

    private fun setUpUpdateMedicineButton() {
        bindingAboutMedicineFragment.updateMedicineNotificationButton.apply {
            setOnClickListener {
                AboutMedicineViewModel().updateMedicine(createMedicineToUpdate())
                returnToHomeActivity()
            }
        }
    }

    private fun deleteMedicine() {
        AboutMedicineViewModel().deleteMedicine(medicine)
        Snackbar.make(requireView(), R.string.item_deleted, Snackbar.LENGTH_SHORT).show()
        returnToHomeActivity()
    }

    private fun stopNotification() {
        // TODO: Not implemented yet
    }

    private fun pauseNotification() {
        Snackbar.make(requireView(), R.string.item_paused, Snackbar.LENGTH_SHORT).show()
        returnToHomeActivity()
    }

    private fun getIndexFromParentFragment() {
        index = arguments?.getInt(INDEX_ARGUMENT)
    }

    private fun returnToHomeActivity() {
        activity?.supportFragmentManager?.popBackStack()
    }
}