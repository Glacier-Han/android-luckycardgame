package com.glacier.luckycardgamesofteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pickCard(initCard())
    }

    fun initCard(): MutableList<Card> {
        var cardList = mutableListOf<Card>()

        for (i in 1..12) {
            val pickedCard = Card(AnimalInfo.values().random(), i)
            cardList.add(pickedCard)
        }

        return cardList
    }

    fun pickCard(cardList: MutableList<Card>) {
        var results = ""

        cardList.shuffle()

        for (card in cardList) {
            results += String.format("%s%02d, ", card.animalType.unicode, card.num)
        }

        // 마지막 쉼표와 공백은 제외하고 콘솔창에 출력하기 위해 dropLast(2)를 사용
        Log.d("ShowCard", results.dropLast(2))
    }
}