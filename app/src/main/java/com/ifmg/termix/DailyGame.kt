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

        registerButtonEvents()
        createLettersGrid()
    }

    // Criar a grade com as letras: linhas são as palavras que o usuário vai inserir, colunas são as tentativas usadas para acertar a palavra
    private fun createLettersGrid(){
        letterGrid = LettersGrid(this, dailyGameBinding.lettersGrid)
        letterGrid.createGrid()
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

        // Voltar à tela inicial
        dailyGameBinding.backToHomeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        dailyGameBinding.profileBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        dailyGameBinding.sendBtn.setOnClickListener {
            val isCorrect = letterGrid.confirmWord(correctWord)

            if (isCorrect) {
                Toast.makeText(this, "Acertou!", Toast.LENGTH_LONG).show()
            } else if (letterGrid.currentRow == 6) {
                Toast.makeText(this, "As tentativas acabaram! A palavra era $correctWord", Toast.LENGTH_LONG).show()
            }
        }
    }
}