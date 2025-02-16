package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
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
    private lateinit var increaseTimeBtn: ImageButton
    private lateinit var decreaseTimeBtn: ImageButton
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 60000 // Inicia com 1 minuto = 60000 milissegundos
    private var wordsGuessedCorrectly = 0 // Variável para contar as palavras corretas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla os componentes da interface
        termixTurboMinigameBinding = ActivityTermixTurboMinigameBinding.inflate(layoutInflater)
        setContentView(termixTurboMinigameBinding.root)

        // Referencia os componentes
        timerText = findViewById(R.id.time_display)
        increaseTimeBtn = findViewById(R.id.increase_time_btn)
        decreaseTimeBtn = findViewById(R.id.decrease_time_btn)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Registra eventos de botão
        registerButtonEvents()

        // Inicia o timer
        startTimer()

        // Configura botões de aumentar e diminuir tempo
        increaseTimeBtn.setOnClickListener {
            changeTime(0.5) // Aumenta 30 segundos
        }

        decreaseTimeBtn.setOnClickListener {
            changeTime(-0.5) // Diminui 30 segundos
        }
    }

    private fun startTimer() {
        // Cancela o timer atual se já existir antes de criar um novo
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
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

    // Aumenta ou diminui o tempo
    private fun changeTime(minutesToAdd: Double) {
        timeLeftInMillis += (minutesToAdd * 60000).toLong() // Cada minuto é 60000 milissegundos
        if (timeLeftInMillis < 0) {
            timeLeftInMillis = 0 // Não permite valores negativos
        }
        startTimer() // Reinicia o timer com o novo tempo
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
