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
    // TODO IndexOutOfBoundsException: Index 6 out of bounds for length 6
    // Inserir uma letra na grade com os EditText
    private fun insertLetter(letter: String) {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]

        if (letterGrid.selectedColumn < currentRowList.size) {
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

    // TODO: Criar classe intermediária que cuida do fluxo do jogo
    // Enviar a palavra que o usuário informou nos campos
    private fun submitWord() {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        val guess = currentRowList.joinToString("") { it.text.toString() }

        if (guess.length < correctWord.length) {
            Toast.makeText(this, "Preencha todas as letras!", Toast.LENGTH_SHORT).show()
            return
        }

        letterGrid.clearSelection()

        val isCorrect = letterGrid.confirmWord(correctWord)
        keyboardGrid.updateKeyboardColors(guess, correctWord)

        // Verificar a resposta e bloquear o botão para não permitir enviar mais palavras
        if (isCorrect) {
            Toast.makeText(this, "Acertou!", Toast.LENGTH_LONG).show()
            keyboardGrid.setEnterButtonEnabled(false)
        } else if (letterGrid.currentRow == 6) {
            dailyGameBinding.answerTxt.text = "A resposta certa era: $correctWord"
            keyboardGrid.setEnterButtonEnabled(false)
            letterGrid.blockRow()
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