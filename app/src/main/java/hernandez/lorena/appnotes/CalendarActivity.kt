package hernandez.lorena.appnotes

import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var db: NotesDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)

        val btnBack = findViewById<ImageView>(R.id.btnBackC)
        btnBack.setOnClickListener {
            finish()
        }

        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.notesRecyclerView)
        db = NotesDatabaseHelper(this)

        notesAdapter = NotesAdapter(this, mutableListOf())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        val currentDate = getDateString(calendarView.date)
        mostrarNotas(currentDate)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            mostrarNotas(selectedDate)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        val currentDate = getDateString(calendarView.date)

        db = NotesDatabaseHelper(this)
        val todasLasNotas = db.getAllNotes()
        val notasDelDia = todasLasNotas.filter { it.date == currentDate }

        Log.d("Notas", "Notas actualizadas al abrir el calendario: $notasDelDia")

        notesAdapter.updateNotes(notasDelDia)
    }

    private fun getDateString(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timeInMillis))
    }

    private fun mostrarNotas(fecha: String) {
        val todasLasNotas = db.getAllNotes()
        Log.d("Notas", "Fecha recibida para filtrar: $fecha")
        Log.d("Notas", "Todas las notas antes de filtrar: $todasLasNotas")

        val notasDelDia = todasLasNotas.filter {
            Log.d("Notas", "Comparando '${it.date}' con '$fecha'")
            it.date?.trim() == fecha.trim()
        }


        Log.d("Notas", "Notas filtradas con fecha '$fecha': $notasDelDia")

        notesAdapter.updateNotes(notasDelDia)
    }
}
