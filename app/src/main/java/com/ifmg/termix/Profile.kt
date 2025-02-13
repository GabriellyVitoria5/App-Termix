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
    }

    private fun registerButtonEvents() {
        profileBinding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupCalendar() {
        // Pega o mês e o ano atual
        val currentCalendar = Calendar.getInstance()
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)

        // Atualiza o cabeçalho para mostrar o mês e o ano
        val monthName = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        profileBinding.monthTextView.text = "$monthName $currentYear"

        // Passa o mês atual para a função de gerar o calendário
        val days = CalendarUtils.generateMonthData(currentYear, currentMonth)
        profileBinding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        profileBinding.calendarRecyclerView.adapter = CalendarAdapter(days)
    }
}
