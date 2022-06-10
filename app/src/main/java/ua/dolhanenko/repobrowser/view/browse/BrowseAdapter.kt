package ua.dolhanenko.repobrowser.view.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.browse_list_item.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.data.remote.RepoResponse


class BrowseAdapter : RecyclerView.Adapter<BrowseAdapter.ViewHolder>() {
    var dataList: List<RepoResponse> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.browse_list_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.itemView.repoTitle.text = "$position ${data.name}"

    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}