package com.glacier.luckycardgamesofteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        } else {
            binding.tvResultGuide.text = "이번 게임에서는 승자가 없습니다!\n아래 재시작 버튼을 눌러 게임을 재시작해주세요."
        }

        binding.btnRestart.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
        }

    }
}