package by.matthewvirus.medicinenotifier.ui.patientslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.matthewvirus.medicinenotifier.R

class PatientsListFragment : Fragment() {

    companion object {
        fun newInstance() = PatientsListFragment()
    }

    private lateinit var viewModel: PatientsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.patients_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PatientsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}