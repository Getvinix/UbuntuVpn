package com.myvpn.app

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isConnected = false
    private lateinit var statusText: TextView
    private lateinit var connectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusText = findViewById(R.id.statusText)
        connectButton = findViewById(R.id.connectButton)
        connectButton.setOnClickListener {
            if (!isConnected) {
                val intent = VpnService.prepare(this)
                if (intent != null) {
                    startActivityForResult(intent, 100)
                } else {
                    startVpn()
                }
            } else {
                stopVpn()
            }
        }
    }

    private fun startVpn() {
        val intent = Intent(this, MyVpnService::class.java)
        intent.action = "START"
        startService(intent)
        isConnected = true
        statusText.text = "Connected - Ubuntu VPN"
        connectButton.text = "Disconnect"
    }

    private fun stopVpn() {
        val intent = Intent(this, MyVpnService::class.java)
        intent.action = "STOP"
        startService(intent)
        isConnected = false
        statusText.text = "Disconnected"
        connectButton.text = "Connect"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            startVpn()
        }
    }
}
