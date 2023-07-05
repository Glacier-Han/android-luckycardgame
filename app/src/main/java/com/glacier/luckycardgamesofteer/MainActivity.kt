package com.glacier.luckycardgamesofteer

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.adapter.CardAdapter
import com.glacier.luckycardgamesofteer.databinding.ActivityMainBinding
import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.model.Participant


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

    }

    fun init(){
        // https://selfish-developer.com/entry/%EA%B2%B9%EC%B9%98%EB%8A%94-recyclerview-%EB%A7%8C%EB%93%A4%EA%B8%B0

        binding.mbToggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                // 명수가 변경될 때 마다 카드를 새로 뽑는다
                var cardList = initCard()

                // 뽑은 카드를 한번 섞는다
                cardList.shuffle()

                when (checkedId){
                    R.id.button1 -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE

                        binding.button1.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.button2.icon = null
                        binding.button3.icon = null

                        // share cards
                        val participants = shareCard(cardList, 3)

                        // recyclerview 어댑터 설정
                        binding.rv1.apply {
                            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                            adapter = CardAdapter(participants[0].getCards(), true)
                        }

                        binding.rv2.apply {
                            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                            adapter = CardAdapter(participants[1].getCards(), false)
                        }

                        binding.rv3.apply {
                            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                            adapter = CardAdapter(participants[2].getCards(), false)
                        }

                        binding.rv0.apply {
                            layoutManager = GridLayoutManager(applicationContext, 5)
                            adapter = CardAdapter(participants[3].getCards(), false)
                        }

                    }
                    R.id.button2 -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.VISIBLE

                        binding.button1.icon = null
                        binding.button2.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.button3.icon = null
                    }
                    R.id.button3 -> {
                        binding.cv5.visibility = View.VISIBLE
                        binding.cv4.visibility = View.VISIBLE

                        binding.button1.icon = null
                        binding.button2.icon = null
                        binding.button3.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                    }
                }
            }
        }
        // 처음 키면 첫번째 옵션으로
        binding.mbToggle.check(R.id.button1)
    }

    fun initCard(): MutableList<Card> {

        // 12개의 전체 카드 객체를 저장하기 위한 MutableList 선언
        val cardList = mutableListOf<Card>()

        // AnimalInfo enum중 랜덤하게 뽑은 카드를 총 12개 생성 후 리스트에 담는다. 숫자는 1~12로 지정
        for(i in 1..12){
            for (animal in Animal.values()) {
                val pickedCard = Card(animal, i)
                cardList.add(pickedCard)
            }
        }

        return cardList
    }

    fun pickCard(cardList: MutableList<Card>) {
        var results = ""

        for (card in cardList) {
            results += String.format("%s%02d, ", card.animalType.unicode, card.num)
        }

        // 마지막 쉼표와 공백은 제외하고 콘솔창에 출력하기 위해 dropLast(2)를 사용
        Log.d("ShowCard", results.dropLast(2))
    }

    fun shareCard(cardList_:MutableList<Card>, numOfParticipants: Int): MutableList<Participant>{
        val participantList = mutableListOf<Participant>()

        // 카드들을 수정할일이 생기기 때문에 (나누어 주는 과정) 깊은 복사를 하여 새로운 리스트로 만듦
        var cardList = mutableListOf<Card>()
        cardList.addAll(cardList_)

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
            participant.showCards()
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
        remainCards.showCards()

        return participantList
    }

}