package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.controller.GameController
import com.ifmg.termix.controller.KeyboardGridController
import com.ifmg.termix.databinding.ActivityDailyGameBinding
import com.ifmg.termix.model.LettersGrid

class DailyGame : AppCompatActivity() {

    private lateinit var dailyGameBinding: ActivityDailyGameBinding

    private lateinit var gameController: GameController

    private lateinit var letterGrid: LettersGrid
    private lateinit var keyboardGridController: KeyboardGridController

    private lateinit var correctWord: String
    private val gameMode = "diario"

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


        
        // Criar instância do controlador
        gameController = GameController(this)

        //Iniciar partida
        startNewGameSession()

        // Registrar eventos dos botões
        registerButtonEvents(gameController)
        createLettersGrid()
        createKeyBoardGrid()

        // Sortear a palavra do jogo
        correctWord = getWordGame()
    }

    // Iniciar uma nova partida se não houver uma partida em andamento
    private fun startNewGameSession() {
        val activeGameSession = gameController.getCurrentGameSession(gameMode)

        // Já existe uma partida em andamento, não precisa criar uma nova
        if (activeGameSession != null) {
            gameController.getActiveGameId(gameMode)
            return
        }

        // Inicia uma nova partida
        gameController.startNewGameSession(gameMode)
    }


    // Criar a grade com as letras: linhas são as palavras que o usuário vai inserir, colunas são as tentativas usadas para acertar a palavra
    private fun createLettersGrid(){
        letterGrid = LettersGrid(this, dailyGameBinding.lettersGridDaily)
        letterGrid.createGrid()
    }

    // Criar a grade com o teclado: cada letra do alfabeto é um botão, mais os botões de enviar e apagar. Os botões já recebem ações que devem ser feitas ao serem clicados
    private fun createKeyBoardGrid() {
        keyboardGridController = KeyboardGridController(
            context = this,
            gridLayout = dailyGameBinding.keyboardGridDaily,
            onLetterPressed = { letter -> insertLetter(letter) },
            onDeletePressed = { deleteLetter() },
            onEnterPressed = { submitWord() }
        )
        keyboardGridController.createKeyboard()
    }

    // TODO Criar classe intermediária para adicionar os eventos dos botões para não ficar na classe da activity
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

    // TODO: Criar classe intermediária que cuida do fluxo do jogo
    // Enviar a palavra que o usuário informou nos campos
    private fun submitWord() {
        val currentRowList = letterGrid.editTextList[letterGrid.currentRow]
        val guess = currentRowList.joinToString("") { it.text.toString() }

        if (guess.length < correctWord.length) {
            Toast.makeText(this, "Preencha todas as letras!", Toast.LENGTH_SHORT).show()
            return
        }

        // Usuário só pode informar palavras que estão no banco de dados local do jogo
        /*if (!gameController.isWordInLocalDatabase(guess)) {
            Toast.makeText(this, "Essa palavra não é aceita porque está na nossa lista", Toast.LENGTH_SHORT).show()
            return
        }*/

        // Salvar a palavra digitada
        gameController.savePlayerWord(gameMode, guess, letterGrid.currentRow)

        letterGrid.clearSelection()

        if (letterGrid.currentRow < 6) {
            val isCorrect = letterGrid.confirmWord(correctWord)
            keyboardGridController.updateKeyboardColors(guess, correctWord)

            // Verificar a resposta e bloquear o botão para não permitir enviar mais palavras
            if (isCorrect || letterGrid.currentRow > 6) { // TODO quando acerta na última ainda dá pra selecionar o campo
                dailyGameBinding.answerTxt.text = "Acertou, parabéns!"
                keyboardGridController.setEnterButtonEnabled(false)
                keyboardGridController.disableKeyboard() // TODO corrigir problema visual de ir pra próxima linha depois de ganhar sem bloquear o teclado

                // Atualize status do jogo como finalizado
                gameController.endGameSession(gameMode, true)

                // Jogar novamente ao clicar no botão
                dailyGameBinding.retryGameBtn.setVisibility(View.VISIBLE)
                dailyGameBinding.retryGameBtn.setOnClickListener {
                    resetGameUI()
                }
            } else if (letterGrid.currentRow == 6) {
                dailyGameBinding.answerTxt.text = "A resposta certa era: $correctWord"
                keyboardGridController.setEnterButtonEnabled(false)
                keyboardGridController.disableKeyboard() // TODO desbloquear teclado depois de resolver o TODO de cima
                letterGrid.blockRow()

                gameController.endGameSession(gameMode, false)

                // Jogar novamente ao clicar no botão
                dailyGameBinding.retryGameBtn.setVisibility(View.VISIBLE)
                dailyGameBinding.retryGameBtn.setOnClickListener {
                    resetGameUI()
                }
            }
        }
    }

    // Escolher e validar a palavra do jogo
    private fun getWordGame(): String{
        val dailyWord = gameController.getRandomWord()

        // Não foi possível escolher uma palavra para o jogo
        if(!gameController.validateWord(dailyWord)){
            keyboardGridController.disableKeyboard()
            Toast.makeText(this, "Erro ao buscar palavra! Não foi possível iniciar o jogo", Toast.LENGTH_LONG).show()
            return ""
        }

        return dailyWord
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(gameController: GameController){

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

        // Mostrar regras do jogo em um pop up customizado
        dailyGameBinding.ruleDailyBtn.setOnClickListener {
            gameController.showPopup(R.layout.daily_rules)
        }
    }

    private fun resetGameUI() {
        gameController.startNewGameSession(gameMode) // Criar uma nova partida ao resetar

        correctWord = gameController.getRandomWord() // Obter nova palavra da partida

        dailyGameBinding.retryGameBtn.setVisibility(View.INVISIBLE)

        // Limpar grade do teclado e de letras
        letterGrid.clearLetterGrid()
        keyboardGridController.clearKeyboardColors()

        keyboardGridController.setEnterButtonEnabled(true)
        keyboardGridController.enableKeyboard()

        dailyGameBinding.answerTxt.text = ""
    }

    // Voltar o estado de uma partida em andamento
    override fun onResume() {
        super.onResume()

        // Recuperar o estado da última partida do banco de dados (palavras inseridas pelo usuário e palavra escolhida)
        val gameSession = gameController.getCurrentGameSession(gameMode)

        // Verificar se existe uma partida em andamento
        if (gameSession != null) {
            val attemptCount = gameSession.attempt
            val previousCorrectWord = gameController.getCorrectWord(gameMode)
            correctWord = previousCorrectWord

            // Preencher a grade de letras com as palavras já informadas antes e deixar a
            for (row in 0 until attemptCount) {
                val guess = gameController.getPlayerWord(gameMode, row)
                letterGrid.currentRow = row

                if (guess != null) {
                    for (column in 0 until letterGrid.editTextList[row].size) {
                        letterGrid.editTextList[row][column].setText(guess[column].toString())
                        letterGrid.clearSelection() // Bloquear linha e tirar a selação
                    }

                    // Colorir as letras dessa linha
                    letterGrid.colorLetters(guess, correctWord)

                }
            }

            // Corrigir problema que não pulava uma linha ao recuperar uma partida
            if(attemptCount > 0){
                letterGrid.currentRow  += 1
            }

            // Colocar uma seleção no começo da linha em que o jogador parou
            letterGrid.blockRow()
            letterGrid.addSelectionInFirstColumn()

            // Restaurar o estado do teclado (cores dos botões com base nas respostas anteriores)
            keyboardGridController.restoreKeyboardState(gameSession)
        }
    }



}