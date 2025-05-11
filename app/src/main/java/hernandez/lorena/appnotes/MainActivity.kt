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
import hernandez.lorena.appnotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var allNotes: MutableList<Note>
    private var isAscending = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        allNotes = db.getAllNotes().toMutableList()
        notesAdapter = NotesAdapter(this, allNotes)


        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
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
        allNotes = db.getAllNotes().toMutableList()
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
