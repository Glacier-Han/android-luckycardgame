package com.glacier.luckycardgamesofteer

import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.model.Participant

// 요구사항 : 참가자와 화면 전체를 포함하는 LuckyGame 객체를 작성한다
// 요구사항을 해결하기 위해 전반적인 카드게임 로직에 해당하는 기능을 모두 LuckyGame 클래스에 담았다.
class LuckyGame {

    var cardList = mutableListOf<Card>()
    var participantList = mutableListOf<Participant>()

    fun initCard(){
        // 12개의 전체 카드 객체를 저장하기 위한 MutableList 선언
        cardList.clear()
        participantList.clear()

        // AnimalInfo enum중 랜덤하게 뽑은 카드를 총 12개 생성 후 리스트에 담는다. 숫자는 1~12로 지정
        for(i in 1..12){
            for (animal in Animal.values()) {
                val pickedCard = Card(animal, i)
                cardList.add(pickedCard)
            }
        }
    }

    fun shareCard(numOfParticipants: Int){

        // 카드들을 수정할일이 생기기 때문에 (나누어 주는 과정) 깊은 복사를 하여 새로운 리스트로 만듦

        // 3명일때는 12번 카드는 제외시켜야함.
        if(numOfParticipants == 3){
            cardList = cardList.filter { it.num != 12 } as MutableList<Card>
        }

        // 참가자 한명당 카드를 배분함
        for (i in 1..numOfParticipants) {
            // 참가자 객체 생성
            val participant = Participant(i.toString())

            // 총 참가자 수에 따라 나누어 주는 카드 갯수가 다르기 때문에 분기
            val numCardsPerParticipant = when (numOfParticipants) {
                3 -> 8
                4 -> 7
                5 -> 6
                else -> 0
            }

            // 카드 중에서 정해진 갯수만큼 들고온 다음, 뽑힌 카드는 삭제함
            participant.addAllCard(cardList.take(numCardsPerParticipant))
            cardList = cardList.drop(numCardsPerParticipant) as MutableList<Card>

            // 참가자 생성 후 참가자 리스트에 추가함
            participantList.add(participant)
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

}