package hernandez.lorena.appnotes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val context: Context, private var noteList: MutableList<Note>) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.tvTitle.text = note.title
        holder.tvContent.text = note.content

        holder.btnEdit.setImageResource(R.drawable.baseline_edit_24)

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, UpdateNoteActivity::class.java)
            intent.putExtra("note_id", note.id)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteDetailActivity::class.java).apply {
                putExtra("note_title", note.title)
                putExtra("note_content", note.content)
                putExtra("note_date", note.date)
                putExtra("note_category", note.category)
                putExtra("note_favorite", note.isFavorite)
            }
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle("Eliminar nota")
            builder.setMessage("¿Estás seguro de que quieres eliminar esta nota?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                val positionToDelete = holder.adapterPosition
                if (positionToDelete in noteList.indices) {
                    val db = NotesDatabaseHelper(context)
                    db.deleteNote(noteList[positionToDelete].id)

                    noteList.removeAt(positionToDelete)
                    notifyItemRemoved(positionToDelete)
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    override fun getItemCount(): Int = noteList.size

    fun updateNotes(newNotes: List<Note>) {
        this.noteList = newNotes.toMutableList()
        notifyDataSetChanged()
    }

    fun refreshData(newNotes: List<Note>) {
        noteList = newNotes.toMutableList()
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Note>) {
        noteList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun addNewNote(newNote: Note) {
        noteList.add(newNote)
        notifyItemInserted(noteList.size - 1)
    }
}
