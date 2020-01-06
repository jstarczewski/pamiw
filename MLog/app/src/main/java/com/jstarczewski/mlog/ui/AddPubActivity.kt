package com.jstarczewski.mlog.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jstarczewski.mlog.R
import com.jstarczewski.mlog.vm.AddPubViewModel
import com.jstarczewski.mlog.vm.factory.ViewModeFactory
import kotlinx.android.synthetic.main.activity_add_pub.*

class AddPubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pub)

        val vm = ViewModeFactory().create<AddPubViewModel>()
        submitPubBt.setOnClickListener {
            vm?.submitForm(pubTitleEt.text.toString(), pubDescriptionEt.text.toString())
            onBackPressed()
        }
    }
}
