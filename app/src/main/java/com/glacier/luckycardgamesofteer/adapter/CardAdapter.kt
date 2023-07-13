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
import kotlin.random.Random


class CardAdapter(
    val luckyGame: LuckyGame,
    val participantNum: Int,
    val listener: OnCardFilpedListener
) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private val cards = luckyGame.participantList[participantNum].getCards()

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

            setCardBack(binding)

            if (!card.isBack) {
                setCardFront(binding, card)
            }

            binding.itemView.setOnClickListener {
                if (card.isBack) {
                    if (luckyGame.isCardCanFilp(participantNum, adapterPosition)) {
                        card.isBack = false
                        luckyGame.filpCard(participantNum, adapterPosition)
                        listener.onCardFilped(card, participantNum, adapterPosition)
                        setCardFrontWithAnimation(binding, card)
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
        binding.ivBack.visibility = View.GONE
        binding.tvAnimal.visibility = View.VISIBLE
        binding.tvNumBottom.visibility = View.VISIBLE
        binding.tvNumTop.visibility = View.VISIBLE

        binding.tvAnimal.text = card.animalType.unicode
        binding.tvNumBottom.text = card.num.toString()
        binding.tvNumTop.text = card.num.toString()
    }

    fun setCardFrontWithAnimation(binding: ItemCardBinding, card: Card) {
        val rotationY = ObjectAnimator.ofFloat(binding.itemView, "rotationY", -180f, 0f)
        rotationY.duration = 500 // Set the duration of the animation (in milliseconds)
        rotationY.start() // Start the animation

        Handler(Looper.getMainLooper()).postDelayed({
            setCardFront(binding, card)
        }, 250)

    }
}
