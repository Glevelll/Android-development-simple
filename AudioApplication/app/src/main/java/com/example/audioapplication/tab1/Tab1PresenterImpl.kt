package com.example.audioapplication.tab1

import android.os.SystemClock
import android.widget.Chronometer
import com.example.audioapplication.data.AudioRepository

class Tab1PresenterImpl(private val view: Tab1View, private val audioRepository: AudioRepository) :
    Tab1Presenter {
    override fun pressOnButton(chronometer: Chronometer, isRecording: Boolean) {
        if (!isRecording) {
            view.startRecording()
            startChronometer(chronometer)
        } else {
            view.stopRecording()
            stopChronometer(chronometer)
        }
        view.toggleButtonAnimation()
    }


    override fun startChronometer(chronometer: Chronometer) {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }

    override fun stopChronometer(chronometer: Chronometer) {
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
    }
}