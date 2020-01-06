package com.jstarczewski.mlog.ui.recycler.pub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jstarczewski.mlog.R
import com.jstarczewski.mlog.assign
import com.jstarczewski.mlog.model.publication.api.PublicationResponse

class PublicationAdapter(
    private val linkAction: (String, Long) -> Unit,
    private val unlinkAction: (String, Long) -> Unit,
    private val deleteAction: (Long) -> Unit
) :
    RecyclerView.Adapter<PublicationViewHolder>() {

    private val items: MutableList<PublicationResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pub, parent, false)
        return PublicationViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) =
        holder.assign {
            setTitle(items[position].title)
            setAuthor(items[position].author)
            setDescription(items[position].description)
            items[position].pdfs?.let {
                setPdfs(it)
            }
            setLinkAction { linkAction(it, items[position].id) }
            setUnlinkAction { unlinkAction(it, items[position].id) }
            setDeleteAction { deleteAction(items[position].id) }
        }

    fun replaceItemsAndNotifyAboutChange(items: List<PublicationResponse>) =
        with(this.items) {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
}