package hernandez.lorena.appnotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoldersAdapter(private var folderList: List<Folder>, private val onFolderClick: (Folder) -> Unit) :
    RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>() {

    fun updateFolderList(newFolderList: List<Folder>) {
        folderList = newFolderList
        notifyDataSetChanged()
    }

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage: ImageView = itemView.findViewById(R.id.folderImage)
        val folderName: TextView = itemView.findViewById(R.id.folderName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]

        val imageResId = if (folder.noteCount > 0) {
            R.drawable.llena // Asegúrate de tener esta imagen en drawable
        } else {
            R.drawable.carpeta // Asegúrate de tener esta imagen en drawable
        }

        holder.folderImage.setImageResource(imageResId)
        holder.folderName.text = folder.name

        holder.itemView.setOnClickListener {
            onFolderClick(folder)
        }
    }

    override fun getItemCount(): Int = folderList.size

}