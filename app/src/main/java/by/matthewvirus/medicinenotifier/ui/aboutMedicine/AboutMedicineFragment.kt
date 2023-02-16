package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.databinding.AboutMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.util.AlarmUtils.Companion.cancelPendingIntent
import by.matthewvirus.medicinenotifier.util.AlarmUtils.Companion.createNotificationCode
import by.matthewvirus.medicinenotifier.util.AlarmUtils.Companion.startAlarm
import by.matthewvirus.medicinenotifier.util.INDEX_ARGUMENT
import by.matthewvirus.medicinenotifier.util.SpinnerUtils.Companion.selectSpinnerItem
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AboutMedicineFragment :
    Fragment(),
    HomeActivity.Callbacks
{

    private var index: Int? = 0
    private lateinit var bindingAboutMedicineFragment: AboutMedicineFragmentBinding
    private lateinit var spinner: Spinner
    private var medicine = Medicine()
    private var notificationCode = 0

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
        setMedicineTimesPerDayAdapter()
        setUpUpdateMedicineButton()
        setUpTakeMedicineButton()
        notificationCode = createNotificationCode(
            aboutMedicineViewModel.getMedicines(),
            viewLifecycleOwner
        )
        activity?.title = getString(R.string.change_take)
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
                updateUI()
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

            @RequiresApi(Build.VERSION_CODES.S)
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_pause -> {
                        pauseNotification()
                        true
                    }
                    R.id.action_delete -> {
                        pauseNotification()
                        deleteMedicine()
                        true
                    }
                    R.id.action_resume -> {
                        continueNotification()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUI() {
        bindingAboutMedicineFragment.medicineNameInput.setText(medicine.medicineName)
        bindingAboutMedicineFragment.medicineNumberInContainerInput.setText(medicine.medicineNumberInContainer.toString())
        bindingAboutMedicineFragment.medicineCriticalNumberInput.setText(medicine.medicineMinNumberRemind.toString())
        bindingAboutMedicineFragment.medicineDoseInput.setText(medicine.medicineDose.toString())
        if (medicine.isStoredInContainer) {
            bindingAboutMedicineFragment.containerFormConstraint.visibility = View.VISIBLE
        }
        bindingAboutMedicineFragment.takeMedicineButton.isClickable = medicine.currentlyTaken != (medicine.medicineDose * medicine.medicineUseTimesPerDayInt)
        bindingAboutMedicineFragment.takeMedicineButton.isClickable = medicine.medicineStatus == 1
        setUpTheSpinner()
    }

    private fun setMedicineTimesPerDayAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times_per_day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingAboutMedicineFragment.medicineTimesPerDaySpinner.adapter = adapter
            selectSpinnerItem(bindingAboutMedicineFragment.medicineTimesPerDaySpinner, resources)
        }
    }

    private fun setUpBinding(inflater: LayoutInflater, container: ViewGroup?) {
        bindingAboutMedicineFragment = AboutMedicineFragmentBinding.inflate(inflater, container, false)
    }

    private fun createMedicineToUpdate(): Medicine {
        medicine.medicineName = bindingAboutMedicineFragment.medicineNameInput.text.toString()
        medicine.medicineNumberInContainer = bindingAboutMedicineFragment.medicineNumberInContainerInput.text.toString().toInt()
        medicine.medicineMinNumberRemind = bindingAboutMedicineFragment.medicineCriticalNumberInput.text.toString().toInt()
        medicine.medicineDose = bindingAboutMedicineFragment.medicineDoseInput.text.toString().toInt()
        medicine.medicineUseTimesPerDay = selectSpinnerItem(bindingAboutMedicineFragment.medicineTimesPerDaySpinner, resources).userTimesPerDayChoice
        medicine.medicineUseTimesPerDayInt = selectSpinnerItem(bindingAboutMedicineFragment.medicineTimesPerDaySpinner, resources).userTimesPerDayChoiceInt
        return medicine
    }

    private fun setUpUpdateMedicineButton() {
        bindingAboutMedicineFragment.updateMedicineNotificationButton.apply {
            setOnClickListener {
                aboutMedicineViewModel.updateMedicine(createMedicineToUpdate())
                returnToHomeActivity()
            }
        }
    }

    private fun setUpTakeMedicineButton() {
        bindingAboutMedicineFragment.takeMedicineButton.apply {
            setOnClickListener {
                if (medicine.isStoredInContainer) {
                    if (medicine.medicineNumberInContainer > 0 && medicine.medicineNumberInContainer >= medicine.medicineMinNumberRemind) {
                        medicine.medicineNumberInContainer -= medicine.medicineDose
                    } else if (medicine.medicineNumberInContainer < medicine.medicineMinNumberRemind) {
                        createSnackBar(R.string.zero_error)
                    } else {
                        medicine.medicineNumberInContainer = 0
                        createSnackBar(R.string.zero_error)
                    }
                }
                medicine.currentlyTaken += 1
                aboutMedicineViewModel.updateMedicine(medicine)
                returnToHomeActivity()
            }
        }
    }

    private fun deleteMedicine() {
        aboutMedicineViewModel.deleteMedicine(medicine)
        Snackbar.make(requireView(), R.string.item_deleted, Snackbar.LENGTH_SHORT).show()
        returnToHomeActivity()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun pauseNotification() {
        val medicineToUpdate = createMedicineToUpdate()
        if (medicineToUpdate.medicineStatus == 0) {
            createSnackBar(R.string.item_already_paused)
            return
        }
        medicineToUpdate.medicineStatus = 0
        aboutMedicineViewModel.updateMedicine(medicineToUpdate)
        cancelPendingIntent(context, index)
        createSnackBar(R.string.item_paused)
        returnToHomeActivity()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun continueNotification() {
        val medicineToUpdate = createMedicineToUpdate()
        if (medicineToUpdate.medicineStatus == 1) {
            createSnackBar(R.string.item_already_working)
            return
        }
        medicineToUpdate.medicineStatus = 1
        aboutMedicineViewModel.updateMedicine(medicineToUpdate)
        startAlarm(
            context,
            medicine,
            notificationCode,
            bindingAboutMedicineFragment.medicineTimesPerDaySpinner,
            resources
        )
        createSnackBar(R.string.notifications_active)
        returnToHomeActivity()
    }

    private fun createSnackBar(textResource: Int) {
        Snackbar.make(requireView(), textResource, Snackbar.LENGTH_SHORT).show()
    }

    private fun getIndexFromParentFragment() {
        index = arguments?.getInt(INDEX_ARGUMENT)
    }

    private fun returnToHomeActivity() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun setUpTheSpinner() {
        spinner = bindingAboutMedicineFragment.medicineTimesPerDaySpinner
        spinner.setSelection(medicine.medicineUseTimesPerDayInt)
    }
}