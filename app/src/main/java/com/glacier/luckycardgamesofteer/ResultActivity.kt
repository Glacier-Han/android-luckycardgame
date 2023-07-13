package com.glacier.luckycardgamesofteer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glacier.luckycardgamesofteer.adapter.CardAdapter
import com.glacier.luckycardgamesofteer.adapter.ResultCardAdapter
import com.glacier.luckycardgamesofteer.databinding.ActivityResultBinding
import com.glacier.luckycardgamesofteer.model.Participant
import java.util.ArrayList

class ResultActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val result = intent.getBooleanExtra("IsWinner", false)

        if (result) {
            val winners = intent.getIntegerArrayListExtra("WinnerList")
            val winnerDB = intent.getSerializableExtra("WinnerDB") as ArrayList<Participant>
            Log.d("DBG", winners.toString())
            Log.d("DBG", winnerDB.toString())

            binding.tvWinner.text =
                "이번 게임은 ${winners?.let { convertNumbersToLetters(it) }}가 승리했습니다!"
            setRecyclerView(winnerDB)
            setWinnerRvHighlight(winners!!)

        } else {
            binding.tvWinner.text = "이번 게임에서는 승자가 없습니다!\n아래 재시작 버튼을 눌러 게임을 재시작해주세요."
        }

        binding.btnRestart.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
        }

    }

    private fun setRecyclerView(winnerDB: ArrayList<Participant>) {
        try {
            with(binding.rv1) {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = ResultCardAdapter(winnerDB[0].getCards())
            }

            with(binding.rv2) {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = ResultCardAdapter(winnerDB[1].getCards())
            }

            with(binding.rv3) {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = ResultCardAdapter(winnerDB[2].getCards())
            }

            with(binding.rv4) {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = ResultCardAdapter(winnerDB[3].getCards())
            }

            with(binding.rv5) {
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                adapter = ResultCardAdapter(winnerDB[4].getCards())
            }
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    private fun setWinnerRvHighlight(winners: ArrayList<Int>) {
        for (winner in winners) {
            when (winner) {
                0 -> binding.cv1.setCardBackgroundColor(Color.parseColor("#FF66CC"))
                1 -> binding.cv2.setCardBackgroundColor(Color.parseColor("#FF66CC"))
                2 -> binding.cv3.setCardBackgroundColor(Color.parseColor("#FF66CC"))
                3 -> binding.cv4.setCardBackgroundColor(Color.parseColor("#FF66CC"))
                4 -> binding.cv5.setCardBackgroundColor(Color.parseColor("#FF66CC"))
            }
        }
    }

    private fun convertNumbersToLetters(numbers: ArrayList<Int>): List<String> {
        return numbers.map { convertNumberToLetter(it) }
    }

    private fun convertNumberToLetter(number: Int): String {
        return when (number) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            4 -> "E"
            else -> ""
        }
    }

}