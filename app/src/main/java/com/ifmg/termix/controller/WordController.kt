package com.ifmg.termix.controller

import android.content.Context
import android.widget.Toast
import com.ifmg.termix.model.Word
import com.ifmg.termix.repository.WordRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class WordController(var context: Context) {

    lateinit var wordRepository: WordRepository
    private val wordsFilePath = "app/src/main/java/com/termix/localdata/WordsDatabase.txt"

    init {
        wordRepository = WordRepository(context)
    }

    // Buscar palavras do arquivo local
    private fun getWordsFromFile(): List<String> {
        val wordsList = mutableListOf<String>()
        val file = File(wordsFilePath)

        if (file.exists()) {
            try {
                BufferedReader(FileReader(file)).useLines { lines -> wordsList.addAll(lines) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            println("Arquivo WordsDatabase não encontrado em $wordsFilePath")
        }

        return wordsList
    }

    // Carregar palavras do arquivo local
    fun loadWordsFromFile() {
        val words = getWordsFromFile()
        for (word in words) {
            addWord(word)
        }
    }

    // Inserir palavras do jogo no banco
    fun addWord(word: String) {
        wordRepository.insertWord(Word(word = word))
    }

    // Carregar todas as palavras no banco de dados quando iniciar a aplicação pela primeira vez
    fun loadWordsIfFirstRun() {
        val sharedPreferences = context.getSharedPreferences("TermixPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("firstRun", true)

        if (isFirstRun) {
            loadWordsFromFile()
            println("Funcionou")

            // Atualiza o SharedPreferences para não rodar novamente
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }
    }

    // Buscar todas as palavras
    fun loadWords(): List<Word> {
        return wordRepository.getAllWords()
    }

    // Sortear e mostrar uma palavra aramazenada no banco
    fun showRandomWord() {
        val word = wordRepository.getRandomWord()

        if (word != null) {
            Toast.makeText(context, "Palavra sorteada: $word", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Nenhuma palavra encontrada no banco!", Toast.LENGTH_SHORT).show()
        }
    }

}