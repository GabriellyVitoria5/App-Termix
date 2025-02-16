package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityTimeLimitMinigameBinding
import java.util.Locale

class TimeLimitMinigame : AppCompatActivity() {

    private lateinit var tileLimiteMinigameBinding: ActivityTimeLimitMinigameBinding
    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null
    private val totalTimeInMillis: Long = 60000 // 1 minuto = 60000 milissegundos
    private var isGameWon = false // Variável que indica se o jogador acertou a palavra

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla os componentes da interface
        tileLimiteMinigameBinding = ActivityTimeLimitMinigameBinding.inflate(layoutInflater)
        setContentView(tileLimiteMinigameBinding.root)

        // Referencia o TextView para o timer
        timerText = findViewById(R.id.timer_time_limit)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Registra eventos de botão
        registerButtonEvents()

        // Inicia o timer
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                timerText.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

                // Se o jogo foi ganho, para o timer
                if (isGameWon) {
                    cancelTimer()
                }
            }

            override fun onFinish() {
                timerText.text = getString(R.string._00_00)
                // TODO: adicionar a lógica para interromper o jogo quando o timer chegar a 00:00
                if (!isGameWon) {
                    endGame() // Finaliza o jogo se o tempo acabar e o jogador não tiver ganhado
                }
            }
        }.start()
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
    }

    // TODO: adicionar a lógica para verificar se a palavra foi adivinhada corretamente
    private fun onWordGuessedCorrectly() {
        isGameWon = true
        // Para o timer quando o jogador acertar
        cancelTimer()
        // Ação adicional quando o jogador acertar
        // Você pode adicionar lógica para mostrar uma tela de vitória ou outra coisa
    }

    // TODO: adicionar a lógica para interromper o jogo quando o timer chegar a 00:00
    private fun endGame() {}

    // Configura todos os eventos de botão
    private fun registerButtonEvents() {
        // Volta à tela de minijogos
        tileLimiteMinigameBinding.backToHomeTimeLimitBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Move para a activity do perfil do jogador
        tileLimiteMinigameBinding.profileTimeLimiteBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancela o timer se a activity for destruída antes de terminar
        countDownTimer?.cancel()
    }
}
