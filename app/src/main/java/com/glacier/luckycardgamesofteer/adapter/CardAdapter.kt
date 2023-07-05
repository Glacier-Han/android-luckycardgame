package com.glacier.luckycardgamesofteer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.databinding.ItemCardBinding

class CardAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class ViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card) {
            binding.tvAnimal.text = card.animalType.unicode
            binding.tvNumBottom.text = card.num.toString()
            binding.tvNumTop.text = card.num.toString()
        }
    }
}
