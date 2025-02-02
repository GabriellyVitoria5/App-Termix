package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityDailyGameBinding

class DailyGame : AppCompatActivity() {

    private lateinit var dailyGameBinding: ActivityDailyGameBinding

    private lateinit var letterGrid: LettersGrid
    private lateinit var keyboardGrid: KeyboardGrid

    private val correctWord = "CARRO" // Palavra correta padrão para teste

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        dailyGameBinding = ActivityDailyGameBinding.inflate(layoutInflater)
        setContentView(dailyGameBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Registrar eventos
        registerButtonEvents()
        createLettersGrid()
        createKeyBoardGrid()
    }

    // Criar a grade com as letras: linhas são as palavras que o usuário vai inserir, colunas são as tentativas usadas para acertar a palavra
    private fun createLettersGrid(){
        letterGrid = LettersGrid(this, dailyGameBinding.lettersGridDaily)
        letterGrid.createGrid()
    }

    // Criar a grade com o teclado: cada letra do alfabeto é um botão, mais os botões de enviar e apagar. Os botões já recebem ações que devem ser feitas ao serem clicados
    private fun createKeyBoardGrid() {
        keyboardGrid = KeyboardGrid(
            context = this,
            gridLayout = dailyGameBinding.keyboardGridDaily,
            onLetterPressed = { letter -> insertLetter(letter) },
            onDeletePressed = { deleteLetter() },
            onEnterPressed = { submitWord() }
        )
        keyboardGrid.createKeyboard()
    }

    // TODO Criar classe intermediária para adicionar os eventos dos botões para não ficar na classe da activity
    // Inserir uma letra na grade com os EditText
    private fun insertLetter(letter: String) {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        for (editText in currentRowList) {
            if (editText.text.isEmpty()) {
                editText.setText(letter)
                break
            }
        }
    }

    // Apagar a última letra
    private fun deleteLetter() {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        for (i in currentRowList.indices.reversed()) {
            if (currentRowList[i].text.isNotEmpty()) {
                currentRowList[i].text.clear()
                break
            }
        }
    }

    // Enviar a palavra que o usuário informou nos campos
    private fun submitWord() {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        val guess = currentRowList.joinToString("") { it.text.toString() }

        if (guess.length < correctWord.length) {
            Toast.makeText(this, "Preencha todas as letras!", Toast.LENGTH_SHORT).show()
            return
        }

        val isCorrect = letterGrid.confirmWord(correctWord)
        keyboardGrid.updateKeyboardColors(guess, correctWord)

        if (isCorrect) {
            Toast.makeText(this, "Acertou!", Toast.LENGTH_LONG).show()
        } else if (letterGrid.currentRow == 6) {
            dailyGameBinding.answerTxt.text = "A resposta certa era: $correctWord"
        }
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

        // Voltar à tela inicial
        dailyGameBinding.backToHomeDailyBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        dailyGameBinding.profileDailyBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}