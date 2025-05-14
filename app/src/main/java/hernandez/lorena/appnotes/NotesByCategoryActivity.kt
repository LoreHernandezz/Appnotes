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
    private var showFavoritesOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_by_category)

        val btnCancel: ImageView = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        db = NotesDatabaseHelper(this)

        showFavoritesOnly = intent.getBooleanExtra("favoritesOnly", false)
        allNotes = db.getAllNotes().toMutableList()

        val notesToDisplay = if (showFavoritesOnly) {
            allNotes.filter { it.isFavorite }.toMutableList()
        } else {
            selectedCategory = intent.getStringExtra("category")
            allNotes.filter { it.category == selectedCategory }.toMutableList()
        }

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this, notesToDisplay)
        notesRecyclerView.adapter = notesAdapter

        val categoryTitle: TextView = findViewById(R.id.categoryTitle)
        categoryTitle.text = if (showFavoritesOnly) {
            "Favoritos "
        } else {
            "Categoría: $selectedCategory"
        }
    }
}