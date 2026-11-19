package com.myvpn.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.connect)
        btn.setOnClickListener {
            val intent = VpnService.prepare(this)
            if (intent != null) {
                startActivityForResult(intent, 100)
            } else {
                startVpn()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startVpn()
        }
    }

    fun startVpn() {
        val intent = Intent(this, MyVpnService::class.java)
        intent.putExtra("host", findViewById<EditText>(R.id.host).text.toString())
        intent.putExtra("user", findViewById<EditText>(R.id.user).text.toString())
        intent.putExtra("pass", findViewById<EditText>(R.id.pass).text.toString())
        startService(intent)
    }
}
