package by.matthewvirus.medicinenotifier.ui.patientslist

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import by.matthewvirus.medicinenotifier.databinding.MedicineListFragmentBinding

class MedicineListFragment : Fragment() {

    interface Callbacks {
        fun onPatientGonnaBeAdded()
    }

    private lateinit var bindingMedicineListFragment: MedicineListFragmentBinding

    private var callbacks: Callbacks? = null
    private var medicineAdapter: MedicineAdapter? = null

    private val patientsListViewModel by lazy {
        ViewModelProvider(this)[MedicineListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingMedicineListFragment =
            MedicineListFragmentBinding.inflate(inflater, container, false)
        applyForAllElements()
        updateUI()
        return bindingMedicineListFragment.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI() {
        val medicines = patientsListViewModel.medicines
        medicineAdapter = MedicineAdapter(medicines)
        bindingMedicineListFragment.medicineRecyclerView.adapter = medicineAdapter
    }

    private fun applyForAllElements() {
        recyclerViewSettings()
        addNewMedicineButtonSettings()
    }

    private fun recyclerViewSettings() {
        bindingMedicineListFragment.medicineRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = medicineAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun addNewMedicineButtonSettings() {
        bindingMedicineListFragment.addNewMedicine.apply {
            setColorFilter(Color.argb(255, 255, 255, 255))
            setOnClickListener {
                callbacks?.onPatientGonnaBeAdded()
            }
        }
    }

    private inner class MedicineHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var medicine: MedicineDataModel
        private val medicineNameTitle: TextView = itemView.findViewById(R.id.medicine_item_name)
        private val medicineStatusTitle: TextView = itemView.findViewById(R.id.medicine_item_status)
        private val medicineNumberInContainerTitle: TextView =
            itemView.findViewById(R.id.medicine_item_number_in_container)

        fun bind(medicine: MedicineDataModel) {
            this.medicine = medicine
            medicineNameTitle.text = getString(R.string.medicine_name, this.medicine.medicineName)
            medicineStatusTitle.text = getString(
                R.string.medicine_status,
                when(this.medicine.medicineStatus) {
                    true -> "принято"
                    false -> "не принято"
                })
            medicineNumberInContainerTitle.text = getString(
                R.string.medicine_num_in_container,
                this.medicine.medicineNumberInContainer)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

        }

    }

    private inner class MedicineAdapter(var medicines: List<MedicineDataModel>)
        : RecyclerView.Adapter<MedicineHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineHolder {
            val view = layoutInflater.inflate(viewType, parent, false)
            return MedicineHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: MedicineHolder, position: Int) {
            val medicine = medicines[position]
            holder.bind(medicine)
        }

        override fun getItemCount(): Int {
            return medicines.size
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.medicine_list_item
        }

    }

    companion object {
        fun newInstance() = MedicineListFragment()
    }

}