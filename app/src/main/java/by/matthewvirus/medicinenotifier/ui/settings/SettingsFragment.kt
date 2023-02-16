package by.matthewvirus.medicinenotifier.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.medicinenotifier.R
import by.matthewvirus.medicinenotifier.data.model.Settings
import by.matthewvirus.medicinenotifier.data.mqtt.MqttClient
import by.matthewvirus.medicinenotifier.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsFragmentBinding: SettingsFragmentBinding
    private var settingsModel: Settings = Settings()
    lateinit var mqttClient: MqttClient

    private val settingsViewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        activity?.title = getString(R.string.settings)
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false)
        getSettingsFromDb()
        showOrHideSettingsForm()
        setUpUpdateMqttSettingsButton()
        mqttClient = MqttClient()
        establishMqttConnection()
        mqttDisconnection()
        return settingsFragmentBinding.root
    }

    private fun getSettingsFromDb() {
        settingsViewModel.getSettings().observe(
            viewLifecycleOwner
        ) { settings ->
            settings?.let {
                settingsModel = settings
                updateUI(settingsModel)
            }
        }
    }

    private fun updateUI(settings: Settings) {
        settingsFragmentBinding.mqttBrokerEdit.setText(settings.mqttServer)
        settingsFragmentBinding.mqttPortEdit.setText(settings.mqttServerPort.toString())
        settingsFragmentBinding.isEnabledSwitch.isChecked = settings.isEnabled
    }

    private fun getFormInfo(): Settings {
        settingsModel.mqttServer = settingsFragmentBinding.mqttBrokerEdit.text.toString()
        settingsModel.mqttServerPort = settingsFragmentBinding.mqttPortEdit.text.toString().toInt()
        return settingsModel
    }

    private fun establishMqttConnection() {
        settingsFragmentBinding.mqttConnectionButton.apply {
            setOnClickListener {
                mqttClient.connect(context, settingsModel)
                Toast.makeText(context, getText(R.string.connected), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mqttDisconnection() {
        settingsFragmentBinding.mqttDisconnectionButton.apply {
            setOnClickListener {
                try {
                    mqttClient.disconnect()
                    Toast.makeText(context, getText(R.string.disconnected), Toast.LENGTH_SHORT).show()
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun setUpUpdateMqttSettingsButton() {
        settingsFragmentBinding.updateSettingsButton.apply {
            setOnClickListener {
                settingsViewModel.updateSettings(getFormInfo())
            }
        }
    }

    private fun switchOnOffSelection() {
        if (settingsFragmentBinding.isEnabledSwitch.isChecked) {
            settingsFragmentBinding.settingsLayout.visibility = View.VISIBLE
            settingsModel.isEnabled = true
        } else {
            settingsFragmentBinding.settingsLayout.visibility = View.GONE
            settingsModel.isEnabled = false
        }
    }

    private fun showOrHideSettingsForm() {
        switchOnOffSelection()
        settingsFragmentBinding.isEnabledSwitch.setOnCheckedChangeListener { _, _ ->
            showOrHideSettingsForm()
        }
    }
}