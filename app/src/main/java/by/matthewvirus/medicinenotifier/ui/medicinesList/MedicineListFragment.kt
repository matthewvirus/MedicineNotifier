package by.matthewvirus.medicinenotifier.ui.medicinesList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.model.Medicine
import by.matthewvirus.medicinenotifier.databinding.MedicineListFragmentBinding
import by.matthewvirus.medicinenotifier.ui.aboutMedicine.AboutMedicineFragment
import by.matthewvirus.medicinenotifier.ui.activities.Communicator
import by.matthewvirus.medicinenotifier.ui.addMedicine.AddMedicineFragment
import by.matthewvirus.medicinenotifier.util.MedicineAdapter

class MedicineListFragment :
    Fragment(),
    MedicineAdapter.OnItemClickListener
{

    interface Callbacks {
        fun onFragmentTransition(fragment: Fragment)
    }

    private var callbacks: Callbacks? = null
    private lateinit var communicator: Communicator
    private lateinit var bindingMedicineListFragment: MedicineListFragmentBinding
    private var medicineAdapter: MedicineAdapter? = MedicineAdapter(emptyList(), this)

    private val medicinesListViewModel by lazy {
        ViewModelProvider(this)[MedicineListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) : View {
        setUpBinding(inflater, container)
        applyForAllElements()
        setUpCommunicator()
        activity?.title = getString(R.string.treatment)
        return bindingMedicineListFragment.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        medicinesListViewModel.getMedicines().observe(
            viewLifecycleOwner
        ) { medicines ->
            medicines?.let {
                updateUI(medicines)
            }
        }
    }

    private fun updateUI(medicines: List<Medicine>) {
        medicineAdapter = MedicineAdapter(medicines, this)
        bindingMedicineListFragment.medicineRecyclerView.adapter = medicineAdapter
        updateViewWhenRecyclerViewIsNotEmpty(bindingMedicineListFragment.emptyView, bindingMedicineListFragment.emptyDraw)
    }

    private fun updateViewWhenRecyclerViewIsNotEmpty(vararg views: View) {
        for (view in views) {
            view.visibility = when(medicineAdapter?.itemCount) {
                0 -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun applyForAllElements() {
        recyclerViewSettings()
        addNewMedicineButtonSettings()
    }

    private fun setUpBinding(inflater: LayoutInflater, container: ViewGroup?) {
        bindingMedicineListFragment = MedicineListFragmentBinding.inflate(inflater, container, false)
    }

    private fun recyclerViewSettings() {
        bindingMedicineListFragment.medicineRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = medicineAdapter
        }
    }

    private fun addNewMedicineButtonSettings() {
        bindingMedicineListFragment.addNewMedicine.apply {
            setOnClickListener {
                callbacks?.onFragmentTransition(AddMedicineFragment())
            }
        }
    }

    private fun setUpCommunicator() {
        communicator = requireActivity() as Communicator
    }

    override fun onItemClick(medicine: Medicine, position: Int) {
        val fragment = AboutMedicineFragment()
        fragment.arguments = communicator.passArgumentsBetweenFragments(position)
        callbacks?.onFragmentTransition(fragment)
    }
}