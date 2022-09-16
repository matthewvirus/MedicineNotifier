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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
    private var medicineAdapter: MedicineAdapter? = MedicineAdapter(emptyList())

    private val medicinesListViewModel by lazy {
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
        return bindingMedicineListFragment.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        medicinesListViewModel.medicinesLiveData.observe(
            viewLifecycleOwner
        ) { medicines ->
            medicines?.let {
                updateUI(medicines)
            }
        }
    }

    private fun updateUI(medicines: List<MedicineDataModel>) {
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

    private inner class MedicineAdapter(var medicines: List<MedicineDataModel>)
        : RecyclerView.Adapter<MedicineAdapter.MedicineHolder>() {

        private inner class MedicineHolder(view: View)
            : RecyclerView.ViewHolder(view), View.OnClickListener {

            private lateinit var medicine: MedicineDataModel
            private val medicineNameTitle: TextView =
                itemView.findViewById(R.id.medicine_item_name)
            private val medicineStatusTitle: TextView =
                itemView.findViewById(R.id.medicine_item_status)
            private val medicineNumberInContainerTitle: TextView =
                itemView.findViewById(R.id.medicine_item_number_in_container)
            val expandedLayout: RelativeLayout =
                itemView.findViewById(R.id.card_view_expanded_layout)
            val mainLayout: ConstraintLayout =
                itemView.findViewById(R.id.card_view_main_layout)

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

            override fun onClick(view: View?) {}

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineHolder {
            val view = layoutInflater.inflate(viewType, parent, false)
            return MedicineHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: MedicineHolder, position: Int) {
            val medicine = medicines[position]
            holder.bind(medicine)

            val isExpanded = medicines[position].isExpanded
            holder.expandedLayout.visibility = when(isExpanded) {
                true -> View.VISIBLE
                false -> View.GONE
            }

            holder.mainLayout.setOnClickListener {
                medicine.isExpanded = !medicine.isExpanded
                notifyItemChanged(position)
            }
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