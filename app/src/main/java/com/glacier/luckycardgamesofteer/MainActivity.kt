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
        // 12개의 전체 카드 객체를 저장하기 위한 MutableList 선언
        var cardList = mutableListOf<Card>()

        // AnimalInfo enum중 랜덤하게 뽑은 카드를 총 12개 생성 후 리스트에 담는다. 숫자는 1~12로 지정
        for (i in 1..12) {
            val pickedCard = Card(AnimalInfo.values().random(), i)
            cardList.add(pickedCard)
        }

        // 생성한 카드 리스트를 리턴한다.
        return cardList
    }

    fun pickCard(cardList: MutableList<Card>) {
        // 콘솔에 결과를 표시하기 위한 String 변수
        var results = ""

        // 뽑기 전에 카드들을 한번 섞는다
        cardList.shuffle()

        // 카드를 12개 다 뽑아서 콘솔에 정해진 형식대로 출력한다.
        for (card in cardList) {
            results += String.format("%s%02d, ", card.animalType.unicode, card.num)
        }

        // 마지막 쉼표와 공백은 제외하고 콘솔창에 출력한다.
        Log.d("ShowCard", results.dropLast(2))
    }
}