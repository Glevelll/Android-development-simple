package com.project.alarmkotlin

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AlarmActivity: AppCompatActivity() {
    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        setRingtone()
    }

    private fun setRingtone() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notification)
        if (ringtone == null) {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, notification)
        }
        playRingtone()
    }

    private fun playRingtone() {
        if (ringtone?.isPlaying == false) {
            ringtone?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
    }

    private fun stopRingtone() {
        if (ringtone?.isPlaying == true) {
            ringtone?.stop()
        }
    }
}