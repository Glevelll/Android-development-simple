package com.example.audioapplication.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.audioapplication.R
import com.example.audioapplication.data.AudioRecords
import com.example.audioapplication.databinding.AudioRecordItemBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioViewHolder(private val binding: AudioRecordItemBinding, private val playRecord: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    private var currentPlayingPosition: Int? = null
    fun bind(record: AudioRecords) {
        with(binding) {
            val recordingIndex = record.audioPath.indexOf("recording")
            val displayedText = if (recordingIndex != -1) {
                record.audioPath.substring(recordingIndex)
            } else {
                record.audioPath
            }
            text1.text = displayedText
            val totalMilliseconds = record.recordTime
            val totalSeconds = totalMilliseconds / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            val timeString = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
            text2.text = timeString
            text3.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(record.recordDate)

            if (currentPlayingPosition == adapterPosition) {
                rec.setImageResource(R.drawable.playing)
            } else {
                rec.setImageResource(R.drawable.play)
            }

            rec.setOnClickListener {
                if (currentPlayingPosition != adapterPosition) {
                    currentPlayingPosition = adapterPosition
                    playRecord(record.audioPath)
                }
            }
        }
    }
}