package ua.dolhanenko.repobrowser.presentation.common

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.browse_list_item.view.*
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.presentation.databinding.BrowseListItemBinding
import ua.dolhanenko.repobrowser.presentation.utils.colorPrimary
import ua.dolhanenko.repobrowser.presentation.utils.toVisibility
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class RepositoriesAdapter(val showPositions: Boolean, val callback: Callback) :
    RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {
    var dataList: List<IRepositoryModel> = listOf()
        set(value) {
            val callback = DiffCallback(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }
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
                Glide.with(avatarView).load(model.owner?.avatarUrl).into(avatarView)
                root.setOnClickListener {
                    callback.onItemClick(model, adapterPosition)
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

    class DiffCallback(
        private val oldList: List<IRepositoryModel>,
        private val newList: List<IRepositoryModel>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem && oldItem.readAt == newItem.readAt
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    interface Callback {
        fun onItemClick(model: IRepositoryModel, position: Int)
    }
}