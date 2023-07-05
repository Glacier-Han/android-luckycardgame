package com.glacier.luckycardgamesofteer

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.adapter.CardAdapter
import com.glacier.luckycardgamesofteer.databinding.ActivityMainBinding
import com.glacier.luckycardgamesofteer.model.Animal
import com.glacier.luckycardgamesofteer.model.Card
import com.glacier.luckycardgamesofteer.model.Participant


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var cardList : MutableList<Card>


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
                pickCard(initCard())

                when (checkedId){
                    R.id.button1 -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE

                        binding.button1.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.button2.icon = null
                        binding.button3.icon = null

                        // Share Cards
                        // 12번 카드 3개 제외
                        cardList.dropLast(3)

                        // 카드 섞기
                        cardList.shuffle()

                        // 참가자들에게 카드 나눠주기
                        val participant1 = Participant("1")
                        participant1.addAllCard(cardList.take(8))

                        val participant2 = Participant("2")
                        participant2.addAllCard(cardList.take(8))

                        val participant3 = Participant("3")
                        participant3.addAllCard(cardList.take(8))

                        val etcCards = Participant("0")
                        etcCards.addAllCard(cardList.take(9))

                        // recyclerview 어댑터 설정
                        val cardAdapter = CardAdapter(participant1.getCards())
                        val linearLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)

                        binding.rv1.apply {
                            layoutManager = linearLayoutManager
                            adapter = cardAdapter
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
        cardList = mutableListOf()

        // AnimalInfo enum중 랜덤하게 뽑은 카드를 총 12개 생성 후 리스트에 담는다. 숫자는 1~12로 지정
        for(i in 1..12){
            for (animal in Animal.values()) {
                val pickedCard = Card(animal, i)
                cardList.add(pickedCard)
            }
        }

        // 생성한 카드 리스트를 리턴한다.
        return cardList
    }

    fun pickCard(cardList: MutableList<Card>) {
        // 콘솔에 결과를 표시하기 위한 String 변수
        var results = ""

        // 뽑기 전에 카드들을 한번 섞는다
        //cardList.shuffle()

        // 카드를 12개 다 뽑아서 콘솔에 정해진 형식대로 출력한다.
        for (card in cardList) {
            results += String.format("%s%02d, ", card.animalType.unicode, card.num)
        }

        // 마지막 쉼표와 공백은 제외하고 콘솔창에 출력한다.
        Log.d("ShowCard", results.dropLast(2))
    }

    fun dp2px(ctx: Context, dp: Float): Int {
        val scale = ctx.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}