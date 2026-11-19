package com.myvpn.app

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var sshSession: Session? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val host = intent?.getStringExtra("host") ?: ""
        val user = intent?.getStringExtra("user") ?: ""
        val pass = intent?.getStringExtra("pass") ?: ""

        Thread {
            try {
                val builder = Builder()
                builder.setSession("MyVPN")
                builder.addAddress("10.0.0.2", 32)
                builder.addRoute("0.0.0.0", 0)
                builder.addDnsServer("8.8.8.8")
                vpnInterface = builder.establish()

                val jsch = JSch()
                sshSession = jsch.getSession(user, host, 22)
                sshSession?.setPassword(pass)
                sshSession?.setConfig("StrictHostKeyChecking", "no")
                sshSession?.connect(10000)
                sshSession?.setPortForwardingD(1080, "127.0.0.1", 1080)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        try {
            sshSession?.disconnect()
            vpnInterface?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}
