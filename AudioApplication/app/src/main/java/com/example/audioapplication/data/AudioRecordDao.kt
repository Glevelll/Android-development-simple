package com.example.audioapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audio_records")
    fun getAllRecords(): List<AudioRecords>

    @Insert
    fun insertRecord(audioRecord: AudioRecords)
}