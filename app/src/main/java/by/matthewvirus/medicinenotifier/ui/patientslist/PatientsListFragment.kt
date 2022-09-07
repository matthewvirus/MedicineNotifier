package by.matthewvirus.medicinenotifier.ui.patientslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.PatientDataModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PatientsListFragment : Fragment() {

    private lateinit var patientsListRecyclerView: RecyclerView
    private lateinit var addPatientFloatingActionButton: FloatingActionButton
    private var adapter: PatientAdapter? = null

    private val patientsListViewModel by lazy {
        ViewModelProvider(this).get(PatientsListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =  inflater.inflate(R.layout.patients_list_fragment, container, false)
        patientsListRecyclerView = view.findViewById(R.id.patients_recycler_view) as RecyclerView
        patientsListRecyclerView.layoutManager = LinearLayoutManager(context)
        addPatientFloatingActionButton = view.findViewById(R.id.add_new_patient)
        updateUI()
        return view
    }

    private fun updateUI() {
        val patients = patientsListViewModel.patientsList
        adapter = PatientAdapter(patients)
        patientsListRecyclerView.adapter = adapter
    }

    private inner class PatientHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var patient: PatientDataModel
        private val patientNameTitle: TextView = itemView.findViewById(R.id.patient_item_name)
        private val patientSurnameTitle: TextView = itemView.findViewById(R.id.patient_item_surname)
        private val patientStatusIcon: ImageView = itemView.findViewById(R.id.patient_status_ic)

        fun bind(patient: PatientDataModel) {
            this.patient = patient
            patientNameTitle.text = this.patient.patientName
            patientSurnameTitle.text = this.patient.patientSurname
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