package by.matthewvirus.medicinenotifier.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import by.matthewvirus.medicinenotifier.util.ARG_TIME
import java.util.*

@Suppress("DEPRECATION")
class TimePickerFragment: DialogFragment() {

    interface Callbacks {

        fun onTimeSelected(time: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val timeListener = TimePickerDialog.OnTimeSetListener {
            _: TimePicker, hour: Int, minute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onTimeSelected(calendar.time)
            }
        }
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    companion object {

        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }

            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}