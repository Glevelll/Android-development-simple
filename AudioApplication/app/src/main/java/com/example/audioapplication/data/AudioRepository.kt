package com.example.audioapplication.data

class AudioRepository (private val audioRecordDao: AudioRecordDao) {
    fun getAllRecords(): List<AudioRecords> {
        return audioRecordDao.getAllRecords()
    }

    fun insertRecord(audioRecord: AudioRecords) {
        audioRecordDao.insertRecord(audioRecord)
    }
}