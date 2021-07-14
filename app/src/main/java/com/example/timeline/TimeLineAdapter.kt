package com.example.timeline

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.timeline.databinding.ItemLiveViewHolderBinding
import com.example.timeline.databinding.ItemSegmentBinding

abstract class TimeLineAdapter(
    private val archive: Archive
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_SEGMENT = 0
        const val VIEW_TYPE_DIVIDER = 1
        const val VIEW_TYPE_LIVE = 2
        const val VIEW_TYPE_START_FREE_SPACE = 3
        const val VIEW_TYPE_END_FREE_SPACE = 4
        const val DIVIDER_MIN_TIME_IN_MILLISECOND = 10000

        const val ONE_PREVIEW_WIDTH = 200
    }
    lateinit var holderTypeList: List<Int>

    init {
        updateViewHolderList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_SEGMENT -> onCreateViewSegment(layoutInflater, parent)
            VIEW_TYPE_LIVE -> LiveViewHolder(ItemLiveViewHolderBinding.inflate(layoutInflater))
            VIEW_TYPE_START_FREE_SPACE -> {
                val context = parent.context
                val freeSpaceView = FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(getFreeSpaceWidth(context, false), LinearLayout.LayoutParams.MATCH_PARENT)
                }
                FreeSpaceViewHolder(freeSpaceView)
            }
            VIEW_TYPE_END_FREE_SPACE -> {
                val context = parent.context
                val freeSpaceView = FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(getFreeSpaceWidth(context, true), LinearLayout.LayoutParams.MATCH_PARENT)
                }
                FreeSpaceViewHolder(freeSpaceView)
            }
            else -> DividerViewHolder(layoutInflater.inflate(R.layout.item_divider, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == VIEW_TYPE_SEGMENT) onBindSegment(holder as TimeLineSegmentViewHolder, position/2)
    }

    override fun getItemCount(): Int {
        return holderTypeList.size
    }

    override fun getItemViewType(position: Int): Int {
        return holderTypeList[position]
    }

    abstract fun getMillisecondsInOnePixel(): Int;

    private fun updateViewHolderList() {
        val holderList = mutableListOf<Int>()
        holderList.add(VIEW_TYPE_START_FREE_SPACE)
        for (segmentIndex in archive.segments.indices) {
            if(segmentIndex > 0 && archive.segments[segmentIndex].timeStart - archive.segments[segmentIndex-1].timeEnd > DIVIDER_MIN_TIME_IN_MILLISECOND) {
                holderList.add(VIEW_TYPE_DIVIDER)
            }
            holderList.add(VIEW_TYPE_SEGMENT)
        }
        holderList.add(VIEW_TYPE_LIVE)
        holderList.add(VIEW_TYPE_END_FREE_SPACE)
        holderTypeList = holderList
    }

    private fun onCreateViewSegment(layoutInflater: LayoutInflater, parent: ViewGroup): TimeLineSegmentViewHolder {
        val binding = ItemSegmentBinding.inflate(layoutInflater)
        return TimeLineSegmentViewHolder(binding)
    }

    private fun onBindSegment(holder: TimeLineSegmentViewHolder, position: Int) {
        holder.bind(archive.segments[position])
    }

    private fun getSegmentsCount(): Int {
        return archive.segments.size
    }

    private fun getFreeSpaceWidth(context: Context, withLifeOffset: Boolean): Int {
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        val offset = if (withLifeOffset) context.resources.getDimensionPixelSize(R.dimen.timeLineHeight) else 0
        return displayWidth/2 - offset/2
    }
    inner class FreeSpaceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
    inner class LiveViewHolder(binding: ItemLiveViewHolderBinding): RecyclerView.ViewHolder(binding.root) {

    }
    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    inner class TimeLineSegmentViewHolder(
        private val binding: ItemSegmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(archiveSegment: ArchiveSegment) {
            val segmentPixelSize = ((archiveSegment.timeEnd - archiveSegment.timeStart) / getMillisecondsInOnePixel()).toInt()
            binding.container.layoutParams = RecyclerView.LayoutParams(segmentPixelSize, RecyclerView.LayoutParams.MATCH_PARENT)
            val numOfSegments = segmentPixelSize / ONE_PREVIEW_WIDTH
            for (i in 0..numOfSegments) {
                val posterView = ImageView(binding.container.context).apply {
                    layoutParams = LinearLayout.LayoutParams(ONE_PREVIEW_WIDTH, LinearLayout.LayoutParams.MATCH_PARENT)
                    Glide.with(binding.container)
                        .load(archiveSegment.previews)
                        .placeholder(R.drawable.timeline_placeholder)
                        .into(this)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                binding.container.addView(posterView)
            }
        }
    }
}