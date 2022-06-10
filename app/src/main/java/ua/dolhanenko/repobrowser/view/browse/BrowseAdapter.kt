package ua.dolhanenko.repobrowser.view.browse

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.browse_list_item.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.utils.colorPrimary


class BrowseAdapter : RecyclerView.Adapter<BrowseAdapter.ViewHolder>() {
    var dataList: List<RepositoryModel> = listOf()
    var currentFilter: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.browse_list_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: RepositoryModel) {
            itemView.apply {
                indexTextView.text = (adapterPosition + 1).toString()
                insertHighlightedTitle(model.title, currentFilter)
                repoDescription.text = model.description
                language.text = model.language ?: ""
                watchCount.text = model.watchers.toString()
                starCount.text = model.stars.toString()
                userName.text = model.owner.name
                Glide.with(this).load(model.owner.avatarUrl).into(avatarView)
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
}