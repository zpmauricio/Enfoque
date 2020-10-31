package net.it96.enfoque.fragments.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.keyresult_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.KeyResult
import net.it96.enfoque.database.Project
import net.it96.enfoque.viewmodels.ProjectViewModel

class KeyResultsAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<KeyResultsAdapter.KeyResultsViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : KeyResult? = null

    private var keyResultsData = mutableListOf<KeyResult>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeyResultsAdapter.KeyResultsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.keyresult_row, parent, false)
        return KeyResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeyResultsAdapter.KeyResultsViewHolder, position: Int) {
        val currentItem = keyResultsData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = keyResultsData.size

    inner class KeyResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(keyResult: KeyResult) {
            itemView.txt_keyResult.text = keyResult.description
        }
    }

    fun setListData(data: MutableList<KeyResult>) {
        keyResultsData = data
    }

    fun getObject(pos: Int) : KeyResult {
        return keyResultsData[pos]
    }

    fun addKeyResult(keyResult: KeyResult) {
        keyResultsData.toMutableList().add(keyResult)
        notifyDataSetChanged()
    }

    fun deleteKeyResult(keyResult: KeyResult, selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = keyResultsData[removedPosition]

        keyResultsData.removeAt(removedPosition)
        projectViewModel.deleteKeyResult(keyResult, selectedProject)
        notifyItemRemoved(removedPosition)


        Snackbar.make(viewHolder.itemView, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addKeyResult(keyResult, selectedProject)
            keyResultsData.toMutableList().add(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}