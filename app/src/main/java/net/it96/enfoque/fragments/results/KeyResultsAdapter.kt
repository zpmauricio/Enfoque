package net.it96.enfoque.fragments.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.keyresult_row.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.KeyResult
import timber.log.Timber

class KeyResultsAdapter(
    private val context: Context,
    private var keyResultsList: List<KeyResult>,
) : RecyclerView.Adapter<KeyResultsAdapter.KeyResultsViewHolder>() {

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

    fun setListData(data: MutableList<KeyResult>) {
        keyResultsList = data
    }
}