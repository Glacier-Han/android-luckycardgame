package com.glacier.luckycardgamesofteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.glacier.luckycardgamesofteer.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        pickCard(initCard())
    }

    fun init(){
        binding.mbToggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                when (checkedId){
                    R.id.button1 -> {
                        binding.cv5.visibility = View.GONE
                        binding.cv4.visibility = View.INVISIBLE

                        binding.button1.icon = AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_check_24)
                        binding.button2.icon = null
                        binding.button3.icon = null
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
        var cardList = mutableListOf<Card>()

        for (i in 1..12) {
            val pickedCard = Card(AnimalInfo.values().random(), i)
            cardList.add(pickedCard)
        }

        return cardList
    }

    fun pickCard(cardList: MutableList<Card>) {
        var results = ""

        cardList.shuffle()

        for (card in cardList) {
            results += String.format("%s%02d, ", card.animalType.unicode, card.num)
        }

        // 마지막 쉼표와 공백은 제외하고 콘솔창에 출력하기 위해 dropLast(2)를 사용
        Log.d("ShowCard", results.dropLast(2))
    }

}