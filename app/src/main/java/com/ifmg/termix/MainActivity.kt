package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.controller.WordController
import com.ifmg.termix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Popula o banco na primeira execução
        val gameController = WordController(this)
        gameController.loadWordsIfFirstRun()
        gameController.showRandomWord()

        // Inflar os componentes da interface
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerButtonEvents()

        /*val keyboardLayout = findViewById<GridLayout>(R.id.keyboard)
        val letters = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray()

        for (letter in letters) {
            val button = Button(this).apply {
                text = letter.toString()
                textSize = 16f
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED)
                    setMargins(8, 8, 8, 8)
                }
            }

            button.setOnClickListener {
                // Aqui você pode tratar os cliques no botão
                Toast.makeText(this, "Letra clicada: $letter", Toast.LENGTH_SHORT).show()
            }

            keyboardLayout.addView(button)

            button.background = ContextCompat.getDrawable(this, R.drawable.background_button_keyboard)
        }



        val enterButton = Button(this).apply {
            text = "ENTER"
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2f)
                setMargins(8, 8, 8, 8)
            }
        }

        val backspaceButton = Button(this).apply {
            text = "←"
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2f)
                setMargins(8, 8, 8, 8)
            }
        }
        keyboardLayout.addView(enterButton)
        keyboardLayout.addView(backspaceButton)*/

    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

        // Mover para a activity do jogo diário
        mainBinding.playBtn.setOnClickListener {
            val intent = Intent(this, DailyGame::class.java)
            startActivity(intent)
        }

        // Mover para a activity do jogo diário
        mainBinding.playMinigameBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        mainBinding.profileBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}