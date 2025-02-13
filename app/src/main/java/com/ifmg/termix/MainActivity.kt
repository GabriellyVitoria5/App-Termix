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

        // Inflar os componentes da interface
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadWordsToDatabase()
        registerButtonEvents()
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

    // Carregar as palavras do jogo no banco ao baixar o aplicativo, nas próximas vezes que ele for aberto, os dados não serão carregados
    private fun loadWordsToDatabase(){
        val gameController = WordController(this)
        gameController.loadWordsIfFirstRun()
    }
}