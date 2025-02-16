package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityTermixTurboMinigameBinding
import java.util.Locale

class TermixTurboMinigame : AppCompatActivity() {

    private lateinit var termixTurboMinigameBinding: ActivityTermixTurboMinigameBinding
    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null
    private val totalTimeInMillis: Long = 60000 // 1 minuto = 60000 milissegundos
    private var wordsGuessedCorrectly = 0 // Variável para contar as palavras corretas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla os componentes da interface
        termixTurboMinigameBinding = ActivityTermixTurboMinigameBinding.inflate(layoutInflater)
        setContentView(termixTurboMinigameBinding.root)

        // Referencia o TextView para o timer
        timerText = findViewById(R.id.timer_text)

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
            }

            override fun onFinish() {
                timerText.text = getString(R.string._00_00)
                // Quando o tempo acabar, você pode mostrar o número de palavras corretas
                endGame()
            }
        }.start()
    }

    // TODO: adicionar a lógica para verificar se a palavra foi adivinhada corretamente
    private fun onWordGuessedCorrectly() {
        wordsGuessedCorrectly++
        // Aqui você pode adicionar mais lógica, como feedback para o jogador
    }

    // TODO: adicionar a lógica para interromper o jogo quando o timer chegar a 00:00
    private fun endGame() {
        // Exibe o número de palavras corretas ao final do jogo
        // Aqui você pode fazer algo com a variável `wordsGuessedCorrectly`
        // Exemplo: mostrar um diálogo ou uma nova tela com a quantidade de acertos
    }

    // Configura todos os eventos de botão
    private fun registerButtonEvents() {
        // Volta à tela de minijogos
        termixTurboMinigameBinding.backToHomeTurboBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Move para a activity do perfil do jogador
        termixTurboMinigameBinding.profileTurboBtn.setOnClickListener {
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
