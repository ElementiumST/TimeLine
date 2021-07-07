package com.example.timeline

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timeline.databinding.TimeLineViewBinding

class TimeLineView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    private val binding: TimeLineViewBinding = TimeLineViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val timelineScrollListenerPool = mutableListOf<TimeLineOnScrollListener>()

    init {
        binding.minutesLine.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        binding.minutesLine.adapter = TimeLineMinutesAdapter(TimeLineDataProvider.getArchive())
        binding.minutesLine.addOnScrollListener(TimeLineOnScrollListener())

        binding.hourLine.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        binding.hourLine.adapter = TimeLineHourAdapter(TimeLineDataProvider.getArchive())
        binding.hourLine.addOnScrollListener(TimeLineOnScrollListener())
    }
    inner class TimeLineOnScrollListener(): RecyclerView.OnScrollListener() {
        init {
            timelineScrollListenerPool.add(this)
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            for (line in timelineScrollListenerPool) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }
    }

}