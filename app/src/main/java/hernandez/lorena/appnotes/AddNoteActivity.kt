package hernandez.lorena.appnotes

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hernandez.lorena.appnotes.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        // Guardar nota
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val note = Note(0, title, content)
            db.insertNote(note)
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Cancelar nota
        binding.btnCancel.setOnClickListener {
            showCancelConfirmationDialog()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showCancelConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cancelar nota")
            .setMessage("¿Estás seguro de que quieres salir sin guardar la nota?")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
