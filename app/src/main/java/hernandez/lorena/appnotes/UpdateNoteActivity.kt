package hernandez.lorena.appnotes

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etDate: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var checkBoxFavorite: CheckBox

    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancel)
        etDate = findViewById(R.id.etDate)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        checkBoxFavorite = findViewById(R.id.checkBoxFavorite)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->

                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etDate.setText(formattedDate)
                },
                year, month, dayOfMonth
            )
            datePickerDialog.show()
        }

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
            etDate.setText(note.date)
            checkBoxFavorite.isChecked = note.isFavorite

            val categoriesList = listOf("Personal", "Trabajo", "Escuela", "Otra")

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter


            // Obtener la posición de la categoría de la nota
            val categoryPosition = categoriesList.indexOf(note.category)

            // Si la categoría existe en la lista, selecciona la posición correcta
            if (categoryPosition != -1) {
                spinnerCategory.setSelection(categoryPosition)
            }


            // Manejo de clic en el botón de guardar
            btnUpdate.setOnClickListener {
                val updatedTitle = etTitle.text.toString()
                val updatedContent = etContent.text.toString()
                val updatedDate = etDate.text.toString()  // Aquí asignamos la fecha correcta
                val updatedCategory = spinnerCategory.selectedItem?.toString() ?: "Categoría desconocida"
                val updatedIsFavorite = checkBoxFavorite.isChecked

                if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                    // Formatear la fecha antes de guardarla
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val parsedDate = try { sdf.parse(updatedDate) } catch (e: Exception) { null }
                    val formattedDate = parsedDate?.let { sdf.format(it) } ?: updatedDate  // Si falla, usa la fecha original

                    // Crear un objeto Note con los valores actualizados
                    val updatedNote = Note(
                        noteId,  // ID de la nota original
                        updatedTitle,
                        updatedContent,
                        formattedDate,  // Se guarda la fecha con el formato correcto
                        updatedCategory,
                        updatedIsFavorite
                    )

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