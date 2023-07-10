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


class CardAdapter(luckyGame: LuckyGame, participantNum: Int, listener: OnCardFilpedListener) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private val cards = luckyGame.participantList[participantNum].getCards()
    private val participantNum = participantNum
    private val luckyGame = luckyGame
    private val listener = listener

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

            if (card.isBack) {
                setCardBack(binding)
            } else {
                setCardFront(binding, card)
            }

            binding.itemView.setOnClickListener {
                if (card.isBack) {
                    if (luckyGame.isCardCanFilp(participantNum, adapterPosition)) {
                        card.isBack = false
                        luckyGame.filpCard(participantNum, adapterPosition)
                        listener.onCardFilped(card, participantNum, adapterPosition)
                        setCardFront(binding, card)
                    }
                }
            }


        }
    }

    fun setCardBack(binding: ItemCardBinding) {
        binding.ivBack.visibility = View.VISIBLE
        binding.tvAnimal.visibility = View.GONE
        binding.tvNumBottom.visibility = View.GONE
        binding.tvNumTop.visibility = View.GONE
    }

    fun setCardFront(binding: ItemCardBinding, card: Card) {
        val rotationY = ObjectAnimator.ofFloat(binding.itemView, "rotationY", -180f, 0f)
        rotationY.duration = 500 // Set the duration of the animation (in milliseconds)
        rotationY.start() // Start the animation

        Handler(Looper.getMainLooper()).postDelayed({
            binding.ivBack.visibility = View.GONE
            binding.tvAnimal.visibility = View.VISIBLE
            binding.tvNumBottom.visibility = View.VISIBLE
            binding.tvNumTop.visibility = View.VISIBLE

            binding.tvAnimal.text = card.animalType.unicode
            binding.tvNumBottom.text = card.num.toString()
            binding.tvNumTop.text = card.num.toString()
        }, 250)

    }

}
