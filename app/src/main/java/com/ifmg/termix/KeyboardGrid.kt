package com.ifmg.termix

import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat

class KeyboardGrid(
    private val context: Context,
    private val gridLayout: GridLayout,
    private val onLetterPressed: (String) -> Unit,
    private val onDeletePressed: () -> Unit,
    private val onEnterPressed: () -> Unit
) {

    // Letras do teclado
    private val letters = listOf(
        "QWERTYUIOP",
        "ASDFGHJKLÇ",
        "ZXCVBNM"
    )

    private val buttonsMap = mutableMapOf<String, Button>() // Mapeia as letras aos botões
    private val correctLetters = mutableSetOf<String>() // Letras que já foram confirmadas como verdes

    // Criar o layout da grade do teclado na activity, cada letra será um botão do teclado
    fun createKeyboard() {
        gridLayout.rowCount = 4
        gridLayout.columnCount = 10

        // Adiciona as letras
        for ((rowIndex, row) in letters.withIndex()) {
            for ((colIndex, letter) in row.withIndex()) {
                addButton(letter.toString(), rowIndex, colIndex)
            }
        }

        // Botões especiais de apagar e enviar a palavra informada
        addButton("⌫", 2, 8, 2)
        addButton("ENVIAR", 3, 0, 10)
    }

    // Adicionar os botões no teclado
    private fun addButton(text: String, row: Int, col: Int, colSpan: Int = 1) {

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
            columnSpec = GridLayout.spec(col, colSpan, 1f)
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            setMargins(4, 8, 4, 8)
        }

        button.layoutParams = params
        gridLayout.addView(button)

        // Adiciona ao mapa apenas se for uma letra
        if (text.length == 1) {
            buttonsMap[text] = button
        }
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
            val button = buttonsMap[letter]

            // Só altera a cor se o botão não for de uma letra correta, ação necessária para não alterar a cor do botão no teclado de uma letra já encontrada na posição certa em tentativas anteriores
            if (button != null && !correctLetters.contains(letter)) {
                when {
                    guessedChars[i] == correctChars[i] -> { // Letra está na palavra e na posição certa
                        button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
                        correctLetters.add(letter) // Trava essa letra para não mudar a cor e o usuário saber que encontrou a posição dela
                    }
                    correctWord.contains(letter) -> { // Letra está na palavra, mas em outra posição
                        button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.yellow)
                    }
                    else -> { // Letra não está na palavra errada, bloquear o botão dela no teclado
                        button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.gray)
                        button.isEnabled = false
                    }
                }
            }
        }
    }
}
