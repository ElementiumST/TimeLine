package com.example.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.timeline.databinding.ItemSegmentBinding

class TimeLineMinutesAdapter(
    private val archive: Archive
): TimeLineAdapter(archive) {

    override fun getMillisecondsInOnePixel(): Int {
        return 8.toPx.toInt()
    }
}
