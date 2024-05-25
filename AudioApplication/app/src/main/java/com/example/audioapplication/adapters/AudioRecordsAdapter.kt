import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.audioapplication.adapters.AudioViewHolder
import com.example.audioapplication.data.AudioRecords
import com.example.audioapplication.databinding.AudioRecordItemBinding

class AudioRecordsAdapter(private val recordsList: List<AudioRecords>) : RecyclerView.Adapter<AudioViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = AudioRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioViewHolder(binding) { audioPath -> playRecord(audioPath) }
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(recordsList[position])
    }

    private fun playRecord(audioPath: String) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(audioPath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            currentPlayingPosition = null
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mediaPlayer?.release()
    }
}