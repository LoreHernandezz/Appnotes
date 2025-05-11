package hernandez.lorena.appnotes

data class Note(val id: Int, val title: String, val content: String, val date: String?, val category: String?, val isFavorite: Boolean)