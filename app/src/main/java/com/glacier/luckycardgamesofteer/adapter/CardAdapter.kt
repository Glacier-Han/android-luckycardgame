package com.glacier.luckycardgamesofteer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.databinding.ItemCardBinding

class CardAdapter(private val cards: List<Card>, private val isFront: Boolean) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

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

            /*
            // 카드 잘 나오는지 테스트용
            binding.tvAnimal.text = card.animalType.unicode
            binding.tvNumBottom.text = card.num.toString()
            binding.tvNumTop.text = card.num.toString()
             */

            if(isFront){
                binding.ivBack.visibility = View.GONE
                binding.tvAnimal.text = card.animalType.unicode
                binding.tvNumBottom.text = card.num.toString()
                binding.tvNumTop.text = card.num.toString()
            } else{
                binding.ivBack.visibility = View.VISIBLE
                binding.tvAnimal.visibility = View.GONE
                binding.tvNumBottom.visibility = View.GONE
                binding.tvNumTop.visibility = View.GONE
            }


        }
    }
}
