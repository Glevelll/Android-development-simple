package com.example.audioapplication.tab1

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.media.MediaRecorder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Chronometer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.audioapplication.data.AudioRecords
import com.example.audioapplication.data.AudioRepository
import com.example.audioapplication.databinding.FragmentTab1Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException
import java.util.Date

class Tab1Fragment : Fragment(), Tab1View {

    private val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123
    private lateinit var binding: FragmentTab1Binding
    private var isExpanded: Boolean = false
    private lateinit var mediaRecorder: MediaRecorder
    private var isRecording: Boolean = false
    private lateinit var chronometer: Chronometer
    private var outputFile: File? = null
    private var audioFilePath: String? = null
    private val audioRepository by inject<AudioRepository>()
    private val presenter: Tab1Presenter by lazy { Tab1PresenterImpl(this, audioRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTab1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun setupUI() = with(binding) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_REQUEST_CODE)
        }
        chronometer = time

        btnRec.setOnClickListener {
            presenter.pressOnButton(chronometer, isRecording)
        }
    }

    override fun toggleButtonAnimation() = with(binding) {
        val startRadius = if (isExpanded) 20f else 100f
        val endRadius = if (isExpanded) 100f else 20f

        val animator = ValueAnimator.ofFloat(startRadius, endRadius)
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            val shape = btnRec.background as GradientDrawable
            shape.cornerRadius = value
        }
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 100
        animator.start()

        isExpanded = !isExpanded
    }

    override fun startRecording() {
        CoroutineScope(Dispatchers.IO).launch {
            val recordingsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            audioFilePath = "${recordingsDir.absolutePath}/recording_${System.currentTimeMillis()}.wav"
            outputFile = File(audioFilePath!!)

            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder.setOutputFile(outputFile?.absolutePath)

            try {
                mediaRecorder.prepare()
                mediaRecorder.start()
                isRecording = true
            } catch (e: IOException) {
                Log.e("Recording", "MediaRecorder start() failed. ${e.message}")
            }
        }
    }

    override fun stopRecording() {
        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()
        isRecording = false

        val currentDate = Date()
        val recordTime = SystemClock.elapsedRealtime() - chronometer.base

        val audioRecord = AudioRecords(0, currentDate, recordTime, audioFilePath ?: "")

        CoroutineScope(Dispatchers.IO).launch {
            audioRepository.insertRecord(audioRecord)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Запись успешно сохранена", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showRecordingSavedMessage() {
        Toast.makeText(context, "Запись успешно сохранена", Toast.LENGTH_SHORT).show()
    }
}