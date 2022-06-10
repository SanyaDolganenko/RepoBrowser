package ua.dolhanenko.repobrowser.view.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.browse_list_item.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel


class BrowseAdapter : RecyclerView.Adapter<BrowseAdapter.ViewHolder>() {
    var dataList: List<RepositoryModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.browse_list_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        holder.itemView.apply {
            repoTitle.text = model.title
            repoDescription.text = model.description
            language.text = model.language ?: ""
            watchCount.text = model.watchers.toString()
            starCount.text = model.stars.toString()
            userName.text = model.owner.name
            Glide.with(this).load(model.owner.avatarUrl).into(avatarView)
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}