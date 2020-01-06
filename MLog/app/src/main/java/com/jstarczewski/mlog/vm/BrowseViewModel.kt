package com.jstarczewski.mlog.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.async
import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.model.publication.api.PublicationResponse
import com.jstarczewski.mlog.remote.pdf.PdfDataSource
import com.jstarczewski.mlog.remote.pub.PublicationDataSource

class BrowseViewModel(
    private val publicationDataSource: PublicationDataSource,
    private val pdfsDataSource: PdfDataSource,
    private val user: User
) : ViewModel() {

    private val _pdfs: MutableLiveData<List<PdfResponse>> = MutableLiveData()

    val pdfs: LiveData<List<PdfResponse>> = _pdfs

    private val _publications: MutableLiveData<List<PublicationResponse>> = MutableLiveData()

    val publications: LiveData<List<PublicationResponse>> = _publications

    fun init() = async {
        _pdfs.value = pdfsDataSource.getAllPdfs(user)
        _publications.value = publicationDataSource.getAllPublications(user)
    }

    private fun refresh() = init()

    fun onDeletePdf(id: Long) =
        doAsyncAndRefresh {
            pdfsDataSource.deletePdf(id, user)
        }

    fun onDeletePub(id: Long) =
        doAsyncAndRefresh {
            publicationDataSource.deletePublication(id, user)
        }

    fun onLinkPdf(name: String, pubId: Long) =
        doAsyncAndRefresh {
            publicationDataSource.linkPdf(name, pubId, user)
        }

    fun onUnlinkPdf(name: String, pubId: Long) =
        doAsyncAndRefresh {
            publicationDataSource.unlinkPdf(name, pubId, user)
        }

    private fun doAsyncAndRefresh(block: suspend () -> Unit) =
        async {
            block()
            refresh()
        }
}