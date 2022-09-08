package by.matthewvirus.medicinenotifier.ui.addpatient

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.matthewvirus.medicinenotifier.data.datamodel.PatientDataModel
import by.matthewvirus.medicinenotifier.databinding.AddPatientFragmentBinding
import by.matthewvirus.medicinenotifier.ui.datepicker.DatePickerFragment
import by.matthewvirus.medicinenotifier.util.DATE_PATTERN
import by.matthewvirus.medicinenotifier.util.DIALOG_DATE
import by.matthewvirus.medicinenotifier.util.REQUEST_DATE
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class AddPatientFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var bindingAddPatientFragment: AddPatientFragmentBinding
    private var patient = PatientDataModel()

    private val addPatientViewModel by lazy {
        ViewModelProvider(this)[AddPatientViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingAddPatientFragment = AddPatientFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        return bindingAddPatientFragment.root
    }

    override fun onDateSelected(date: Date) {
        if (date.after(Date())) {
            Toast.makeText(context, "Выбрана неверная дата", Toast.LENGTH_SHORT).show()
        } else {
            patient.dateOfBirth = date
            updateUI()
        }
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private fun updateUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bindingAddPatientFragment.dateOfBirthText.text =
                SimpleDateFormat(DATE_PATTERN).format(patient.dateOfBirth)
        }
    }

    private fun applyForAllElements() {
        bindingAddPatientFragment.dateOfBirthPicker.apply {
            setOnClickListener {
                DatePickerFragment.newInstance(patient.dateOfBirth).apply {
                    setTargetFragment(this@AddPatientFragment, REQUEST_DATE)
                    show(this@AddPatientFragment.requireFragmentManager(), DIALOG_DATE)
                }
            }
        }
    }

    companion object {
        fun newInstance() = AddPatientFragment()
    }

}