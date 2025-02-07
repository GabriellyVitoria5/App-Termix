package com.ifmg.termix

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.GridLayout
import androidx.core.content.ContextCompat

class LettersGrid(private val context: Context, private val gridLayout: GridLayout) {

    private val rows = 6
    private val cols = 5
    val editTextList = mutableListOf<MutableList<EditText>>()
    var currentRow = 0
    var selectedColumn: Int = 0

    // Criar o layout da grade de letras na activity, cada letra será um campo de texto
    fun createGrid() {
        for (row in 0 until rows) {
            val rowList = mutableListOf<EditText>()
            for (col in 0 until cols) {

                // Criar o EditText
                val editText = EditText(context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 148
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(4, 4, 4, 4)
                    }
                    gravity = Gravity.CENTER
                    textSize = 24f
                    maxLines = 1
                    filters = arrayOf(InputFilter.LengthFilter(1))
                    background = ContextCompat.getDrawable(context, R.drawable.background_edit_text_letter_grid)
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    showSoftInputOnFocus = false // Bloquear que o teclado do celular do usuário apareça
                    isFocusable = false
                    isFocusableInTouchMode = false
                    isCursorVisible = false

                    // Evento de clique para selecionar o índice do EditText clicado
                    setOnClickListener {
                        if (row == currentRow) { // Permite seleção apenas na linha atual
                            // Restaurar fundo dos outros campos e atualizar coluna
                            editTextList[currentRow].forEach { it.setBackgroundResource(R.drawable.background_edit_text_letter_grid) }
                            selectedColumn = col

                            // Aplicar destaque visual apenas no editText selecionado
                            setBackgroundResource(R.drawable.background_edit_text_selected)
                        }
                    }
                }
                rowList.add(editText)
                gridLayout.addView(editText)
            }
            editTextList.add(rowList)
        }
        addSelectionInFirstColumn()
    }

    // Verificar se jogador acertou a palavra
    fun confirmWord(correctWord: String): Boolean {
        val userWord = editTextList[currentRow].joinToString("") { it.text.toString().uppercase() }
        if (userWord.length < cols) return false // Usuário precisa informar todas as letras

        colorLetters(userWord, correctWord)

        // Usuário acertou a palavra
        if (userWord == correctWord) return true

        // Se usuário errar a palavra, bloquear a linha atual, ir para a próxima e atualizar o foco no primeiro campo da linha
        currentRow++
        if (currentRow < rows){
            clearSelection()
            blockRow()
            addSelectionInFirstColumn()
        }

        return false
    }

    // Colorir as letras da palavra informada pelo usuário conforme a regra:
    // Verde - posição correta; Amarela - Existe, mas não nessa posição; Cinza - não existe na palavra
    private fun colorLetters(userWord: String, correctWord: String) {
        for (i in 0 until cols) {
            val letter = userWord[i]
            val editText = editTextList[currentRow][i]

            when {
                letter == correctWord[i] -> editText.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
                letter in correctWord -> editText.backgroundTintList = ContextCompat.getColorStateList(context, R.color.yellow)
                else -> editText.backgroundTintList = ContextCompat.getColorStateList(context, R.color.gray)
            }
        }
    }

    // Adicionar um fundo de seleção na primeira coluna da linha atual
    private fun addSelectionInFirstColumn() {
        selectedColumn = 0
        editTextList[currentRow][selectedColumn].setBackgroundResource(R.drawable.background_edit_text_selected)
    }

    // Limpar o fundo de seleção dos campos
    fun clearSelection(){
        for (i in 0 until rows) {
            for (editText in editTextList[i]) {
                editText.isEnabled = (i == currentRow)
                editText.setBackgroundResource(R.drawable.background_edit_text_letter_grid)
            }
        }
    }

    // Bloquear todos os campos de uma linha
    fun blockRow(){
        for (i in 0 until rows) {
            for (editText in editTextList[i]) {
                editText.isEnabled = (i == currentRow)
            }
        }
    }

}