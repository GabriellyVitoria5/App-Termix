package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.controller.GameController
import com.ifmg.termix.controller.KeyboardGridController
import com.ifmg.termix.databinding.ActivityTermixTurboMinigameBinding
import com.ifmg.termix.model.LettersGrid
import java.util.Locale

class TermixTurboMinigame : AppCompatActivity() {

    private lateinit var termixTurboMinigameBinding: ActivityTermixTurboMinigameBinding
    private lateinit var timerText: TextView
    private lateinit var increaseTimeBtn: ImageButton
    private lateinit var decreaseTimeBtn: ImageButton
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 60000 // Inicia com 1 minuto

    private lateinit var gameController: GameController

    private lateinit var letterGrid: LettersGrid
    private lateinit var keyboardGridController: KeyboardGridController

    private lateinit var correctWord: String
    private val gameMode = "turbo"

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
        
        // Criar instância do controller
        gameController = GameController(this)

        // Registrar eventos
        registerButtonEvents(gameController)
        createLettersGrid()
        createKeyBoardGrid()

        startTimer()
    }

    // Criar a grade com as letras: linhas são as palavras que o usuário vai inserir, colunas são as tentativas usadas para acertar a palavra
    private fun createLettersGrid() {
        letterGrid = LettersGrid(this, termixTurboMinigameBinding.lettersGridTurbo)
        letterGrid.createGrid()
    }

    // Criar a grade com o teclado: cada letra do alfabeto é um botão, mais os botões de enviar e apagar. Os botões já recebem ações que devem ser feitas ao serem clicados
    private fun createKeyBoardGrid() {
        keyboardGridController = KeyboardGridController(
            context = this,
            gridLayout = termixTurboMinigameBinding.keyboardGridTurbo,
            onLetterPressed = { letter -> insertLetter(letter) },
            onDeletePressed = { deleteLetter() },
            onEnterPressed = { submitWord() }
        )
        keyboardGridController.createKeyboard()
    }

    // Inserir uma letra na grade com os EditText
    private fun insertLetter(letter: String) {
        if (letterGrid.currentRow >= letterGrid.rows) {
            return
        }

        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        currentRowList[letterGrid.selectedColumn].setText(letter)

        // Restaurar fundo do campo anterior
        currentRowList[letterGrid.selectedColumn].setBackgroundResource(R.drawable.background_edit_text_letter_grid)

        // Mover para o próximo campo (se houver)
        if (letterGrid.selectedColumn < currentRowList.size - 1) {
            letterGrid.selectedColumn++
        }

        // Destacar o novo campo selecionado
        currentRowList[letterGrid.selectedColumn].setBackgroundResource(R.drawable.background_edit_text_selected)

    }

    // Apagar uma letra
    private fun deleteLetter() {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        val selectedIndex = letterGrid.selectedColumn

        // Se o campo tiver uma letra, ela será apagada, o foco só será alterado para o campo anterior se o atual estiver vazio
        if (currentRowList[selectedIndex].text.isNotEmpty()) {
            currentRowList[selectedIndex].text.clear()
        } else if (selectedIndex > 0) {
            letterGrid.selectedColumn--
            currentRowList[letterGrid.selectedColumn].text.clear()
        }

        // Restaurar fundo dos campos antes de destacar o atual
        currentRowList.forEach { it.setBackgroundResource(R.drawable.background_edit_text_letter_grid) }
        currentRowList[letterGrid.selectedColumn].setBackgroundResource(R.drawable.background_edit_text_selected)
    }

    // Enviar a palavra que o usuário informou nos campos
    private fun submitWord() {
        // Criar regra específica para esse minigame
        Toast.makeText(this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show()
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

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(gameController: GameController){

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

        // Mostrar regras do jogo em um pop up customizado
        termixTurboMinigameBinding.ruleTurboBtn.setOnClickListener {
            gameController.showPopup(R.layout.turbo_rules)
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
