package com.example.audioapplication.tab2

import AudioRecordsAdapter
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioapplication.data.AudioRecords
import com.example.audioapplication.data.AudioRepository
import com.example.audioapplication.databinding.FragmentTab2Binding
import org.koin.android.ext.android.inject

class Tab2Fragment : Fragment(), Tab2View {

    private val audioRepository by inject<AudioRepository>()
    private lateinit var recordsList: MutableList<AudioRecords>
    private lateinit var adapter: AudioRecordsAdapter
    private lateinit var binding: FragmentTab2Binding
    private val presenter: Tab2Presenter by lazy { Tab2PresenterImpl(this, audioRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTab2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun setupUI() = with(binding) {
        recycler.layoutManager = LinearLayoutManager(requireContext())

        recordsList = mutableListOf()
        adapter = AudioRecordsAdapter(recordsList)
        recycler.adapter = adapter
    }

    override fun updateRecordsList(newRecordsList: List<AudioRecords>) {
        recordsList.clear()
        recordsList.addAll(newRecordsList)
        adapter.notifyDataSetChanged()
    }
}