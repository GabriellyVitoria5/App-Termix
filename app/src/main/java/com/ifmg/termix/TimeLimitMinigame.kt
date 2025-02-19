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
import com.ifmg.termix.controller.GameController
import com.ifmg.termix.databinding.ActivityTimeLimitMinigameBinding
import java.util.Locale

class TimeLimitMinigame : AppCompatActivity() {

    private lateinit var timeLimitMinigameBinding: ActivityTimeLimitMinigameBinding
    private lateinit var timerText: TextView
    private lateinit var increaseTimeBtn: ImageButton
    private lateinit var decreaseTimeBtn: ImageButton
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 60000 // Inicia com 1 minuto
    private var isGameWon = false // Variável que indica se o jogador acertou a palavra

    private lateinit var gameController: GameController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla os componentes da interface
        timeLimitMinigameBinding = ActivityTimeLimitMinigameBinding.inflate(layoutInflater)
        setContentView(timeLimitMinigameBinding.root)

        // Referencia os componentes
        timerText = findViewById(R.id.time_display)
        increaseTimeBtn = findViewById(R.id.increase_time_btn)
        decreaseTimeBtn = findViewById(R.id.decrease_time_btn)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Criar instância do controller
        gameController = GameController(this)

        registerButtonEvents(gameController)

        startTimer()
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

                // Se o jogo foi ganho, para o timer
                if (isGameWon) {
                    cancelTimer()
                }
            }

            override fun onFinish() {
                timerText.text = getString(R.string._00_00)
                // TODO: adicionar a lógica para interromper o jogo quando o timer chegar a 00:00
                if (!isGameWon) {
                    // Finaliza o jogo se o tempo acabar e o jogador não tiver ganhado
                }
            }
        }.start()
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
    }

    // Aumenta ou diminui o tempo
    private fun changeTime(minutesToAdd: Double) {
        timeLeftInMillis += (minutesToAdd * 60000).toLong() // Cada minuto é 60000 milissegundos
        if (timeLeftInMillis < 0) {
            timeLeftInMillis = 0 // Não permite valores negativos
        }
        startTimer() // Reinicia o timer com o novo tempo
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(gameController: GameController){

        // Volta à tela de minijogos
        timeLimitMinigameBinding.backToHomeTimeLimitBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Move para a activity do perfil do jogador
        timeLimitMinigameBinding.profileTimeLimiteBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        // Mostrar regras do jogo em um pop up customizado
        timeLimitMinigameBinding.ruleTimeLimiteBtn.setOnClickListener {
            gameController.showPopup(R.layout.time_rules)
        }

        // Aumentar tempo em 30 segundos
        increaseTimeBtn.setOnClickListener {
            changeTime(0.5)
        }

        // Diminuir tempo em 30 segundos
        decreaseTimeBtn.setOnClickListener {
            changeTime(-0.5)
        }
    }

    // Cancelar o timer se a activity for destruída antes de terminar
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
