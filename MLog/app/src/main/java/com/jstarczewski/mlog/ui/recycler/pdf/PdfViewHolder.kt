package com.jstarczewski.mlog.ui.recycler.pdf

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pdf.view.*

class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setTitle(title: String) {
        itemView.itemPdfTitleTv.text = title
    }

    fun setAuthor(author: String) {
        itemView.itemPdfAuthorTv.text = author
    }

    fun setOnDelteAction(action: (() -> Unit)?) {
        action?.let { action ->
            itemView.itemPdfDeleteBt.setOnClickListener {
                action()
            }
        } ?: run {
            itemView.itemPdfDeleteBt.visibility = View.GONE
        }
    }
}