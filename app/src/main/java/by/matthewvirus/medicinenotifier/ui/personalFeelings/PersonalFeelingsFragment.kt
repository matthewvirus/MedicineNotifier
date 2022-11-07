package by.matthewvirus.medicinenotifier.ui.personalFeelings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import by.matthewvirus.medicinenotifier.R

class PersonalFeelingsFragment : Fragment() {

    companion object {
        fun newInstance() = PersonalFeelingsFragment()
    }

    private lateinit var viewModel: PersonalFeelingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.personal_feelings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalFeelingsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}