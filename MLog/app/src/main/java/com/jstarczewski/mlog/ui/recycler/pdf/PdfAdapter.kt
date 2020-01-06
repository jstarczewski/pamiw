package com.jstarczewski.mlog.ui.recycler.pdf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.R

class PdfAdapter(
    private val onPdfDelete: ((Long) -> Unit)? = null
) :
    RecyclerView.Adapter<PdfViewHolder>() {

    private val items: MutableList<PdfResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.setTitle(items[position].fileName)
        holder.setAuthor(items[position].author)
        onPdfDelete?.let {
            holder.setOnDelteAction { it(items[position].id) }
        } ?: run {
            holder.setOnDelteAction(null)
        }
    }

    fun replaceItemsAndNotifyAboutChange(items: List<PdfResponse>) =
        with(this.items) {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
}