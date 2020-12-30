package net.it96.enfoque.fragments.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.it96.enfoque.database.Note
import net.it96.enfoque.databinding.FragmentProjectDetailBinding
import net.it96.enfoque.databinding.NoteRowBinding
import net.it96.enfoque.viewmodels.ProjectViewModel

class NotesAdapter (
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : Note? = null
    var topId : Int = 0

    private var notesData = mutableListOf<Note>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotesAdapter.NotesViewHolder {
        val itemBinding = NoteRowBinding.inflate(LayoutInflater.from(context), parent, false)

        return NotesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NotesViewHolder, position: Int) {
        val currentItem = notesData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = notesData.size

    inner class NotesViewHolder(val binding: NoteRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(note: Note) {
            binding.txtNote.text = note.description
            binding.etxtEditNote.setText(note.description)
            if(note.id.toInt() > topId) topId = note.id.toInt()

            binding.cvRowNotes.setOnClickListener {
                it.visibility = View.GONE
                binding.cvRowEditNotes.visibility = View.VISIBLE
            }

            binding.ibSaveEditNote.setOnClickListener {
                note.description = binding.etxtEditNote.text.toString()
                projectViewModel.editNote(note)
                binding.cvRowEditNotes.visibility = View.GONE
                binding.cvRowNotes.visibility = View.VISIBLE
            }
        }
    }

    fun setListData(data: MutableList<Note>) {
        notesData = data
    }

    fun getObject(pos: Int) : Note {
        return notesData[pos]
    }

    fun addNote(note: Note) {
        notesData.add(note)
    }

    fun deleteNote(note: Note, viewHolder: RecyclerView.ViewHolder, binding : FragmentProjectDetailBinding) {
        removedPosition = viewHolder.adapterPosition
        removedItem = notesData[removedPosition]

        notesData.removeAt(removedPosition)
        projectViewModel.deleteNote(note)
        notifyItemRemoved(removedPosition)


        Snackbar.make(binding.clDetailMainLayout, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addNote(note)
            addNote(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}
