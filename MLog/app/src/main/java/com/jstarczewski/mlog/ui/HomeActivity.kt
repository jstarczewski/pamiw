package com.jstarczewski.mlog.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jstarczewski.mlog.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpView()
    }

    private fun setUpView() {
        addPubBt.setOnClickListener {
            startActivity(Intent(this, AddPubActivity::class.java))
        }
        browsePubsBt.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
        }
    }
}
