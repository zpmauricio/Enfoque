package net.it96.enfoque.fragments.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.it96.enfoque.database.KeyResult
import net.it96.enfoque.databinding.FragmentProjectDetailBinding
import net.it96.enfoque.databinding.KeyresultRowBinding
import net.it96.enfoque.viewmodels.ProjectViewModel

class KeyResultsAdapter(
    private val context: Context,
    private var projectViewModel : ProjectViewModel
) : RecyclerView.Adapter<KeyResultsAdapter.KeyResultsViewHolder>() {

    private var removedPosition : Int = 0
    private var removedItem : KeyResult? = null
    var topId : Int = 0

    private var keyResultsData = mutableListOf<KeyResult>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeyResultsAdapter.KeyResultsViewHolder {
        val itemBinding = KeyresultRowBinding.inflate(LayoutInflater.from(context), parent, false)

        return KeyResultsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: KeyResultsAdapter.KeyResultsViewHolder, position: Int) {
        val currentItem = keyResultsData[position]
        holder.bindView(currentItem)
    }

    override fun getItemCount(): Int = keyResultsData.size

    inner class KeyResultsViewHolder(val binding: KeyresultRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(keyResult: KeyResult) {
            binding.txtKeyResult.text = keyResult.description
            binding.etxtEditKeyResult.setText(keyResult.description)
            if(keyResult.id.toInt() > topId) topId = keyResult.id.toInt()

            binding.cvRowKeyResults.setOnClickListener {
                it.visibility = View.GONE
                binding.cvRowEditKeyResults.visibility = View.VISIBLE
            }

            binding.ibSaveEditKeyResult.setOnClickListener {
                keyResult.description = binding.etxtEditKeyResult.text.toString()
                projectViewModel.editKeyResult(keyResult)
                binding.cvRowEditKeyResults.visibility = View.GONE
                binding.cvRowKeyResults.visibility = View.VISIBLE
            }
        }
    }

    fun setListData(data: MutableList<KeyResult>) {
        keyResultsData = data
    }

    fun getObject(pos: Int) : KeyResult {
        return keyResultsData[pos]
    }

    fun addKeyResult(keyResult: KeyResult) {
        keyResultsData.add(keyResult)
    }

    fun deleteKeyResult(keyResult: KeyResult, viewHolder: RecyclerView.ViewHolder, binding: FragmentProjectDetailBinding) {
        removedPosition = viewHolder.adapterPosition
        removedItem = keyResultsData[removedPosition]

        keyResultsData.removeAt(removedPosition)
        projectViewModel.deleteKeyResult(keyResult)
        notifyItemRemoved(removedPosition)

        Snackbar.make(binding.clDetailMainLayout, "${removedItem!!.description} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            projectViewModel.addKeyResult(keyResult)
            addKeyResult(removedItem!!)
            notifyDataSetChanged()
        }.show()
    }
}