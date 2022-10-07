package by.matthewvirus.medicinenotifier.ui.addMedicine

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AddMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.dialogs.TimePickerFragment
import by.matthewvirus.medicinenotifier.util.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddMedicineFragment
    : Fragment(), TimePickerFragment.Callbacks {

    private lateinit var bindingAddPatientFragment: AddMedicineFragmentBinding
    private var userTimesPerDayChoice = ""

    private val medicine = MedicineDataModel()

    private val addMedicineViewModel by lazy {
        ViewModelProvider(this)[AddMedicineViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingAddPatientFragment =
            AddMedicineFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        return bindingAddPatientFragment.root
    }

    override fun onTimeSelected(time: Date) {
        medicine.medicineTakingFirstTime = time
        updateTime()
    }

    private fun applyForAllElements() {
        setMedicineTimesPerDayAdapter()
        selectFirstTime()
        createNotification()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun updateTime() {
        bindingAddPatientFragment.firstNotificationTime.text =
            getString(R.string.first_notification_time_question) + " " +
                    SimpleDateFormat(TIME_FORMAT).format(medicine.medicineTakingFirstTime)
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
                    Toast.makeText( context,
                        "Your choice is $position",
                        Toast.LENGTH_SHORT).show()
                    userTimesPerDayChoice = choice[position]
                }
            }
    }

    private fun selectFirstTime() {
        bindingAddPatientFragment.firstNotificationTime.setOnClickListener {
            TimePickerFragment.newInstance(medicine.medicineTakingFirstTime).apply {
                setTargetFragment(this@AddMedicineFragment, REQUEST_TIME)
                show(
                    this@AddMedicineFragment.
                            requireFragmentManager(),
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
        medicine.medicineUseTimesPerDay = userTimesPerDayChoice
        return medicine
    }

    private fun createNotification() {
        bindingAddPatientFragment.createMedicineNotificationButton.setOnClickListener {
            AddMedicineViewModel().addMedicine(createMedicine())
        }
    }

    companion object {
        fun newInstance() = AddMedicineFragment()
    }
}