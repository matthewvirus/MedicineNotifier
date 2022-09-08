package by.matthewvirus.medicinenotifier.ui.patientslist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.PatientDataModel
import by.matthewvirus.medicinenotifier.databinding.PatientsListFragmentBinding
import by.matthewvirus.medicinenotifier.util.DATE_PATTERN
import java.text.SimpleDateFormat

class PatientsListFragment : Fragment() {

    interface Callbacks {
        fun onPatientGonnaBeAdded()
    }

    private lateinit var bindingPatientsListFragment: PatientsListFragmentBinding

    private var callbacks: Callbacks? = null
    private var adapter: PatientAdapter? = null

    private val patientsListViewModel by lazy {
        ViewModelProvider(this)[PatientsListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingPatientsListFragment = PatientsListFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        updateUI()
        return bindingPatientsListFragment.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI() {
        val patients = patientsListViewModel.patientsList
        adapter = PatientAdapter(patients)
        bindingPatientsListFragment.patientsRecyclerView.adapter = adapter
    }

    private fun applyForAllElements() {
        bindingPatientsListFragment.patientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        bindingPatientsListFragment.addNewPatient.apply {
            setColorFilter(Color.argb(255, 255, 255, 255))
            setOnClickListener {
                callbacks?.onPatientGonnaBeAdded()
            }
        }
    }

    private inner class PatientHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var patient: PatientDataModel
        private val patientNameTitle: TextView = itemView.findViewById(R.id.patient_item_name)
        private val patientDateOfBirthTitle: TextView = itemView.findViewById(R.id.patient_item_date_of_birth)
        private val patientDiagnosisTitle: TextView = itemView.findViewById(R.id.patient_item_diagnosis)
        private val patientStatusIcon: ImageView = itemView.findViewById(R.id.patient_status_ic)

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(patient: PatientDataModel) {
            this.patient = patient
            patientNameTitle.text = this.patient.patientName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                patientDateOfBirthTitle.text = SimpleDateFormat(DATE_PATTERN).format(this.patient.dateOfBirth)
            }
            patientDiagnosisTitle.text = this.patient.patientDiagnosis
            when(this.patient.patientStatus) {
                true -> patientStatusIcon.setColorFilter(Color.argb(255, 0, 255, 0))
                false -> patientStatusIcon.setColorFilter(Color.argb(255, 255, 0, 0))
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

    }

    private inner class PatientAdapter(var patients: List<PatientDataModel>): RecyclerView.Adapter<PatientHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientHolder {
            val view = layoutInflater.inflate(viewType, parent, false)
            return PatientHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: PatientHolder, position: Int) {
            val patient = patients[position]
            holder.bind(patient)
        }

        override fun getItemCount(): Int {
            return patients.size
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.patient_list_item
        }

    }

    companion object {
        fun newInstance() = PatientsListFragment()
    }

}