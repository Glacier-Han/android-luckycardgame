package com.glacier.luckycardgamesofteer

import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class LuckyGameTest {

    // 테스트 케이스 네이밍 컨벤션 -> 함수이름_테스트내용_기대결과 로 결정
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
    fun initCard_Count_36() {
        // TC1. 카드 생성후 갯수가 36개가 맞는지?
        luckyGame.initCard()
        assertEquals(36, luckyGame.cardList.size)
    }

    @Test
    fun initCard_Has3Type_True() {
        // TC2. 카드 36개중에 같은 동물이 12개씩 있는지? (동물이 3종류임으로 한동물당 12개씩)
        val animalTypeCounts = luckyGame.cardList.groupBy { it.animalType }
        val hasThreeOfEach = animalTypeCounts.values.any { it.size == 12 }
        assertEquals(true, hasThreeOfEach)
    }

    @Test
    fun shareCard_3ParticipantsCount_4() {
        // TC3. 참가자 인원에 맞게 생성되는지?
        // 3명이 게임에 참가할 경우 4가 정답이여야 한다. 왜냐하면 '남은 카드리스트'도 participantList에 들어가기 때문
        luckyGame.shareCard(3)
        assertEquals(4, luckyGame.participantList.size)
    }

    @Test
    fun shareCard_3ParticipantsHas12_False() {
        // TC4. 3명이 참가했을 시 participant가 12번 카드를 가지고 있으면 안됨. 가지고 있는지 검사
        var isSameContain = false
        for (participant in luckyGame.participantList) {
            for (card in participant.getCards()) {
                if (card.num == 12) {
                    isSameContain = true
                }
            }
        }
        assertEquals(false, isSameContain)
    }

    @Test
    fun shareCard_5ParticipantsCount_6() {
        // TC5. 5명이 참가했을 시 participantList.size가 6이 맞는지?
        luckyGame.initCard()
        luckyGame.shareCard(5)
        assertEquals(6, luckyGame.participantList.size)
    }


    @Test
    fun sortCardAscend_IsVaild_True() {
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
    fun sortRemainAscend_IsValid_True() {
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
    fun isSameCardInParticipants_isValid_True() {
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
        luckyGame.participantList[0].setCards(sameCardSet)

        // 1번참가자가 같은카드 3개씩을 들고있기 때문에 무조건 true가 나와야함
        val methodAnswer = luckyGame.isSameCardInParticipants()
        assertEquals(true, methodAnswer)
    }

    // 특정 참가자와 해당 참가자 카드 중에 가장 낮은 숫자 또는 가장 높은 숫자, 바닥 카드 중 아무거나를 선택해서 3개가 같은지 판단할 수 있어야 한다.
    @Test
    fun isSameCardInSpecificCase_IsValid_True() {
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

    @Test
    fun isCardCanFlip_IsValid_True() {
        luckyGame.shareCard(5)
        luckyGame.sortCardAscend(0)
        luckyGame.filpCard(0, 0)

        // 1번 참가자의 왼쪽 카드를 뒤집었는 상황일 때, 왼쪽에서 두번쨰 카드를 뒤집을 수 있어야함
        assertEquals(true, luckyGame.isCardCanFilp(0, 1))
    }

    @Test
    fun isCardCanFlip_IsValid_False() {
        luckyGame.initCard()
        luckyGame.shareCard(5)
        luckyGame.sortCardAscend(0)
        luckyGame.filpCard(0, 0)

        // 1번 참가자의 왼쪽 카드를 뒤집었는 상황일 때, 왼쪽에서 세번째 카드를 뒤집을 수 없어야함
        assertEquals(false, luckyGame.isCardCanFilp(0, 2))
    }

    @Test
    fun checkWhen3CardSameStatus_IsValid_True() {
        // test 상황을 만들기 위해서 임의로 같은 카드 3개를 1번 참가자에게 지급
        luckyGame.shareCard(3)
        val targetCardSet = luckyGame.participantList[0].getCards()

        val sameCardSet = mutableListOf<Card>()
        for (i in 0..2) {
            sameCardSet.add(targetCardSet.minBy { it.num })
        }
        for (i in 0..2) {
            sameCardSet.add(targetCardSet.maxBy { it.num })
        }
        for (i in 0..1) {
            sameCardSet.add(targetCardSet.elementAt(targetCardSet.lastIndex - 2))
        }
        luckyGame.participantList[0].setCards(sameCardSet)

        // 1번 참가자의 차례에서 카드 3개를 뽑았는데, 같은 숫자가 3개 나온 상황 연출
        val selectedCard = (luckyGame.participantList[0].getCards().take(3) as MutableList<Card>)
        luckyGame.setResultCard(selectedCard, 0)

        // 표시 화면에서 제거되었는지 확인
        var isSuccessfullyDeleted = false
        if (luckyGame.participantList[0].getCards().size == 5) {
            isSuccessfullyDeleted = true
        }

        // 결과 화면에 추가되었는지 확인
        var isSuccessfullyAdded = false
        if (luckyGame.participantResultList[0].getCards().size == 3) {
            isSuccessfullyAdded = true
        }

        assertEquals(true, isSuccessfullyAdded && isSuccessfullyDeleted)
    }

    @Test
    fun checkSumDiff7_IsValid_02() {
        // 숫자 리스트에서 합이나 차가 7인 경우 true
        assertEquals(listOf(0, 2), luckyGame.isSumDiffIs7(listOf(1, 4, 8)))
    }

    @Test
    fun checkSumDiff7_IsValid_1010() {
        // 숫자 리스트에서 합이나 차가 7인 경우 true. 이 경우에선 false인 (10,10)이 나와야함
        assertEquals(listOf(10, 10), luckyGame.isSumDiffIs7(listOf(1, 4, 9)))
    }

    @Test
    fun checkFinishState_When1and8_02() {
        // 1번참가자가 숫자1 3개모았고, 2번참가자가 숫자4 3개모았고, 3번참가자가 숫자8 3개모았을때 상황 구현
        // 1과 8의 차가 7임으로 종료조건에 해당한다.
        // 승자는 1번참가자와 3번참가자.
        val card1num = Card(Animal.Dog, 1, false)
        val card4num = Card(Animal.Dog, 4, false)
        val card8num = Card(Animal.Dog, 8, false)

        luckyGame.shareCard(3)
        for (i in 1..3) {
            luckyGame.participantResultList[0].addCard(card1num)
        }
        for (i in 1..3) {
            luckyGame.participantResultList[1].addCard(card4num)
        }
        for (i in 1..3) {
            luckyGame.participantResultList[2].addCard(card8num)
        }

        val result = luckyGame.checkFinishStatus()
        assertEquals(listOf(0, 2), result)
    }

    @Test
    fun checkFinishState_When7_0() {
        // 1번참가자가 숫자7 3개모았고, 2번참가자가 숫자4 3개모았고, 3번참가자가 숫자8 3개모았을때 상황 구현
        // 1번참가자가 뽑은 숫자가 7임으로 종료조건에 해당한다.
        // 승자는 1번참가자
        val card7num = Card(Animal.Dog, 7, false)
        val card4num = Card(Animal.Dog, 4, false)
        val card8num = Card(Animal.Dog, 8, false)

        luckyGame.shareCard(3)
        for (i in 1..3) {
            luckyGame.participantResultList[0].addCard(card7num)
        }
        for (i in 1..3) {
            luckyGame.participantResultList[1].addCard(card4num)
        }
        for (i in 1..3) {
            luckyGame.participantResultList[2].addCard(card8num)
        }

        assertEquals(listOf(0), luckyGame.checkFinishStatus())
    }
}