package ua.dolhanenko.repobrowser.presentation.common

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.browse_list_item.view.*
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.presentation.databinding.BrowseListItemBinding
import ua.dolhanenko.repobrowser.presentation.utils.colorPrimary
import ua.dolhanenko.repobrowser.presentation.utils.loader.IImageLoader
import ua.dolhanenko.repobrowser.presentation.utils.toVisibility
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


internal class RepositoriesAdapter @Inject constructor(private val imageLoader: IImageLoader) :
    RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {
    /**
     * Enable or disable position views on the beginning of view holders.
     */
    var showPositions: Boolean = false
    var callback: Callback? = null
    var dataList: List<IRepositoryModel> = listOf()
        set(value) {
            val callback =
                DiffCallback(filterUsedForLastUpdate.equals(currentFilter).not(), field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
            filterUsedForLastUpdate = currentFilter
        }
    private var filterUsedForLastUpdate: String? = null
    var currentFilter: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BrowseListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: BrowseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IRepositoryModel) {
            binding.apply {
                indexTextView.visibility = showPositions.toVisibility(true)
                if (showPositions) {
                    indexTextView.text = (adapterPosition + 1).toString()
                }
                insertHighlightedTitle(model.title, currentFilter)
                repoDescription.text = model.description
                language.text = model.language ?: ""
                watchCount.text = model.watchers.toString()
                starCount.text = model.stars.toString()
                userName.text = model.owner?.name
                readAt.visibility = View.INVISIBLE
                readImageView.visibility = View.INVISIBLE
                readIndicator.visibility = View.INVISIBLE
                model.readAt?.let {
                    readIndicator.visibility = View.VISIBLE
                    readAt.visibility = View.VISIBLE
                    readImageView.visibility = View.VISIBLE
                    readAt.text = SimpleDateFormat.getDateTimeInstance(
                        DateFormat.SHORT,
                        DateFormat.SHORT, Locale.getDefault()
                    ).format(it)
                }
                imageLoader.load(model.owner?.avatarUrl, avatarView)
                root.setOnClickListener {
                    callback?.onItemClick(model, adapterPosition)
                }
            }
        }

        private fun insertHighlightedTitle(title: String, filter: String?) {
            val spanned = SpannableStringBuilder().append(title)
            if (!filter.isNullOrEmpty()) {
                val index = title.lowercase().indexOf(filter.lowercase())
                if (index in title.indices) {
                    spanned.setSpan(
                        ForegroundColorSpan(itemView.context.colorPrimary),
                        index,
                        index + filter.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
            }
            itemView.repoTitle.text = spanned
        }
    }

    private class DiffCallback(
        private val filterChanged: Boolean,
        private val oldList: List<IRepositoryModel>,
        private val newList: List<IRepositoryModel>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return filterChanged.not() && oldItem == newItem
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    interface Callback {
        fun onItemClick(model: IRepositoryModel, position: Int)
    }
}