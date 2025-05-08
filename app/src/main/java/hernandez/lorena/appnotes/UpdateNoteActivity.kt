package hernandez.lorena.appnotes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancel)

        val noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            Toast.makeText(this, "Error al cargar la nota", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = NotesDatabaseHelper(this)
        val note = dbHelper.getNoteByID(noteId)

        if (note != null) {
            etTitle.setText(note.title)
            etContent.setText(note.content)

            // Manejo de clic en el botón de guardar
            btnUpdate.setOnClickListener {
                val updatedTitle = etTitle.text.toString()
                val updatedContent = etContent.text.toString()

                if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                    // Crear un objeto Note con los valores actualizados
                    val updatedNote = Note(noteId, updatedTitle, updatedContent)
                    dbHelper.updateNote(updatedNote)  // Pasar el objeto Note
                    Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Llena ambos campos", Toast.LENGTH_SHORT).show()
                }
            }

            // Manejo de clic en el botón de cancelar
            btnCancel.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Cancelar edición")
                    .setMessage("¿Estás segura de que quieres salir sin guardar los cambios?")
                    .setPositiveButton("Sí") { _, _ ->
                        finish() // Cierra la actividad y regresa a la anterior (pantalla principal)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

        } else {
            Toast.makeText(this, "Nota no encontrada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
