package com.glacier.luckycardgamesofteer

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
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

        // 리사이클러뷰 아이템 동일간격으로 펼치기
//        val spaceDecoration = object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                outRect.set(10, 10, 10, 10)
//            }
//        }
//        binding.rv0.addItemDecoration(spaceDecoration)

        binding.mbToggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                // 명수가 변경될 때 마다 카드를 새로 뽑는다
                var cardList = initCard()

                // 뽑은 카드를 한번 섞는다
                cardList.shuffle()

                when (checkedId){
                    R.id.btn_3people -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE
                        setLayoutWeight(binding.cvEnd, 0.8f)

                        binding.btn3people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.btn4people.icon = null
                        binding.btn5people.icon = null

                        setCardRecyclerView(cardList, 3)
                    }
                    R.id.btn_4people -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.VISIBLE
                        setLayoutWeight(binding.cvEnd, 0.8f)

                        binding.btn3people.icon = null
                        binding.btn4people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.btn5people.icon = null

                        setCardRecyclerView(cardList, 4)
                    }
                    R.id.btn_5people -> {
                        binding.cv5.visibility = View.VISIBLE
                        binding.cv4.visibility = View.VISIBLE
                        setLayoutWeight(binding.cvEnd, 1.0f)

                        binding.btn3people.icon = null
                        binding.btn4people.icon = null
                        binding.btn5people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)

                        setCardRecyclerView(cardList, 5)
                    }
                }
            }
        }
        // 처음 키면 첫번째 옵션으로
        binding.mbToggle.check(R.id.btn_3people)
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

    fun setCardRecyclerView(cardList: MutableList<Card>, numOfParticipants: Int){
        // share cards
        val participants = shareCard(cardList, numOfParticipants)

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

        if (numOfParticipants >= 4){
            binding.rv4.apply {
                layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(participants[3].getCards(), false)
            }
        }
        if (numOfParticipants == 5){
            binding.rv5.apply {
                layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(participants[4].getCards(), false)
            }
        }

        binding.rv0.apply {
            when(numOfParticipants){
                3 -> layoutManager = GridLayoutManager(applicationContext, 5)
                4 -> layoutManager = GridLayoutManager(applicationContext, 4)
                5 -> layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
            adapter = CardAdapter(participants[3].getCards(), false)
        }
    }

    fun setLayoutWeight(view: CardView, weight: Float){
        var cvendLp = view.layoutParams as LinearLayout.LayoutParams
        cvendLp.weight = weight
        view.layoutParams = cvendLp
    }

}