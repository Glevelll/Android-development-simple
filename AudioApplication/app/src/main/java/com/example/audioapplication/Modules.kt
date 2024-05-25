package com.example.audioapplication

import androidx.room.Room
import com.example.audioapplication.data.AudioRepository
import com.example.audioapplication.data.Records
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(androidContext(), Records::class.java, "audio_records").build() }
    single { get<Records>().audioRecordDao() }
    single { AudioRepository(get()) }
}