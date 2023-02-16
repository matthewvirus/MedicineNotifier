package by.matthewvirus.medicinenotifier.ui.addMedicine

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.databinding.AddMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.ui.dialogs.TimePickerFragment
import by.matthewvirus.medicinenotifier.util.AlarmUtils.Companion.createNotificationCode
import by.matthewvirus.medicinenotifier.util.AlarmUtils.Companion.startAlarm
import by.matthewvirus.medicinenotifier.util.DIALOG_TIME
import by.matthewvirus.medicinenotifier.util.REQUEST_TIME
import by.matthewvirus.medicinenotifier.util.SpinnerUtils.Companion.selectSpinnerItem
import by.matthewvirus.medicinenotifier.util.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddMedicineFragment :
    Fragment(),
    TimePickerFragment.Callbacks,
    HomeActivity.Callbacks
{

    private lateinit var bindingAddPatientFragment: AddMedicineFragmentBinding
    private var medicineTime = Date()
    private var notificationCode = 0
    private var isMedicineStoredInContainer: Boolean = false

    private val addMedicineViewModel by lazy {
        ViewModelProvider(this)[AddMedicineViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        bindingAddPatientFragment = AddMedicineFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        hideBottomNavigationView(true)
        bindingAddPatientFragment.isContainerUsed.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bindingAddPatientFragment.hiddenFormForContainer.visibility = View.VISIBLE
                isMedicineStoredInContainer = true
            } else {
                bindingAddPatientFragment.hiddenFormForContainer.visibility = View.GONE
                isMedicineStoredInContainer = false
            }
        }
        activity?.title = getString(R.string.add_medicine)
        return bindingAddPatientFragment.root
    }

    override fun hideBottomNavigationView(flag: Boolean) {
        (requireActivity() as HomeActivity).hideBottomNavigationView(flag)
    }

    override fun onTimeSelected(time: Date) {
        medicineTime = time
        updateTime()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigationView(false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun applyForAllElements() {
        setFirstTime(Date())
        setMedicineTimesPerDayAdapter()
        selectFirstTime()
        createNotification()
        setUpListeners()
        notificationCode = createNotificationCode(
            addMedicineViewModel.getMedicines(),
            viewLifecycleOwner
        )
    }

    private fun updateTime() {
        setFirstTime(medicineTime)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setFirstTime(timeToFormat: Date) {
        bindingAddPatientFragment.firstNotificationTime.text = SimpleDateFormat(TIME_FORMAT).format(timeToFormat)
    }

    private fun setMedicineTimesPerDayAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times_per_day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingAddPatientFragment.medicineTimesPerDaySpinner.adapter = adapter
            selectSpinnerItem(bindingAddPatientFragment.medicineTimesPerDaySpinner, resources)
        }
    }

    private fun selectFirstTime() {
        bindingAddPatientFragment.firstNotificationTime.setOnClickListener {
            TimePickerFragment.newInstance(medicineTime).apply {
                setTargetFragment(
                    this@AddMedicineFragment,
                    REQUEST_TIME)
                show(
                    this@AddMedicineFragment.requireFragmentManager(),
                    DIALOG_TIME)
            }
        }
    }

    private fun getCreatedMedicine(): Medicine {
        val medicine = Medicine()
        medicine.medicineName = bindingAddPatientFragment.medicineNameInput.text.toString()
        medicine.medicineDose = bindingAddPatientFragment.medicineDoseInput.text.toString().toInt()
        medicine.medicineUseTimesPerDay = selectSpinnerItem(bindingAddPatientFragment.medicineTimesPerDaySpinner, resources).userTimesPerDayChoice
        medicine.medicineUseTimesPerDayInt = selectSpinnerItem(bindingAddPatientFragment.medicineTimesPerDaySpinner, resources).userTimesPerDayChoiceInt
        medicine.medicineTakingFirstTime = medicineTime
        medicine.medicineStatus = 1
        medicine.isStoredInContainer = isMedicineStoredInContainer
        if (isMedicineStoredInContainer) {
            medicine.medicineNumberInContainer = bindingAddPatientFragment.medicineNumberInContainerInput.text.toString().toInt()
            medicine.medicineMinNumberRemind = bindingAddPatientFragment.medicineCriticalNumberInput.text.toString().toInt()
            medicine.cellNumber = bindingAddPatientFragment.medicineCellInput.text.toString().toInt()
        }
        return medicine
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createNotification() {
        bindingAddPatientFragment.createMedicineNotificationButton.setOnClickListener {
            if (isValid()) {
                addMedicineViewModel.addMedicine(getCreatedMedicine())
                activity?.supportFragmentManager?.popBackStack()
                startAlarm(
                    context,
                    getCreatedMedicine(),
                    notificationCode,
                    bindingAddPatientFragment.medicineTimesPerDaySpinner,
                    resources
                )
            }
        }
    }

    private fun isValid(): Boolean =
        validateMedicineName() &&
                validateMedicineNumberInContainer() &&
                validateMedicineCriticalNumber() &&
                validateMedicineDose() &&
                validateStoreCell()

    private fun setUpListeners() {
        bindingAddPatientFragment.medicineNameInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineNameInput))
        bindingAddPatientFragment.medicineDoseInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineDoseInput))
        if (isMedicineStoredInContainer) {
            bindingAddPatientFragment.medicineDoseInput.addTextChangedListener(TextValidation(
                bindingAddPatientFragment.medicineCellInput))
            bindingAddPatientFragment.medicineNumberInContainerInput.addTextChangedListener(
                TextValidation(bindingAddPatientFragment.medicineNumberInContainerInput))
            bindingAddPatientFragment.medicineCriticalNumberInput.addTextChangedListener(
                TextValidation(bindingAddPatientFragment.medicineCriticalNumberInput))
        }
    }

    private fun validateMedicineName(): Boolean {
        if (bindingAddPatientFragment.medicineNameInput.text.toString().trim().isEmpty()) {
            bindingAddPatientFragment.medicineNameLayout.error = getString(R.string.name_error)
            bindingAddPatientFragment.medicineNameInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineNameLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateMedicineDose(): Boolean {
        if (bindingAddPatientFragment.medicineDoseInput.text.toString().trim().isEmpty()) {
            bindingAddPatientFragment.medicineDoseLayout.error = getString(R.string.dose_error)
            bindingAddPatientFragment.medicineDoseInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineDoseLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateMedicineNumberInContainer(): Boolean {
        if (bindingAddPatientFragment.medicineNumberInContainerInput.text.toString().trim().isEmpty() && isMedicineStoredInContainer) {
            bindingAddPatientFragment.medicineNumberInContainerLayout.error = getString(R.string.items_error)
            bindingAddPatientFragment.medicineNumberInContainerInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineNumberInContainerLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateMedicineCriticalNumber(): Boolean {
        if (bindingAddPatientFragment.medicineCriticalNumberInput.text.toString().trim().isEmpty() && isMedicineStoredInContainer) {
            bindingAddPatientFragment.medicineCriticalNumberLayout.error = getString(R.string.critical_error)
            bindingAddPatientFragment.medicineCriticalNumberInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineCriticalNumberLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateStoreCell(): Boolean {
        val medicineCellText = bindingAddPatientFragment.medicineCellInput.text.toString()
        if ((medicineCellText.trim().isEmpty() || isCellContainsMedicine(medicineCellText) || (medicineCellText.toInt() > 7 || medicineCellText.toInt() < 1)) && isMedicineStoredInContainer) {
            bindingAddPatientFragment.medicineCellLayout.error =
                getString(R.string.cell_error)
            bindingAddPatientFragment.medicineCellInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineCellLayout.isErrorEnabled = false
        }
        return true
    }

    private fun isCellContainsMedicine(medicineCellText: String): Boolean {
        val medicines = addMedicineViewModel.getMedicines().value
        if (medicines != null) {
            for (medicine in medicines) {
                if (medicine.cellNumber == medicineCellText.toInt()) {
                    return true
                } else {
                    continue
                }
            }
        }
        return false
    }

    inner class TextValidation(private val view: View) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.medicine_name_input -> {
                    validateMedicineName()
                }
                R.id.medicine_number_in_container_input -> {
                    validateMedicineNumberInContainer()
                }
                R.id.medicine_critical_number_input -> {
                    validateMedicineCriticalNumber()
                }
                R.id.medicine_dose_input -> {
                    validateMedicineDose()
                }
                R.id.medicine_cell_input -> {
                    validateStoreCell()
                }
            }
        }
    }
}