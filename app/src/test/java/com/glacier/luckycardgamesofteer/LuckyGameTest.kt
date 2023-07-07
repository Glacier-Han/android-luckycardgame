package com.glacier.luckycardgamesofteer

import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class LuckyGameTest {

    // 테스트 케이스에도 네이밍 컨벤션이 많은데, 그 중 제일 직관적인 test + 테스트할 기능 설명 으로 결정.
    lateinit var luckyGame: LuckyGame

    @Before
    fun setUp() {
        luckyGame = LuckyGame()
        luckyGame.initCard()
    }

    @After
    fun tearDown() {
        luckyGame.cardList.clear()
        luckyGame.participantList.clear()
    }

    @Test
    fun testInitCardCount() {
        // TC1. 카드 생성후 갯수가 36개가 맞는지?
        luckyGame.initCard()
        assertEquals(36, luckyGame.cardList.size)
    }

    @Test
    fun testInitCardTypeVaild() {
        // TC2. 카드 36개중에 같은 동물이 12개씩 있는지? (동물이 3종류임으로 한동물당 12개씩)
        val animalTypeCounts = luckyGame.cardList.groupBy { it.animalType }
        val hasThreeOfEach = animalTypeCounts.values.any { it.size == 12 }
        assertEquals(true, hasThreeOfEach)
    }

    @Test
    fun testShareCardCountWhen3() {
        // TC3. 참가자 인원에 맞게 생성되는지?
        // 3명이 게임에 참가할 경우 4가 정답이여야 한다. 왜냐하면 '남은 카드리스트'도 participantList에 들어가기 때문
        luckyGame.shareCard(3)
        assertEquals(4, luckyGame.participantList.size)
    }
    @Test
    fun testShareCard3PeopleVaild(){
        // TC4. 3명이 참가했을 시 participant가 12번 카드를 가지고 있으면 안됨. 가지고 있는지 검사
        var isSameContain = false
        for (participant in luckyGame.participantList) {
            for (card in participant.getCards()) {
                if (card.num == 12) isSameContain = true
            }
        }
        assertEquals(false, isSameContain)
    }

    @Test
    fun testShareCardCountWhen5() {
        // TC5. 5명이 참가했을 시 participantList.size가 6이 맞는지?
        luckyGame.initCard()
        luckyGame.shareCard(5)
        assertEquals(6, luckyGame.participantList.size)
    }


    @Test
    fun testSortCardAscend() {
        // TC6. A참가자를 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
        // 정렬하지 않은 리스트와, 정렬 된 리스트가 같은지 비교
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
    fun testSortRemainAscend() {
        // TC7. 남은 카드 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
        luckyGame.shareCard(3)

        // 정렬하기 전 A 카드리스트 저장
        val originalACards = mutableListOf<Card>()
        originalACards.addAll(luckyGame.participantList[luckyGame.participantList.lastIndex].getCards())

        // A 카드리스트 오름차순으로 정렬
        luckyGame.sortRemainAscend()

        // 원본과 정렬 후 비교해서 같으면 통과
        assertEquals(
            originalACards,
            luckyGame.participantList[luckyGame.participantList.lastIndex].getCards()
        )
    }

    @Test
    fun testIsSameCardInParticipants() {
        // TC8. 참가자 중 같은 숫자 카드 3개를 가진 경우가 있는지 판단
        // test 상황을 만들기 위해서 임의로 같은 카드 3개를 1번 참가자에게 지급
        luckyGame.shareCard(5)
        val targetCardSet = luckyGame.participantList[0].getCards()

        val sameCardSet = mutableListOf<Card>()
        for (i in 0..2) {
            sameCardSet.add(targetCardSet.minBy { it.num })
        }
        for (i in 0..2) {
            sameCardSet.add(targetCardSet.maxBy { it.num })
        }
        luckyGame.participantList[luckyGame.participantList.lastIndex].setCards(sameCardSet)

        // 1번참가자가 같은카드 3개씩을 들고있기 때문에 무조건 true가 나와야함
        val methodAnswer = luckyGame.isSameCardInParticipants()
        assertEquals(true, methodAnswer)
    }

    // 특정 참가자와 해당 참가자 카드 중에 가장 낮은 숫자 또는 가장 높은 숫자, 바닥 카드 중 아무거나를 선택해서 3개가 같은지 판단할 수 있어야 한다.
    @Test
    fun testIsSameCardInSpecificCase() {
        // TC9. 1번 참가자와 3번 참가자 카드 중에 가장 낮은숫자 기준으로 3개가 같은지 판단
        // test 상황을 만들기 위해서 임의로 같은 카드Set을 2명의 참가자 에게 넣음
        // 남은 카드에는 위 카드셋의 min값으로 채움 (비교를 수월하게 하기 위해)
        luckyGame.shareCard(5)
        val targetCardSet = luckyGame.participantList[0].getCards()

        luckyGame.participantList[0].setCards(targetCardSet as MutableList<Card>)
        luckyGame.participantList[2].setCards(targetCardSet)

        val remainCardSet = mutableListOf<Card>()
        for (i in 0..5) {
            remainCardSet.add(targetCardSet.minBy { it.num })
        }
        luckyGame.participantList[luckyGame.participantList.lastIndex].setCards(remainCardSet)

        // 1번참가자, 3번참가자 모두 같은 데이터셋을 들고있다. 남은 카드는 어차피 해당 두개 데이터셋의 최소값으로 채워져 있기 때문에 무조건 True가 나와야함
        val methodAnswer = luckyGame.isSameCardInSpecificCase(0, 2, true)
        assertEquals(true, methodAnswer)
    }
}