package com.example.trucksmap.activity.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksmap.activity.model.Data
import com.example.trucksmap.databinding.ItemTruckViewBinding
import java.lang.Math.abs
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AdapterTruckslist(
    private var getList: ArrayList<Data>,
) : RecyclerView.Adapter<AdapterTruckslist.Holder>() {

    class Holder(val binding: ItemTruckViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemTruckViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = getList[position]
        holder.binding.model = model
        val date1 = Date(model.lastRunningState.stopStartTime)
        val date2 = Date()
        val diff = date2.time - date1.time
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        val hours: Long = days / 3600
        val minutes: Long = days % 3600 / 60
        val seconds: Long = days % 3600 % 60

        val speed = String.format("%.0f", model.lastWaypoint.speed)
        holder.binding.tructkmNo.text = speed
        holder.binding.tructRunningStatus.text = "$days days ago"
        if (model.lastRunningState.truckRunningState == 1){
            holder.binding.tructkmNo.visibility = View.VISIBLE
            holder.binding.truckkm.visibility = View.VISIBLE
            holder.binding.tructStatus.text = "Running since last $days days ago"
        }else{
            holder.binding.tructStatus.text = "Stopped since last $days days ago"
            holder.binding.tructkmNo.visibility = View.GONE
            holder.binding.truckkm.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = getList.size

    fun addMoreLikeList(newLikeList: ArrayList<Data>) {
        val oldLikeListSize = getList.size
        if (getList != newLikeList) {
            getList.addAll(newLikeList)
            notifyItemRangeInserted(oldLikeListSize, newLikeList.size)
        }
    }

    fun initList(initialLikeList: ArrayList<Data>) {
        getList = initialLikeList
        notifyDataSetChanged()
    }
}