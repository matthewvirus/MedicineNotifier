package by.matthewvirus.medicinenotifier.util

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.datamodel.MedicineDataModel
import java.text.SimpleDateFormat

class MedicineAdapter(
    private var medicines: List<MedicineDataModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MedicineAdapter.MedicineHolder>() {

    interface OnItemClickListener {
        fun onItemClick(medicine: MedicineDataModel, position: Int)
    }

    class MedicineHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private lateinit var medicine: MedicineDataModel
        private val medicineNameTitle: TextView = itemView.findViewById(R.id.medicine_item_name)
        private val medicineTakingTime: TextView = itemView.findViewById(R.id.medicines_times)
        private val medicineNumberInContainerTitle: TextView = itemView.findViewById(R.id.medicine_item_number_in_container)
        private val medicineStatusImageView: ImageView = itemView.findViewById(R.id.medicine_active_status)
        private val medicineWarningImageView: ImageView = itemView.findViewById(R.id.warning_image)
        private val medicineWarningTextView: TextView = itemView.findViewById(R.id.warning_text)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(medicine: MedicineDataModel) {
            this.medicine = medicine
            medicineNameTitle.text = this.medicine.medicineName
            medicineTakingTime.text = this.medicine.medicineUseTimesPerDay + itemView.context.getString(R.string.first_notification) + SimpleDateFormat(TIME_FORMAT).format(this.medicine.medicineTakingFirstTime)
            medicineNumberInContainerTitle.text = itemView.context.getString(R.string.medicines_left) + " " + this.medicine.medicineNumberInContainer.toString()
            when (medicine.medicineStatus) {
                0 -> {
                    medicineStatusImageView.setImageResource(R.drawable.ic_notif_off)
                    ImageViewCompat.setImageTintList(medicineStatusImageView, ColorStateList.valueOf(Color.parseColor("#ff5722")))
                }
                else -> {
                    medicineStatusImageView.setImageResource(R.drawable.ic_notif_on)
                    ImageViewCompat.setImageTintList(medicineStatusImageView, ColorStateList.valueOf(Color.parseColor("#8bc34a")))

                }
            }
            if (medicine.medicineNumberInContainer <= medicine.medicineMinNumberRemind) {
                medicineWarningImageView.visibility = View.VISIBLE
                medicineWarningTextView.visibility = View.VISIBLE
            } else {
                medicineWarningImageView.visibility = View.GONE
                medicineWarningTextView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MedicineHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MedicineHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineHolder, position: Int) {
        val medicine = medicines[position]
        holder.bind(medicine)
        holder.itemView.setOnClickListener {
            listener.onItemClick(medicine, position)
        }
    }

    override fun getItemCount() : Int {
        return medicines.size
    }

    override fun getItemViewType(position: Int) : Int {
        return R.layout.medicine_list_item
    }
}