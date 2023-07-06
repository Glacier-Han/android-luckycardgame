package com.glacier.luckycardgamesofteer.model

import android.util.Log

class Participant(private val name: String) {
    private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun addAllCard(cardList: List<Card>){
        cards.addAll(cardList)
    }

    fun removeCard(card: Card) {
        cards.remove(card)
    }

    fun getCards(): List<Card> {
        return cards
    }

    fun getCardCount(): Int{
        return cards.size
    }

    fun showCards(){
        for(c in cards){
            Log.d("Participant $name", c.toString())
        }
    }
}