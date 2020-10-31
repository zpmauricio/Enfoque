package net.it96.enfoque.fragments.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.note_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Note
import net.it96.enfoque.database.Project
import net.it96.enfoque.viewmodels.ProjectViewModel

class NotesAdapter (
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Note? = null

    private var notesData = mutableListOf<Note>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotesAdapter.NotesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_row, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NotesViewHolder, position: Int) {
        val currentItem = notesData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = notesData.size

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Note) {
            itemView.txt_note.text = note.description
        }
    }

    fun setListData(data: MutableList<Note>) {
        notesData = data
    }

    fun getObject(pos: Int) : Note {
        return notesData[pos]
    }

    fun addNote(note: Note) {
        notesData.toMutableList().add(note)
        notifyDataSetChanged()
    }

    fun deleteNote(note: Note, selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = notesData[removedPosition]

        notesData.removeAt(removedPosition)
        projectViewModel.deleteNote(note, selectedProject)
        notifyItemRemoved(removedPosition)


        Snackbar.make(viewHolder.itemView, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addNote(note, selectedProject)
            notesData.toMutableList().add(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}
