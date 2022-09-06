package by.matthewvirus.medicinenotifier.ui.medicinelist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.matthewvirus.medicinenotifier.R

class MedicineListFragment : Fragment() {

    companion object {
        fun newInstance() = MedicineListFragment()
    }

    private lateinit var viewModel: MedicineListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.medicine_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MedicineListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}