package by.matthewvirus.medicinenotifier.ui.addMedicine

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AddMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.receivers.AlarmReceiver
import by.matthewvirus.medicinenotifier.ui.activities.HomeActivity
import by.matthewvirus.medicinenotifier.ui.dialogs.TimePickerFragment
import by.matthewvirus.medicinenotifier.util.DIALOG_TIME
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
    private var flags = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingAddPatientFragment =
            AddMedicineFragmentBinding.inflate(inflater, container, false)
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
        medicine.medicineName =
            bindingAddPatientFragment.medicineNameInput.text.toString()
        medicine.medicineNumberInContainer =
            bindingAddPatientFragment.medicineNumberInContainerInput.text.toString().toInt()
        medicine.medicineMinNumberRemind =
            bindingAddPatientFragment.medicineCriticalNumberInput.text.toString().toInt()
        medicine.medicineDose -
                bindingAddPatientFragment.medicineDoseInput.text.toString().toInt()
        medicine.medicineUseTimesPerDay = userTimesPerDayChoice
        medicine.medicineTakingFirstTime = this.medicine.medicineTakingFirstTime
        return medicine
    }

    private fun createNotification() {
        bindingAddPatientFragment.createMedicineNotificationButton.setOnClickListener {
            AddMedicineViewModel().addMedicine(createMedicine())
            activity?.supportFragmentManager?.popBackStack()
            startAlarm(context)
        }
    }

    private fun startAlarm(context: Context?) {
        val timeInMillis = MedicineDataModel().medicineTakingFirstTime.time
        val delayTimeInMillis: Long = 600000
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, delayTimeInMillis, pendingIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigationView(false)
    }
}