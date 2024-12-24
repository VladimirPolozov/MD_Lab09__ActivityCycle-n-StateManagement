package com.example.md_lab09__activitycycle_n_statemanagement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(diffCallback: DiffCallback) : ListAdapter<WeatherList, RecyclerView.ViewHolder>(diffCallback) {

    private val VIEW_TYPE_HOT = 1
    private val VIEW_TYPE_COLD = 2

    override fun getItemViewType(position: Int): Int {
        val tempCel = getItem(position).main.temp - 273.15
        return if (tempCel >= 0) VIEW_TYPE_HOT else VIEW_TYPE_COLD
    }
    inner class ViewHolderHot(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("DefaultLocale")
        fun bind(weatherList: WeatherList) {
            itemView.findViewById<TextView>(R.id.datetime).text = weatherList.dt_txt
            val tempCel = weatherList.main.temp - 273.15
            itemView.findViewById<TextView>(R.id.temp).text = String.format("%.2f °C", tempCel)
            val iconUrl = "https://openweathermap.org/img/wn/${weatherList.weather[0].icon}@2x.png"
            Glide.with(itemView.context)
                .load(iconUrl)
                .into(itemView.findViewById(R.id.temp_icon))
        }
    }

    inner class ViewHolderCold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("DefaultLocale")
        fun bind(weatherList: WeatherList) {
            itemView.findViewById<TextView>(R.id.datetime).text = weatherList.dt_txt
            val tempCel = weatherList.main.temp - 273.15
            itemView.findViewById<TextView>(R.id.temp).text = String.format("%.2f °C", tempCel)
            val iconUrl = "https://openweathermap.org/img/wn/${weatherList.weather[0].icon}@2x.png"
            Glide.with(itemView.context)
                .load(iconUrl)
                .into(itemView.findViewById(R.id.temp_icon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HOT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item_hot, parent, false)
                ViewHolderHot(view)
            }
            VIEW_TYPE_COLD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item_cold, parent, false)
                ViewHolderCold(view)
            }
            else -> throw IllegalArgumentException("Неизвестный тип представления $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weatherList = getItem(position)
        when (holder) {
            is ViewHolderHot -> holder.bind(weatherList)
            is ViewHolderCold -> holder.bind(weatherList)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<WeatherList>() {
    override fun areItemsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
        return oldItem == newItem
    }
}