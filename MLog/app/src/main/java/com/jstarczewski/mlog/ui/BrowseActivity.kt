package com.jstarczewski.mlog.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jstarczewski.mlog.R
import com.jstarczewski.mlog.ui.recycler.pdf.PdfAdapter
import com.jstarczewski.mlog.ui.recycler.pub.PublicationAdapter
import com.jstarczewski.mlog.vm.BrowseViewModel
import com.jstarczewski.mlog.vm.factory.ViewModeFactory
import kotlinx.android.synthetic.main.activity_browse.*

class BrowseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        ViewModeFactory().create<BrowseViewModel>()?.let {
            setupRecyclerViews(it)
            setupObservers(it)
            it.init()
        }
    }

    private fun setupRecyclerViews(vm: BrowseViewModel) {
        activityBrowsePdfRv.apply {
            setHasFixedSize(true)
            adapter = PdfAdapter { vm.onDeletePdf(it) }
        }
        activityBrowsePubRv.apply {
            setHasFixedSize(true)
            adapter =
                PublicationAdapter(
                    { name, id -> vm.onLinkPdf(name, id) },
                    { name, id -> vm.onUnlinkPdf(name, id) },
                    { vm.onDeletePub(it) })
        }
    }

    private fun setupObservers(vm: BrowseViewModel) {
        vm.publications.observe(this, Observer {
            (activityBrowsePubRv.adapter as PublicationAdapter).replaceItemsAndNotifyAboutChange(it)
        })
        vm.pdfs.observe(this, Observer {
            (activityBrowsePdfRv.adapter as PdfAdapter).replaceItemsAndNotifyAboutChange(it)
        })
    }
}
