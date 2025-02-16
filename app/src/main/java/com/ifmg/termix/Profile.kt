package com.ifmg.termix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ifmg.termix.adapter.CalendarAdapter
import com.ifmg.termix.databinding.ActivityProfileBinding
import com.ifmg.termix.utils.CalendarUtils
import java.util.*

class Profile : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        registerButtonEvents()
        setupCalendar()
        setupStats()
    }

    private fun registerButtonEvents() {
        profileBinding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupCalendar() {
        val currentCalendar = Calendar.getInstance()
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)

        // Atualiza o cabeçalho do calendário
        val monthName = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        profileBinding.monthTextView.text = "$monthName $currentYear"

        // TODO: Buscar dados do banco sobre partidas jogadas e vitórias
        val days = CalendarUtils.generateMonthData(currentYear, currentMonth)

        profileBinding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        profileBinding.calendarRecyclerView.adapter = CalendarAdapter(days)
    }

    private fun setupStats() {
        // TODO: Substituir por valores reais do banco de dados
        val fakeGamesPerMode = 15
        val fakeWinStreak = 5
        val fakeTotalVictories = 42
        val fakeTotalLosses = 12

        profileBinding.gamesPerMode.text = "$fakeGamesPerMode"
        profileBinding.winStreak.text = "$fakeWinStreak"
        profileBinding.victories.text = "$fakeTotalVictories"
        profileBinding.losses.text = "$fakeTotalLosses"
    }
}
