package com.example.audioapplication.tab2

import com.example.audioapplication.data.AudioRecords


interface Tab2View {
    fun setupUI()
    fun updateRecordsList(newRecordsList: List<AudioRecords>)
}