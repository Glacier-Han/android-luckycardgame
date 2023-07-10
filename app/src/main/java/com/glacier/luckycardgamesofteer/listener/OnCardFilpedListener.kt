package com.glacier.luckycardgamesofteer.listener

import com.glacier.luckycardgamesofteer.model.Card

interface OnCardFilpedListener {
    fun onCardFilped(card: Card, participantNum: Int, cardIndex: Int)
}