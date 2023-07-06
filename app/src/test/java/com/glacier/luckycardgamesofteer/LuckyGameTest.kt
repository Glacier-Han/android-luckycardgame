package com.glacier.luckycardgamesofteer

import com.glacier.luckycardgamesofteer.model.Card
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class LuckyGameTest {

    lateinit var luckyGame: LuckyGame

    @Before
    fun setUp() {
        luckyGame = LuckyGame()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initCard() {
        luckyGame.initCard()
        assertEquals(36, luckyGame.cardList.size)
    }

    @Test
    fun shareCard() {
        luckyGame.initCard()
        luckyGame.shareCard(3)
        // 3명이 게임에 참가할 경우 4가 정답이여야 한다. 왜냐하면 '남은 카드리스트'도 participantList에 들어가기 때문
        assertEquals(4, luckyGame.participantList.size)

        luckyGame.initCard()
        luckyGame.shareCard(5)
        assertEquals(6, luckyGame.participantList.size)
    }

    @Test
    fun sortCardAscend() {
        luckyGame.initCard()
        luckyGame.shareCard(3)

        // 정렬하기 전 A 카드리스트 저장
        val originalACards = mutableListOf<Card>()
        originalACards.addAll(luckyGame.participantList[0].getCards())

        // A 카드리스트 오름차순으로 정렬
        luckyGame.sortCardAscend(0)

        // 원본과 정렬 후 비교해서 같으면 통과
        assertEquals(originalACards, luckyGame.participantList[0].getCards())
    }

    @Test
    fun sortRemainAscend() {
        luckyGame.initCard()
        luckyGame.shareCard(3)

        // 정렬하기 전 A 카드리스트 저장
        val originalACards = mutableListOf<Card>()
        originalACards.addAll(luckyGame.participantList[luckyGame.participantList.lastIndex].getCards())

        // A 카드리스트 오름차순으로 정렬
        luckyGame.sortRemainAscend()

        // 원본과 정렬 후 비교해서 같으면 통과
        assertEquals(originalACards, luckyGame.participantList[luckyGame.participantList.lastIndex].getCards())
    }

    @Test
    fun isSameCardInParticipants() {

    }

    @Test
    fun isSameCardInSpecificCase() {
    }
}