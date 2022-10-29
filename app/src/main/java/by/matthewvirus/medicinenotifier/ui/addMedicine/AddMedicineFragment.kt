package by.matthewvirus.medicinenotifier.ui.addMedicine

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AddMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.receivers.AlarmReceiver
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.ui.dialogs.TimePickerFragment
import by.matthewvirus.medicinenotifier.util.DIALOG_TIME
import by.matthewvirus.medicinenotifier.util.HOUR_IN_MILLIS
import by.matthewvirus.medicinenotifier.util.REQUEST_TIME
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
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private var userTimesPerDayChoice = ""
    private val medicine = MedicineDataModel()
    private var delayTimeInMillis: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingAddPatientFragment = AddMedicineFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        hideBottomNavigationView(true)
        return bindingAddPatientFragment.root
    }

    override fun hideBottomNavigationView(flag: Boolean) {
        (requireActivity() as HomeActivity).hideBottomNavigationView(flag)
    }

    override fun onTimeSelected(time: Date) {
        medicine.medicineTakingFirstTime = time
        updateTime()
    }

    private fun applyForAllElements() {
        bindingAddPatientFragment.firstNotificationTime.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        setTime(Date())
        setMedicineTimesPerDayAdapter()
        selectFirstTime()
        createNotification()
        setUpListeners()
    }

    private fun isValid(): Boolean =
        validateMedicineName() && validateMedicineNumberInContainer() && validateMedicineCriticalNumber() && validateMedicineDose()

    private fun setUpListeners() {
        bindingAddPatientFragment.medicineNameInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineNameInput))
        bindingAddPatientFragment.medicineNumberInContainerInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineNumberInContainerInput))
        bindingAddPatientFragment.medicineCriticalNumberInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineCriticalNumberInput))
        bindingAddPatientFragment.medicineDoseInput.addTextChangedListener(TextValidation(bindingAddPatientFragment.medicineDoseInput))
    }

    private fun updateTime() {
        setTime(medicine.medicineTakingFirstTime)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setTime(timeToFormat: Date) {
        bindingAddPatientFragment.firstNotificationTime.text =
            getString(R.string.first_notification_time_question) + " " +
                SimpleDateFormat(TIME_FORMAT).format(timeToFormat)
    }

    private fun setMedicineTimesPerDayAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times_per_day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingAddPatientFragment.medicineTimesPerDaySpinner.adapter = adapter
            selectSpinnerItem()
        }
    }

    private fun selectSpinnerItem() {
        bindingAddPatientFragment.medicineTimesPerDaySpinner.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

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
                }
            }
    }

    private fun selectFirstTime() {
        bindingAddPatientFragment.firstNotificationTime.setOnClickListener {
            TimePickerFragment.newInstance(medicine.medicineTakingFirstTime).apply {
                setTargetFragment(
                    this@AddMedicineFragment,
                    REQUEST_TIME)
                show(
                    this@AddMedicineFragment.requireFragmentManager(),
                    DIALOG_TIME)
            }
        }
    }

    private fun createMedicine(): MedicineDataModel {
        val medicine = MedicineDataModel()
        medicine.medicineName = bindingAddPatientFragment.medicineNameInput.text.toString()
        medicine.medicineNumberInContainer = bindingAddPatientFragment.medicineNumberInContainerInput.text.toString().toInt()
        medicine.medicineMinNumberRemind = bindingAddPatientFragment.medicineCriticalNumberInput.text.toString().toInt()
        medicine.medicineDose = bindingAddPatientFragment.medicineDoseInput.text.toString().toInt()
        medicine.medicineUseTimesPerDay = userTimesPerDayChoice
        medicine.medicineTakingFirstTime = this.medicine.medicineTakingFirstTime
        return medicine
    }

    private fun createNotification() {
        bindingAddPatientFragment.createMedicineNotificationButton.setOnClickListener {
            if (isValid()) {
                AddMedicineViewModel().addMedicine(createMedicine())
                activity?.supportFragmentManager?.popBackStack()
                startAlarm(context)
            }
        }
    }

    private fun startAlarm(context: Context?) {
        val timeInMillis = MedicineDataModel().medicineTakingFirstTime.time
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, delayTimeInMillis, pendingIntent)
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

    private fun validateMedicineNumberInContainer(): Boolean {
        if (bindingAddPatientFragment.medicineNumberInContainerInput.text.toString().trim().isEmpty()) {
            bindingAddPatientFragment.medicineNumberInContainerLayout.error = getString(R.string.items_error)
            bindingAddPatientFragment.medicineNumberInContainerInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineNumberInContainerLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateMedicineCriticalNumber(): Boolean {
        if (bindingAddPatientFragment.medicineCriticalNumberInput.text.toString().trim().isEmpty()) {
            bindingAddPatientFragment.medicineCriticalNumberLayout.error = getString(R.string.critical_error)
            bindingAddPatientFragment.medicineCriticalNumberInput.requestFocus()
            return false
        } else {
            bindingAddPatientFragment.medicineCriticalNumberLayout.isErrorEnabled = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigationView(false)
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
            }
        }
    }
}