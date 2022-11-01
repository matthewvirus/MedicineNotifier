package by.matthewvirus.medicinenotifier.ui.aboutMedicine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AboutMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.util.HOUR_IN_MILLIS
import by.matthewvirus.medicinenotifier.util.INDEX_ARGUMENT
import com.google.android.material.snackbar.Snackbar

class AboutMedicineFragment :
    Fragment(),
    HomeActivity.Callbacks
{

    private var index: Int? = 0
    private lateinit var bindingAboutMedicineFragment: AboutMedicineFragmentBinding
    private lateinit var spinner: Spinner
    private var medicine = MedicineDataModel()
    private var delayTimeInMillis: Long = 0
    private var userTimesPerDayChoice = ""
    private var userTimesPerDayChoiceInt = 0
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

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

    private fun updateUI(medicine: MedicineDataModel) {
        bindingAboutMedicineFragment.medicineNameInput.setText(medicine.medicineName)
        bindingAboutMedicineFragment.medicineNumberInContainerInput.setText(medicine.medicineNumberInContainer.toString())
        bindingAboutMedicineFragment.medicineCriticalNumberInput.setText(medicine.medicineMinNumberRemind.toString())
        bindingAboutMedicineFragment.medicineDoseInput.setText(medicine.medicineDose.toString())
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
            selectSpinnerItem()
        }
    }

    private fun selectSpinnerItem() {
        bindingAboutMedicineFragment.medicineTimesPerDaySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {
                when(id) {
                    0L -> delayTimeInMillis = 24 * HOUR_IN_MILLIS
                    1L -> delayTimeInMillis = 6 * HOUR_IN_MILLIS
                    2L -> delayTimeInMillis = 4 * HOUR_IN_MILLIS
                    3L -> delayTimeInMillis = 3 * HOUR_IN_MILLIS
                }
                val choice = resources.getStringArray(R.array.times_per_day)
                userTimesPerDayChoice = choice[position]
                userTimesPerDayChoiceInt = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    private fun setUpBinding(inflater: LayoutInflater, container: ViewGroup?) {
        bindingAboutMedicineFragment = AboutMedicineFragmentBinding.inflate(inflater, container, false)
    }

    private fun createMedicineToUpdate(): MedicineDataModel {
        medicine.medicineName = bindingAboutMedicineFragment.medicineNameInput.text.toString()
        medicine.medicineNumberInContainer = bindingAboutMedicineFragment.medicineNumberInContainerInput.text.toString().toInt()
        medicine.medicineMinNumberRemind = bindingAboutMedicineFragment.medicineCriticalNumberInput.text.toString().toInt()
        medicine.medicineDose = bindingAboutMedicineFragment.medicineDoseInput.text.toString().toInt()
        medicine.medicineUseTimesPerDay = userTimesPerDayChoice
        medicine.medicineUseTimesPerDayInt = userTimesPerDayChoiceInt
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

    private fun setUpTakeMedicineButton() {
        bindingAboutMedicineFragment.takeMedicineButton.apply {
            setOnClickListener {
                if (medicine.medicineNumberInContainer > 0 && medicine.medicineNumberInContainer >= medicine.medicineMinNumberRemind) {
                    medicine.medicineNumberInContainer -= medicine.medicineDose
                } else if (medicine.medicineNumberInContainer < medicine.medicineMinNumberRemind) {
                    createSnackBar(R.string.zero_error)
                } else {
                    medicine.medicineNumberInContainer = 0
                    createSnackBar(R.string.zero_error)
                }
                AboutMedicineViewModel().updateMedicine(medicine)
                returnToHomeActivity()
            }
        }
    }

    private fun deleteMedicine() {
        AboutMedicineViewModel().deleteMedicine(medicine)
        Snackbar.make(requireView(), R.string.item_deleted, Snackbar.LENGTH_SHORT).show()
        returnToHomeActivity()
    }

    private fun pauseNotification() {
        val medicineToUpdate = createMedicineToUpdate()
        if (medicineToUpdate.medicineStatus == 0) {
            createSnackBar(R.string.item_already_paused)
            return
        }
        medicineToUpdate.medicineStatus = 0
        AboutMedicineViewModel().updateMedicine(medicineToUpdate)
        cancelPendingIntent()
        createSnackBar(R.string.item_paused)
        returnToHomeActivity()
    }

    private fun continueNotification() {
        val medicineToUpdate = createMedicineToUpdate()
        if (medicineToUpdate.medicineStatus == 1) {
            createSnackBar(R.string.item_already_working)
            return
        }
        medicineToUpdate.medicineStatus = 1
        AboutMedicineViewModel().updateMedicine(medicineToUpdate)
        cancelPendingIntent()
        createSnackBar(R.string.notifications_active)
        returnToHomeActivity()
    }

    private fun cancelPendingIntent() {
        val intent = Intent(context, AboutMedicineFragment::class.java)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent = PendingIntent.getService(context, index!!, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.cancel()
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