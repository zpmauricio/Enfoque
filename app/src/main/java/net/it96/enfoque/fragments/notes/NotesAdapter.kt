package net.it96.enfoque.fragments.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Note
import timber.log.Timber

class NotesAdapter (
    private val context: Context,
    private var notesList: List<Note>,
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotesAdapter.NotesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_row, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NotesViewHolder, position: Int) {
        val currentItem = notesList[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Note) {
            Timber.i("***MZP*** note.description: ${note.description}")
            itemView.txt_note.text = note.description
        }
    }

    fun setListData(data: MutableList<Note>) {
        notesList = data
    }
}
