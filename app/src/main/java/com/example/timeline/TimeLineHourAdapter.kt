package com.example.timeline

class TimeLineHourAdapter(archive: Archive) : TimeLineAdapter(archive) {
    override fun getMillisecondsInOnePixel(): Int {
        return 480.toPx.toInt()
    }
}