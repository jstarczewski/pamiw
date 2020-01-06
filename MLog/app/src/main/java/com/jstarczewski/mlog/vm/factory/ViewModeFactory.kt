package com.jstarczewski.mlog.vm.factory

import androidx.lifecycle.ViewModel
import com.jstarczewski.mlog.Injection
import com.jstarczewski.mlog.vm.AddPubViewModel
import com.jstarczewski.mlog.vm.BrowseViewModel

class ViewModeFactory {

    inline fun <reified T : ViewModel> create(): T? =
        when (T::class) {
            AddPubViewModel::class -> AddPubViewModel(
                Injection.providePublicationDataSource(),
                Injection.provideUser()
            )
            BrowseViewModel::class -> BrowseViewModel(
                Injection.providePublicationDataSource(),
                Injection.providePdfDataSource(),
                Injection.provideUser()
            )
            else -> null
        } as T
}
