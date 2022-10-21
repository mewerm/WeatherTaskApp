package com.maximmesh.weathertaskapp.adapters.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maximmesh.weathertaskapp.R
import com.maximmesh.weathertaskapp.data.WeatherModel
import com.maximmesh.weathertaskapp.databinding.CardItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter : ListAdapter<WeatherModel, WeatherAdapter.Holder>(Comparator()){

    class Holder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = CardItemBinding.bind(view)

        fun bind(item: WeatherModel) = with(binding){
            data.text = item.time
            currentTemp.text = item.currentTemp
            Picasso.get().load("https:" + item.imageUrl).into(picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Comparator : DiffUtil.ItemCallback<WeatherModel>(){ //класс компоратор помогвет сравнивать элементы списка
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }
    }
}
