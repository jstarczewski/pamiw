package com.jstarczewski.mlog.vm

import androidx.lifecycle.ViewModel
import com.jstarczewski.mlog.async
import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.remote.pub.PublicationDataSource

class AddPubViewModel(
    private val publicationDataSource: PublicationDataSource,
    private val user: User
) : ViewModel() {

    fun submitForm(title: String, description: String) {
        async {
            publicationDataSource.addPublication(title, description, user)
        }
    }
}