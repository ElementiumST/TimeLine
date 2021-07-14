package com.example.timeline

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timeline.TimeLineView.TimeLineOnScrollListener.timelineScrollListenerPool
import com.example.timeline.databinding.TimeLineViewBinding
import java.util.*

class TimeLineView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    private val binding: TimeLineViewBinding = TimeLineViewBinding.inflate(LayoutInflater.from(context), this, true)
    init {
        binding.minutesLine.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        binding.minutesLine.adapter = TimeLineMinutesAdapter(TimeLineDataProvider.getArchive())
        binding.minutesLine.addOnScrollListener(TimeLineOnScrollListener)
        timelineScrollListenerPool.add(binding.minutesLine)

        binding.hourLine.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        binding.hourLine.adapter = TimeLineHourAdapter(TimeLineDataProvider.getArchive())
        binding.hourLine.addOnScrollListener(TimeLineOnScrollListener)
        timelineScrollListenerPool.add(binding.hourLine)
    }
    object TimeLineOnScrollListener: RecyclerView.OnScrollListener() {
        val calendar = Calendar.getInstance()
        val timelineScrollListenerPool = mutableListOf<RecyclerView>()
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val adapter = recyclerView.adapter as TimeLineAdapter
            val deltaTime = adapter.getMillisecondsInOnePixel() * dx
            for (line in timelineScrollListenerPool) {
                if (line == recyclerView) return
                line.removeOnScrollListener(this)
                val lineAdapter = line.adapter as TimeLineAdapter
                line.scrollBy(deltaTime / lineAdapter.getMillisecondsInOnePixel(), dy)
                line.addOnScrollListener(this)
            }
        }
    }

}