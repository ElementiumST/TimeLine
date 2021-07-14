package com.example.timeline

class TimeLineHourAdapter(archive: Archive) : TimeLineAdapter(archive) {
    override fun getMillisecondsInOnePixel(): Int {
        return 1200.toPx.toInt()
    }
}