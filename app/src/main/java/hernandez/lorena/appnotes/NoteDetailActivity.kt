package hernandez.lorena.appnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hernandez.lorena.appnotes.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val title = intent.getStringExtra("note_title")
        val content = intent.getStringExtra("note_content")
        val date = intent.getStringExtra("note_date")
        val category = intent.getStringExtra("note_category")
        val isFavorite = intent.getBooleanExtra("note_favorite", false)

        Log.d("NoteDetailActivity", "Received data - Title: $title, Content: $content, Date: $date, Category: $category, Favorite: $isFavorite")


        binding.tvBackPrincipal.text = title
        binding.tvDetailContent.text = content
        binding.tvDetailDate.text = "Fecha: ${date ?: "Sin fecha"}"
        binding.tvDetailCategory.text = "Categoría: ${category ?: "Sin categoría"}"
        binding.tvDetailFavorite.text = if (isFavorite) "★ Favorita" else "☆ No favorita"

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}