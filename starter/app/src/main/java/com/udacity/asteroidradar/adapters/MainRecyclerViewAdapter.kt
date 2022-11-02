package com.udacity.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.model.Asteroid

class MainRecyclerViewAdapter(
    val asteroids: List<Asteroid>,
    val clickListener: AsteroidListener
) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.ItemAsteroidViewHolder>() {
    class ItemAsteroidViewHolder(val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAsteroidViewHolder {
        return ItemAsteroidViewHolder(
            ItemAsteroidBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemAsteroidViewHolder, position: Int) {
        holder.binding.asteroid = asteroids.get(position)
        holder.binding.asteroidClick=clickListener
    }

    override fun getItemCount(): Int {
        return asteroids.size
    }

    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
