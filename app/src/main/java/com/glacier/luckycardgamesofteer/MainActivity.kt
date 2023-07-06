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
    private lateinit var luckyGame: LuckyGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 럭키 게임 객체 생성
        luckyGame = LuckyGame()
        init()

    }

    fun init(){

        luckyGame.initCard()

        // 참가자카드 리사이클러뷰 아이템 겹치게 보이기
        val deco = OverlapDecoration()

        // 남은카드 리사이클러뷰 아이템 동일간격으로 펼치기
        val spaceDeco = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                when(state.itemCount){
                    7 -> outRect.set(30, 10, 30, 10)
                    9 -> outRect.set(10,10,10,10)
                }
            }
        }

        binding.rv0.addItemDecoration(spaceDeco)
        binding.rv1.addItemDecoration(deco)
        binding.rv2.addItemDecoration(deco)
        binding.rv3.addItemDecoration(deco)
        binding.rv4.addItemDecoration(deco)
        binding.rv5.addItemDecoration(deco)

        binding.mbToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(isChecked){
                // 명수가 변경될 때 마다 카드를 새로 뽑는다
                luckyGame.initCard()
                val cardList = luckyGame.cardList

                // 뽑은 카드를 한번 섞는다
                cardList.shuffle()

                when (checkedId){
                    R.id.btn_3people -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE
                        binding.btn3people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
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
                        binding.btn4people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.btn5people.icon = null

                        setLayoutWeight(binding.cvEnd, 0.8f)
                        setCardRecyclerView(cardList, 4)
                    }
                    R.id.btn_5people -> {
                        binding.cv5.visibility = View.VISIBLE
                        binding.cv4.visibility = View.VISIBLE
                        binding.btn3people.icon = null
                        binding.btn4people.icon = null
                        binding.btn5people.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)

                        // 참가자 5명일땐 남은카드 카드뷰의 weight값 동일하게 줘서 참가자 카드뷰와 동일한 높이로
                        setLayoutWeight(binding.cvEnd, 1.0f)
                        setCardRecyclerView(cardList, 5)
                    }
                }
            }
        }
        // 처음 키면 첫번째 옵션으로
        binding.mbToggle.check(R.id.btn_3people)
    }

    fun setCardRecyclerView(cardList: MutableList<Card>, numOfParticipants: Int){
        // share cards
        luckyGame.shareCard(numOfParticipants)

        // recyclerview 어댑터 설정
        binding.rv1.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            adapter = CardAdapter(luckyGame.participantList[0].getCards(), true)
        }

        binding.rv2.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            adapter = CardAdapter(luckyGame.participantList[1].getCards(), false)
        }

        binding.rv3.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            adapter = CardAdapter(luckyGame.participantList[2].getCards(), false)
        }

        if (numOfParticipants >= 4){
            binding.rv4.apply {
                layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(luckyGame.participantList[3].getCards(), false)
            }
        }
        if (numOfParticipants == 5){
            binding.rv5.apply {
                layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = CardAdapter(luckyGame.participantList[4].getCards(), false)
            }
        }

        // 남은카드 리사이클러뷰는 갯수에따라 spancount나 layout이 달라져야함
        binding.rv0.apply {
            when(numOfParticipants){
                3 -> layoutManager = GridLayoutManager(applicationContext, 5)
                4 -> layoutManager = GridLayoutManager(applicationContext, 4)
                5 -> layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
            adapter = CardAdapter(luckyGame.participantList[3].getCards(), false)
        }
    }

    fun setLayoutWeight(view: CardView, weight: Float){
        val cvendLp = view.layoutParams as LinearLayout.LayoutParams
        cvendLp.weight = weight
        view.layoutParams = cvendLp
    }

}