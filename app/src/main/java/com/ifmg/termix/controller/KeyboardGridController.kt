package com.ifmg.termix.controller

import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.ifmg.termix.R
import com.ifmg.termix.model.GameSession
import com.ifmg.termix.model.KeyboardGrid
import com.ifmg.termix.repository.PlayerWordsRepository

class KeyboardGridController(
    private val context: Context,
    private val gridLayout: GridLayout,
    private val onLetterPressed: (String) -> Unit,
    private val onDeletePressed: () -> Unit,
    private val onEnterPressed: () -> Unit
) {
    private val keyboardGridModel = KeyboardGrid()
    private val playerWordsRepository = PlayerWordsRepository(context)

    // Criar o layout da grade do teclado na activity, cada letra será um botão do teclado
    fun createKeyboard() {
        gridLayout.rowCount = 4
        gridLayout.columnCount = 10

        // Adiciona as letras
        for ((rowIndex, row) in keyboardGridModel.letters.withIndex()) {
            for ((colIndex, letter) in row.withIndex()) {
                addButton(letter.toString(), rowIndex, colIndex)
            }
        }

        // Botões especiais de apagar e enviar a palavra informada
        addButton("⌫", 2, 8, 2)
        keyboardGridModel.enterButton = addButton("ENVIAR", 3, 0, 10)
    }

    // Adicionar os botões no teclado
    private fun addButton(text: String, row: Int, col: Int, colSpan: Int = 1): Button {

        // Criar o botão
        val button = Button(context).apply {
            this.text = text
            textSize = 18f
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.background_button_keyboard)
            setOnClickListener { handleKeyPress(text) }
        }

        // Parâmetros de layout do botão
        val params = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(row, 1f)
            columnSpec = GridLayout.spec(col, colSpan, 2f)
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            setMargins(4, 8, 4, 8)
        }

        button.layoutParams = params
        gridLayout.addView(button)

        keyboardGridModel.letterButtonsMap[text] = button

        return button
    }

    // Tratar os eventos dos botões
    private fun handleKeyPress(key: String) {
        when (key) {
            "⌫" -> onDeletePressed()
            "ENVIAR" -> onEnterPressed()
            else -> onLetterPressed(key)
        }
    }

    // Atualiza as cores dos botões do teclado com base na tentativa do usuário
    fun updateKeyboardColors(guess: String, correctWord: String) {
        val correctChars = correctWord.toCharArray()
        val guessedChars = guess.toCharArray()

        for (i in guessedChars.indices) {
            val letter = guessedChars[i].toString()
            val button = keyboardGridModel.letterButtonsMap[letter]

            // Só altera a cor se o botão não for de uma letra correta, ação necessária para não alterar a cor do botão no teclado de uma letra já encontrada na posição certa em tentativas anteriores
            if (button != null && !keyboardGridModel.correctLetters.contains(letter)) {
                when {
                    guessedChars[i] == correctChars[i] -> { // Letra está na palavra e na posição certa
                        button.backgroundTintList = ContextCompat.getColorStateList(context,
                            R.color.green
                        )
                        keyboardGridModel.correctLetters.add(letter) // Trava essa letra para não mudar a cor e o usuário saber que encontrou a posição dela
                    }
                    correctWord.contains(letter) -> { // Letra está na palavra, mas em outra posição
                        button.backgroundTintList = ContextCompat.getColorStateList(context,
                            R.color.yellow
                        )
                    }
                    else -> { // Letra não está na palavra
                        button.backgroundTintList = ContextCompat.getColorStateList(context,
                            R.color.gray
                        )
                    }
                }
            }
        }
    }

    // Bloquear todas as telcas do teclado criado
    fun disableKeyboard() {
        for (button in keyboardGridModel.letterButtonsMap.values) {
            button.isEnabled = false
        }
    }

    // Desloquear todas as telcas do teclado criado
    fun enableKeyboard() {
        for (button in keyboardGridModel.letterButtonsMap.values) {
            button.isEnabled = true
        }
    }

    // Bloquear e habilitar botão de enviar do teclado criado
    fun setEnterButtonEnabled(enabled: Boolean) {
        keyboardGridModel.enterButton?.isEnabled = enabled
        keyboardGridModel.enterButton?.alpha = if (enabled) 1.0f else 0.5f
    }

    // Limpar as informações da grade de letras, limpando as tentativas e cores iniciar um novo jogo
    fun clearKeyboardColors() {
        keyboardGridModel.correctLetters.clear()
        for (button in keyboardGridModel.letterButtonsMap.values) {
            button.backgroundTintList = null
        }
    }

    // Restaura o estado do teclado com base nas palavras digitadas nas tentativas
    fun restoreKeyboardState(gameSession: GameSession) {
        // Recuperar as tentativas já feitas
        val playerWords = playerWordsRepository.getPlayerWordsForGameSession(gameSession.id)

        // Para cada tentativa, atualiza as cores do teclado
        for (playerWord in playerWords) {
            updateKeyboardColors(playerWord.word, gameSession.word)
        }
    }

}
