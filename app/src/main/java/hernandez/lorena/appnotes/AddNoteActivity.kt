package hernandez.lorena.appnotes

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hernandez.lorena.appnotes.databinding.ActivityAddNoteBinding
import java.util.Calendar

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()

        binding.dateEditText.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
                binding.dateEditText.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }


        val categories = arrayOf("Escuela", "Trabajo", "Personal", "Otra")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        db = NotesDatabaseHelper(this)

        // Guardar nota
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val date = binding.dateEditText.text.toString()
            val category = binding.categorySpinner.selectedItem?.toString() ?: "Otra"
            val isFavorite = binding.favoriteCheckBox.isChecked

            val note = Note(0, title, content, date, category, isFavorite)

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
