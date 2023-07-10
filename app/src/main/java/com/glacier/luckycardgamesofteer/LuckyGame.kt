package com.glacier.luckycardgamesofteer

import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.model.Participant

// 요구사항 : 참가자와 화면 전체를 포함하는 LuckyGame 객체를 작성한다
// 요구사항을 해결하기 위해 전반적인 카드게임 로직에 해당하는 기능을 모두 LuckyGame 클래스에 담았다.
class LuckyGame {

    var cardList = mutableListOf<Card>()
    var participantList = mutableListOf<Participant>()
    var participantResultList = mutableListOf<Participant>()

    fun initCard() {
        // 12개의 전체 카드 객체를 저장하기 위한 MutableList 선언

        cardList.clear()
        participantList.clear()

        // AnimalInfo enum중 랜덤하게 뽑은 카드를 총 12개 생성 후 리스트에 담는다. 숫자는 1~12로 지정
        for (i in 1..12) {
            for (animal in Animal.values()) {
                val pickedCard = Card(animal, i, true)
                cardList.add(pickedCard)
            }
        }
    }

    fun shareCard(numOfParticipants: Int) {

        // 카드들을 수정할일이 생기기 때문에 (나누어 주는 과정) 깊은 복사를 하여 새로운 리스트로 만듦

        // 3명일때는 12번 카드는 제외시켜야함.
        if (numOfParticipants == 3) {
            cardList = cardList.filter { it.num != 12 } as MutableList<Card>
        }

        // 참가자 한명당 카드를 배분함
        for (i in 1..numOfParticipants) {
            // 참가자 객체 생성
            val participant = Participant(i.toString())
            val participantResult = Participant(i.toString())

            // 총 참가자 수에 따라 나누어 주는 카드 갯수가 다르기 때문에 분기
            val numCardsPerParticipant = when (numOfParticipants) {
                3 -> 8
                4 -> 7
                5 -> 6
                else -> 0
            }

            // 카드 중에서 정해진 갯수만큼 들고온 다음, 뽑힌 카드는 삭제함, 정렬된 채로 넣어야함!
            participant.addAllCard(cardList.take(numCardsPerParticipant).sortedBy { it.num })
            cardList = cardList.drop(numCardsPerParticipant) as MutableList<Card>

            // 참가자 생성 후 참가자 리스트에 추가함
            participantList.add(participant)
            participantResultList.add(participantResult)
            //participant.showCards()
        }

        // 마찬가지로 남은 카드 갯수가 총 참가자 명수에 따라 다르기 때문에 분기
        val numOfRemainCards = when (numOfParticipants) {
            3 -> 9
            4 -> 8
            5 -> 6
            else -> 0
        }

        // 남은 카드 따로 모으기
        val remainCards = Participant("0")

        // 마지막으로 남은 카드들을 저장
        remainCards.addAllCard(cardList.take(numOfRemainCards))

        // 남은 카드 또한 참가자 리스트에 넣고 최종 리턴
        participantList.add(remainCards)
        //remainCards.showCards()
    }

    // 메소드 : 참가자별로 카드를 숫자 오름차순으로 정렬할 수 있어야 한다
    fun sortCardAscend(indexOfParticipant: Int) {
        participantList[indexOfParticipant].setCards(
            participantList[indexOfParticipant].getCards().sortedBy { it.num } as MutableList<Card>)
    }

    // 메소드 : 바닥에 깔린 카드도 숫자 오름차순으로 정렬할 수 있어야 한다
    fun sortRemainAscend() {
        // 현재 남은 카드는 participantList의 마지막 인덱스에 들어있음
        participantList[participantList.lastIndex].setCards(
            participantList[participantList.lastIndex].getCards()
                .sortedBy { it.num } as MutableList<Card>)
    }

    // 메소드 :참가자 중에 같은 숫자 카드 3장을 가진 경우가 있는지 판단할 수 있다
    fun isSameCardInParticipants(): Boolean {
        var result = false
        // 모든 참가자 대상 검사 (대신 participantList의 마지막 객체는 "남은카드" 임으로 dropLast(1) 해줌
        for (participant in participantList.dropLast(1)) {
            val isSameInParticipant = checkThreeSameNumber(participant.getCards())
            if (isSameInParticipant) result = true
        }

        return result
    }

