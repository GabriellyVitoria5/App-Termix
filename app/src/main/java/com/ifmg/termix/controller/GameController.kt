package com.ifmg.termix.controller

import android.content.Context
import android.widget.Toast
import com.ifmg.termix.model.GameSession
import com.ifmg.termix.model.PlayerWords
import com.ifmg.termix.repository.GameSessionRepository
import com.ifmg.termix.repository.PlayerWordsRepository
import com.ifmg.termix.repository.WordRepository
import kotlin.random.Random

// Controlar fluxo e ações do jogo principal e dos minigames
class GameController(var context: Context) {

    private val wordRepository = WordRepository(context)
    private val playerWordsRepository = PlayerWordsRepository(context)
    private val gameSessionRepository = GameSessionRepository(context)

    private val activeGameSessions = mutableMapOf<String, Int>() // Maperar o modo de jogo e id da partida>

    // Criar uma nova partida de um minijogo ou jogo diário
    fun startNewGameSession(mode: String) {
        // Verificar se já existe uma partida em andamento
        if (gameSessionRepository.isGameInProgress()) {
            Toast.makeText(context, "Já existe uma partida em andamento!", Toast.LENGTH_SHORT).show()
            return
        }

        val chosenWord = getRandomWord()

        if (!validateWord(chosenWord)) {
            Toast.makeText(context, "Erro ao sortear palavra!", Toast.LENGTH_SHORT).show()
            return
        }

        val newGameSession = GameSession(
            id = 0,
            gameDate = System.currentTimeMillis().toString(),
            mode = mode,
            word = chosenWord,
            attempt = 0,
            status = "andamento"
        )

        val gameId = gameSessionRepository.insertGameSession(newGameSession)

        if (gameId != -1L) {
            activeGameSessions[mode] = gameId.toInt()
            Toast.makeText(context, "Nova partida ($mode) iniciada!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Erro ao iniciar partida ($mode)!", Toast.LENGTH_SHORT).show()
        }
    }

    // Sortear uma palavra aleatória do banco
    fun getRandomWord(): String{
        val words = wordRepository.getAllWords()
        if (words.isNotEmpty()) {
            return words[Random.nextInt(words.size)].word.uppercase()
        }
        return ""
    }

    // Validar se a palavra sorteada tem o tamanho correto (e foi encontrada no banco corretamente)
    fun validateWord(chosenWord: String): Boolean{
        return chosenWord.length == 5
    }

    // Verificar se a palavra informada pelo usuário está no banco de palavras do jogo
    fun isWordInLocalDatabase(userWord: String): Boolean {
        val words = wordRepository.getAllWords().map { it.word.uppercase() }
        return userWord.uppercase() in words
    }

    // Recuperar uma partida ativa de um modo específico
    fun getActiveGameId(mode: String): Int? {
        return activeGameSessions[mode]
    }

    // Salvar a palavra digitada pelo usuário no banco e relacionar essas palavras com uma partida já iniciada
    fun savePlayerWord(gameMode: String, word: String, attempt: Int) {
        val gameId = getActiveGameId(gameMode)

        if (gameId == null) {
            Toast.makeText(context, "Nenhuma partida ativa para o modo: $gameMode!", Toast.LENGTH_SHORT).show()
            return
        }

        val playerWord = PlayerWords(
            id = 0,
            gameId = gameId,
            word = word,
            attempt = attempt,
            gameMode = gameMode
        )

        val resultado = playerWordsRepository.insertPlayerWord(playerWord)
        Toast.makeText(context, "Palavra salva ($gameMode)! ID: $resultado", Toast.LENGTH_SHORT).show()
    }

    // Encerrar uma partida específica e atualizar status no banco
    fun endGameSession(mode: String, win: Boolean) {
        val gameId = getActiveGameId(mode) ?: return

        val newStatus = if (win) "vitoria" else "derrota"
        gameSessionRepository.updateGameStatus(gameId, newStatus)

        Toast.makeText(context, "Partida ($mode) finalizada: $newStatus", Toast.LENGTH_SHORT).show()
        activeGameSessions.remove(mode) // Remover a partida finalizada
    }

    // Recuperar a partida de jogo ativa para um modo de jogo específico
    fun getCurrentGameSession(mode: String): GameSession? {
        val gameId = getActiveGameId(mode) // Obter o id da partida ativa para o modo
        return if (gameId != null) {
            gameSessionRepository.getGameSessionById(gameId) // Buscar a partida ativa no repositório
        } else {
            null // Retorna null se não houver partida ativa para o modo
        }
    }

    // Recuperar a palavra digitada pelo jogador em uma linha específica
    fun getPlayerWord(gameMode: String, attempt: Int): String? {
        val gameId = getActiveGameId(gameMode) // Obter o id da partida ativa para o modo

        if (gameId == null) {
            Toast.makeText(context, "Nenhuma partida ativa para o modo: $gameMode!", Toast.LENGTH_SHORT).show()
            return null
        }

        // Buscar as palavras digitadas pelo jogador para a partida ativa e modo específico
        val playerWords = playerWordsRepository.getPlayerWordsByGameId(gameId)

        // Buscar a palavra para a linha (tentativa) específica
        val playerWord = playerWords.find { it.attempt == attempt }

        return playerWord?.word // Retorna a palavra ou null se não encontrar
    }





}