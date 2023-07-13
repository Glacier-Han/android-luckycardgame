package com.glacier.luckycardgamesofteer.adapter

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.LuckyGame
import com.glacier.luckycardgamesofteer.databinding.ItemCardBinding
import com.glacier.luckycardgamesofteer.listener.OnCardFilpedListener
import com.glacier.luckycardgamesofteer.model.Card
import java.util.ArrayList
import kotlin.random.Random


class ResultCardAdapter(private val cards: List<Card>) :
    RecyclerView.Adapter<ResultCardAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card) {
            binding.ivBack.visibility = View.GONE
            binding.tvAnimal.visibility = View.VISIBLE
            binding.tvNumBottom.visibility = View.VISIBLE
            binding.tvNumTop.visibility = View.VISIBLE

            binding.tvAnimal.text = card.animalType.unicode
            binding.tvNumBottom.text = card.num.toString()
            binding.tvNumTop.text = card.num.toString()
        }

    }

}
