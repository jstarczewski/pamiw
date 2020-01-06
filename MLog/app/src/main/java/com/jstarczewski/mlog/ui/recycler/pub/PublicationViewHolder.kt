package com.jstarczewski.mlog.ui.recycler.pub

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.ui.recycler.pdf.PdfAdapter
import kotlinx.android.synthetic.main.item_pub.view.*

class PublicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setTitle(title: String) {
        itemView.itemPubTitleTv.text = title
    }

    fun setDescription(description: String) {
        itemView.itemPubDescriptionTv.text = description
    }

    fun setAuthor(author: String) {
        itemView.itemPubAuthorTv.text = author
    }

    fun setPdfs(pdfs: List<PdfResponse>) {
        itemView.itemPubPdfsRv.apply {
            setHasFixedSize(true)
            adapter = PdfAdapter().also {
                it.replaceItemsAndNotifyAboutChange(pdfs)
            }
        }
    }

    fun setLinkAction(action: (String) -> Unit) {
        itemView.itemPubLinkBt.setOnClickListener {
            action(itemView.itemPubFilenameEt.text.toString())
        }
    }

    fun setUnlinkAction(action: (String) -> Unit) {
        itemView.itemPubUnlinkBt.setOnClickListener {
            action(itemView.itemPubFilenameEt.text.toString())
        }
    }

    fun setDeleteAction(action: () -> Unit) {
        itemView.itemPubDeleteBt.setOnClickListener {
            action()
        }
    }
}