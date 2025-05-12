package hernandez.lorena.appnotes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hernandez.lorena.appnotes.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var allNotes: MutableList<Note>
    private var isAscending = true

    // Hacer categories y foldersAdapter propiedades globales
    private val categories = listOf("Escuela", "Trabajo", "Personal", "Otra")
    private lateinit var foldersAdapter: FoldersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        allNotes = db.getAllNotes().toMutableList()
        notesAdapter = NotesAdapter(this, allNotes)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        // Recyclerview de carpetas
        val foldersRecyclerView = findViewById<RecyclerView>(R.id.foldersRecyclerView)
        foldersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Inicializar foldersAdapter
        val folderList = categories.map { category ->
            val count = allNotes.count { it.category == category }
            Folder(category, count)
        }

        foldersAdapter = FoldersAdapter(folderList) { selectedFolder ->
            val category = selectedFolder.name
            val intent = Intent(this, NotesByCategoryActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        foldersRecyclerView.adapter = foldersAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        val sortButton = findViewById<ImageButton>(R.id.btnSortAZ)
        sortButton.setOnClickListener {
            isAscending = !isAscending
            sortNotes()
        }

        // Búsqueda por título
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().lowercase()
                val filteredList = allNotes.filter {
                    it.title.lowercase().contains(searchText)
                }
                notesAdapter.updateList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()

        // Recalcular todas las notas
        allNotes = db.getAllNotes().toMutableList()
        notesAdapter.refreshData(allNotes)  // Actualiza el adaptador de notas con la nueva lista de notas

        // Recalcular las carpetas
        val folderList = categories.map { category ->
            val count = allNotes.count { it.category == category }
            Folder(category, count)
        }

        // Actualizar el adaptador de carpetas y notificar que los datos han cambiado
        foldersAdapter.updateFolderList(folderList)
    }

    private fun deleteNote(note: Note) {
        db.deleteNote(note.id)  // Eliminar la nota de la base de datos
        allNotes.remove(note)   // Eliminar la nota de la lista de notas

        // Actualizar las carpetas después de la eliminación
        val folderList = categories.map { category ->
            val count = allNotes.count { it.category == category }
            Folder(category, count)
        }

        // Actualizar el adaptador de carpetas con la nueva lista
        foldersAdapter.updateFolderList(folderList)

        // Actualizar el adaptador de notas
        notesAdapter.refreshData(allNotes)
    }

    private fun sortNotes() {
        if (isAscending) {
            allNotes.sortBy { it.title.lowercase() }
        } else {
            allNotes.sortByDescending { it.title.lowercase() }
        }
        notesAdapter.refreshData(allNotes)
    }
}
