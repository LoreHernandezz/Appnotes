package hernandez.lorena.appnotes

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotesByCategoryActivity : AppCompatActivity() {

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var allNotes: MutableList<Note>
    private lateinit var db: NotesDatabaseHelper
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_by_category)

        val btnCancel: ImageView = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        db = NotesDatabaseHelper(this)

        // Recupera la categoría seleccionada desde el Intent
        selectedCategory = intent.getStringExtra("category")

        // Obtén todas las notas de la base de datos
        allNotes = db.getAllNotes().toMutableList()

        // Filtra las notas por la categoría seleccionada
        val notesInCategory = allNotes.filter { it.category == selectedCategory }.toMutableList()

        // Configura el RecyclerView
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this, notesInCategory)
        notesRecyclerView.adapter = notesAdapter

        // Muestra el título de la categoría seleccionada
        val categoryTitle: TextView = findViewById(R.id.categoryTitle)
        categoryTitle.text = "Notas en la categoría: $selectedCategory"
    }
}
