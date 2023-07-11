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

    fun init() {
        setRecyclerViewDecoration()
        setButtonCheckedListener()
        binding.mbToggle.check(R.id.btn_3people)
    }

    fun initCard(): MutableList<Card> {
        val cardList = mutableListOf<Card>()

        for (i in 1..12) {
            for (animal in Animal.values()) {
                val pickedCard = Card(animal, i)
                cardList.add(pickedCard)
            }
        }

        return cardList
    }

    fun shareCard(cardList_: MutableList<Card>, numOfParticipants: Int): MutableList<Participant> {
        val participantList = mutableListOf<Participant>()

        // 카드들을 수정할일이 생기기 때문에 (나누어 주는 과정) 깊은 복사를 하여 새로운 리스트로 만듦
        var cardList = mutableListOf<Card>()
        cardList.addAll(cardList_)

        if (numOfParticipants == 3) {
            cardList = cardList.filter { it.num != 12 } as MutableList<Card>
        }

        for (i in 1..numOfParticipants) {
            val participant = Participant(i.toString())
            val numCardsPerParticipant = when (numOfParticipants) {
                3 -> 8
                4 -> 7
                5 -> 6
                else -> 0
            }

            participant.addAllCard(cardList.take(numCardsPerParticipant))
            cardList = cardList.drop(numCardsPerParticipant) as MutableList<Card>

            participantList.add(participant)
            //participant.showCards()
        }

        val numOfRemainCards = when (numOfParticipants) {
            3 -> 9
            4 -> 8
            5 -> 6
            else -> 0
        }

        val remainCards = Participant("0")
        remainCards.addAllCard(cardList.take(numOfRemainCards))
        participantList.add(remainCards)
        //remainCards.showCards()

        return participantList
    }

    fun setCardRecyclerView(cardList: MutableList<Card>, numOfParticipants: Int) {
        val participants = shareCard(cardList, numOfParticipants)

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

        if (numOfParticipants >= 4) {
            binding.rv4.apply {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(participants[3].getCards(), false)
            }
        }
        if (numOfParticipants == 5) {
            binding.rv5.apply {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(participants[4].getCards(), false)
            }
        }

        // 남은카드 리사이클러뷰는 갯수에따라 spancount나 layout이 달라져야함
        binding.rv0.apply {
            when (numOfParticipants) {
                3 -> layoutManager = GridLayoutManager(applicationContext, 5)
                4 -> layoutManager = GridLayoutManager(applicationContext, 4)
                5 -> layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
            adapter = CardAdapter(participants[3].getCards(), false)
        }
    }

    fun setLayoutWeight(view: CardView, weight: Float) {
        val cvendLp = view.layoutParams as LinearLayout.LayoutParams
        cvendLp.weight = weight
        view.layoutParams = cvendLp
    }

    fun setRecyclerViewDecoration() {
        // 참가자카드 리사이클러뷰 아이템 겹치게 보이기를 위한 데코레이션
        val deco = OverlapDecoration()

        // 남은카드 리사이클러뷰 아이템 동일간격으로 펼치기를 위한 데코레이션
        val spaceDeco = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                when (state.itemCount) {
                    7 -> outRect.set(30, 10, 30, 10)
                    9 -> outRect.set(10, 10, 10, 10)
                }
            }
        }

        binding.rv0.addItemDecoration(spaceDeco)
        binding.rv1.addItemDecoration(deco)
        binding.rv2.addItemDecoration(deco)
        binding.rv3.addItemDecoration(deco)
        binding.rv4.addItemDecoration(deco)
        binding.rv5.addItemDecoration(deco)
    }

    fun setButtonCheckedListener() {
        binding.mbToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val cardList = initCard()
                cardList.shuffle()

                when (checkedId) {
                    R.id.btn_3people -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE
                        binding.btn3people.icon = AppCompatResources.getDrawable(
                            applicationContext,
                            R.drawable.baseline_check_24
                        )
                        binding.btn4people.icon = null
                        binding.btn5people.icon = null

                        // 참가자 3, 4명일때는 남은 카드 뷰가 크게 보여야 하기때문에 weight 변경
                        setLayoutWeight(binding.cvEnd, 0.8f)
                        setCardRecyclerView(cardList, 3)
                    }

                    R.id.btn_4people -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.VISIBLE
                        binding.btn3people.icon = null
                        binding.btn4people.icon = AppCompatResources.getDrawable(
                            applicationContext,
                            R.drawable.baseline_check_24
                        )
                        binding.btn5people.icon = null

                        setLayoutWeight(binding.cvEnd, 0.8f)
                        setCardRecyclerView(cardList, 4)
                    }

                    R.id.btn_5people -> {
                        binding.cv5.visibility = View.VISIBLE
                        binding.cv4.visibility = View.VISIBLE
                        binding.btn3people.icon = null
                        binding.btn4people.icon = null
                        binding.btn5people.icon = AppCompatResources.getDrawable(
                            applicationContext,
                            R.drawable.baseline_check_24
                        )

                        setLayoutWeight(binding.cvEnd, 1.0f)
                        setCardRecyclerView(cardList, 5)
                    }
                }
            }
        }
    }

}