    // 메소드 : 특정 참가자와 해당 참가자 카드 중에 가장 낮은 숫자 또는 가장 높은 숫자, 바닥 카드 중 아무거나를 선택해서 3개가 같은지 판단할 수 있어야 한다.
    fun isSameCardInSpecificCase(targetIndex1: Int, targetIndex2: Int, isMin: Boolean): Boolean {
        val targetCards1 = participantList[targetIndex1].getCards()
        val targetCards2 = participantList[targetIndex2].getCards()
        val remainCards = participantList[participantList.lastIndex].getCards()

        val targetNum1 =
            if (isMin) targetCards1.minOf { it.num }
            else targetCards1.maxOf { it.num }

        val targetNum2 =
            if (isMin) targetCards2.minOf { it.num }
            else targetCards2.maxOf { it.num }

        val targetNum3 = remainCards.random().num

        return (targetNum1 == targetNum2 && targetNum2 == targetNum3)
    }

    fun checkThreeSameNumber(numbers: List<Card>): Boolean {
        val numCounts = mutableMapOf<Int, Int>()

        for (number in numbers) {
            val count = numCounts.getOrDefault(number.num, 0) + 1
            numCounts[number.num] = count
            if (count >= 3) {
                return true
            }
        }

        return false
    }


    // TODO 게임메소드 추가
    // 선택한 카드 뒤집기
    fun filpCard(participantNum: Int, cardIndex: Int) {
        participantList[participantNum].getCards()[cardIndex].isBack = false
    }

    // 정렬된 상태로 가장 작은 숫자가 있는 왼쪽 또는 가장 큰 숫자가 있는 오른쪽 카드만 터치가 가능하다.
    // 만약 가장 오른쪽 카드를 뒤집어서 앞면이 보이는 상태에서는 그 다음 숫자도 터치 가능하다
    fun isCardCanFilp(participantNum: Int, cardIndex: Int): Boolean {
        val cards = participantList[participantNum].getCards()
        val isBackList = cards.filter { it.isBack }

        if (isBackList.isEmpty()) {
            return false
        }

        // 뒷면상태인 카드 중에서 양끝값만 선택 가능하게 구현
        return cards[cardIndex] == isBackList[0] || cards[cardIndex] == isBackList[isBackList.lastIndex]
    }

    // 사용자별로 총 3장의 카드만 뒤집을 수 있다
    fun isCardFilpedCountUnder3(participantNum: Int): Boolean {
        return participantResultList[participantNum].getCards().count { !it.isBack } <= 3
    }

    // 뽑은 3장의 카드가 똑같은지 확인
    private fun is3CardIsSame(cards: MutableList<Card>): Boolean {
        val firstNum = cards[0].num
        for (i in 1 until cards.size) {
            if (cards[i].num != firstNum) {
                return false
            }
        }
        return true
    }

    // 3장의 카드가 똑같은 걸 뽑으면 결과리스트에 추가 및 기존 리스트에서 제거
    fun setResultCard(cards: MutableList<Card>, participantNum: Int) {
        if (is3CardIsSame(cards)) {
            participantList[participantNum].removeCards(cards)
            participantResultList[participantNum].addAllCard(cards)
        }
    }

    /*
        게임이 끝나는 경우는 3장을 모은 카드 숫자 합 또는 차가 7이면 끝난다.
        누군가 한 명이 7을 모아도 끝나고, A와 B가 각각 1과 8을 모았어도 끝난다.
    */
    fun checkFinishStatus(): Boolean {
        var isFinishStatus = false

        val sumdiffCheckList = mutableListOf<Int>()
        for (participant in participantResultList) {
            val cards = participant.getCards()
            if (cards[0].num == 7) {
                isFinishStatus = true
            } else {
                sumdiffCheckList.add(cards[0].num)
            }
        }

        isFinishStatus = isFinishStatus || isSumDiffIs7(sumdiffCheckList)

        return isFinishStatus
    }

    // 모은 카드 숫자 합 또는 차가 7이면 True
    fun isSumDiffIs7(cardList: List<Int>): Boolean {
        for (i in 0 until cardList.size - 1) {
            for (j in i + 1 until cardList.size) {
                val sum = cardList[i] + cardList[j]
                val difference = kotlin.math.abs(cardList[i] - cardList[j])
                if (sum == 7 || difference == 7) {
                    return true
                }
            }
        }
        return false
    }



}