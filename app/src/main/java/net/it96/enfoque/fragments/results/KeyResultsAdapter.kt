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
import timber.log.Timber

class KeyResultsAdapter(
    private val context: Context,
    private var keyResultsList: List<KeyResult>,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<KeyResultsAdapter.KeyResultsViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : KeyResult? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeyResultsAdapter.KeyResultsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.keyresult_row, parent, false)
        return KeyResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeyResultsAdapter.KeyResultsViewHolder, position: Int) {
        val currentItem = keyResultsList[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int {
        return keyResultsList.size
    }

    inner class KeyResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(keyResult: KeyResult) {
            Timber.i("***MZP*** keyResult.result: ${keyResult.description}")
            itemView.txt_keyResult.text = keyResult.description
        }
    }

    fun getObject(pos: Int) : KeyResult {
        return keyResultsList[pos]
    }

    fun deleteKeyResult(keyResult: KeyResult, selectedProject: Project, viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = keyResultsList[removedPosition]

        projectViewModel.deleteKeyResult(keyResult, selectedProject)
        notifyItemRemoved(removedPosition)

        Snackbar.make(viewHolder.itemView, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addKeyResult(keyResult, selectedProject)
            notifyItemInserted(removedPosition)
        }.show()
    }
}