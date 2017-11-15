package com.imennmn.myprofileapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.imennmn.myprofileapp.R



class AddPhoneNumberActivity : AppCompatActivity() {

    companion object {
        fun openNewActivity(fromActivity: Activity) {
            val intent = Intent(fromActivity, AddPhoneNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            fromActivity.startActivity(intent)
            fromActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_change)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phone_number)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        toolbar!!.title=""
        toolbar!!.subtitle=""
        setSupportActionBar(toolbar)
    }
}
