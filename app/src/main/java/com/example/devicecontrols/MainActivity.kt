package com.example.devicecontrols

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.devicecontrols.broadcasts.MessageReceiver
import com.example.devicecontrols.databinding.ActivityMainBinding
import com.example.devicecontrols.interfaces.MessageListenerInterface
import com.example.devicecontrols.interfaces.NotificationListenerInterface
import com.example.devicecontrols.overview.OverviewViewModel
import com.example.devicecontrols.services.NotificationListenerService
import com.example.devicecontrols.utils.requestIMEI
import com.example.devicecontrols.utils.openDefaultBrowser
import com.example.devicecontrols.utils.requestAddress

class MainActivity : AppCompatActivity(), MessageListenerInterface, NotificationListenerInterface {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: OverviewViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        getIpAddress()
        getIMEI()

        MessageReceiver.bindListener(this)
        requestNotificationListener()
        NotificationListenerService.bindListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun getIpAddress() {
        val ip = requestAddress(true)
        binding.idTextViewIP.text = "IPV4: $ip";
    }

    private fun getIMEI() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = requestIMEI(this, this, telephonyManager)
        Log.d("emei", imei)
    }

    private fun requestNotificationListener() {
        if (!NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(this.packageName)
        ) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_browser_default -> {
                openDefaultBrowser(this, "default browser")
                return true
            }

            R.id.action_chrome -> {
                openDefaultBrowser(this, "chrome")
                return true
            }

            R.id.action_facebook -> {
                openDefaultBrowser(this, "https://www.facebook.com")
                return true
            }

            R.id.action_tiktok -> {
                openDefaultBrowser(this, "https://www.tiktok.com")
                return true
            }

            R.id.action_youtube -> {
                openDefaultBrowser(this, "https://www.youtube.com/")
                return true
            }

            R.id.action_stack -> {
                openDefaultBrowser(this, "https://stackoverflow.com/")
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun messageReceived(message: String?) {
        binding.idTVMessage.text = message
    }

    override fun notificationReceived(message: String?) {
        binding.idTVNotification.text = message
        message?.let { viewModel.postLogsAccountBalance(it) }
        binding.idTVResponse.text = viewModel.message.value
    }
}