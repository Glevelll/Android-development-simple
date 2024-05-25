package com.example.audioapplication.tab2

import com.example.audioapplication.data.AudioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Tab2PresenterImpl(private val view: Tab2View, private val audioRepository: AudioRepository) : Tab2Presenter {

    override fun onViewCreated() {
        view.setupUI()
    }

    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            val newRecordsList = audioRepository.getAllRecords()
            withContext(Dispatchers.Main) {
                view.updateRecordsList(newRecordsList)
            }
        }
    }
}