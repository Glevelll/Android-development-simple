package com.example.audioapplication.tab1

import android.widget.Chronometer

interface Tab1Presenter {
    fun startChronometer(chronometer: Chronometer)
    fun stopChronometer(chronometer: Chronometer)
    fun pressOnButton(chronometer: Chronometer, isRecording: Boolean)
}