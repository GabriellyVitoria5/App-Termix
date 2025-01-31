package com.ifmg.termix

import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.GridLayout
import androidx.core.content.ContextCompat

class LettersGrid(private val context: Context, private val gridLayout: GridLayout) {

    private val rows = 6
    private val cols = 5
    private val editTextList = mutableListOf<MutableList<EditText>>()
    var currentRow = 0

    // Criar o layout da grade de letras na activity, cada letra será um campo de texto
    fun createGrid() {
        for (row in 0 until rows) {
            val rowList = mutableListOf<EditText>()
            for (col in 0 until cols) {
                val editText = EditText(context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 148 // TODO Verificar tamanho da  grade
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(8, 16, 8, 4)
                    }
                    gravity = Gravity.CENTER
                    textSize = 24f
                    maxLines = 1
                    filters = arrayOf(InputFilter.LengthFilter(1))
                    background = ContextCompat.getDrawable(context, R.drawable.background_edit_text_letter_grid)
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                }
                rowList.add(editText)
                gridLayout.addView(editText)
            }
            editTextList.add(rowList)
        }
    }

    // Bloquear e liberar linhas conforme regra do jogo: somente a linha da tentativa atual pode ser alterada, as demais devem ser bloqueadas
    private fun updateRowState() {
        for (i in 0 until rows) {
            for (editText in editTextList[i]) {
                editText.isEnabled = (i == currentRow)
            }
        }
    }

    // Verificar se jogador acertou a palavra
    fun confirmWord(correctWord: String): Boolean {
        val userWord = editTextList[currentRow].joinToString("") { it.text.toString().uppercase() }

        if (userWord.length < cols) return false // Usuário precisa informar todas as letras

        checkLetters(userWord, correctWord)

        // Acertou
        if (userWord == correctWord) return true

        // Errou, ir para a próxima linha
        currentRow++
        if (currentRow < rows) updateRowState()

        return false
    }

    // Colorir as letras da palavra informada pelo usuário conforme a regra:
    // Verde - posição correta; Amarela - Existe, mas não nessa posição; Cinza - não existe na palavra
    private fun checkLetters(userWord: String, correctWord: String) {
        for (i in 0 until cols) {
            val letter = userWord[i]
            val editText = editTextList[currentRow][i]

            when {
                letter == correctWord[i] -> editText.setBackgroundColor(Color.GREEN)
                letter in correctWord -> editText.setBackgroundColor(Color.YELLOW)
                else -> editText.setBackgroundColor(Color.GRAY)
            }
        }
    }

}