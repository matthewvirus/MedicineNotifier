package by.matthewvirus.medicinenotifier.ui.addpatient

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.AddMedicineFragmentBinding
import by.matthewvirus.medicinenotifier.ui.dialogs.DatePickerFragment
import by.matthewvirus.medicinenotifier.util.DATE_FORMAT
import by.matthewvirus.medicinenotifier.util.DIALOG_DATE
import by.matthewvirus.medicinenotifier.util.REQUEST_DATE
import java.text.SimpleDateFormat
import java.util.*

class AddMedicineFragment
    : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var bindingAddPatientFragment: AddMedicineFragmentBinding

    private val medicine = MedicineDataModel()

    private val addPatientViewModel by lazy {
        ViewModelProvider(this)[AddMedicineViewModel::class.java]
    }

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

    override fun onDateSelected(date: Date) {
        medicine.medicineTakingEndDate = date
        updateUI()
    }

    private fun applyForAllElements() {
        setMedicineTimesPerDayAdapter()
        selectEndDate()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun updateUI() {
        bindingAddPatientFragment.endDate.text =
            getString(R.string.starting_date) +
                    SimpleDateFormat(DATE_FORMAT).format(medicine.medicineTakingEndDate)
    }

    private fun setMedicineTimesPerDayAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times_per_day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingAddPatientFragment.medicineTimesPerDaySpinner.adapter = adapter
        }
    }

    private fun selectEndDate() {
        bindingAddPatientFragment.endDate.setOnClickListener {
            DatePickerFragment.newInstance(medicine.medicineTakingEndDate).apply {
                setTargetFragment(this@AddMedicineFragment, REQUEST_DATE)
                show(
                    this@AddMedicineFragment
                        .requireParentFragment()
                        .parentFragmentManager,
                    DIALOG_DATE)
            }
        }
    }

    companion object {
        fun newInstance() = AddMedicineFragment()
    }

}