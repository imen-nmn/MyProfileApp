package com.imennmn.myprofileapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.telephony.TelephonyManager
import android.view.View
import com.imennmn.myprofileapp.R
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil


class AddPhoneNumberActivity : BaseActivity() {

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
        showMessage(getCountryCode())
        setSupportActionBar(toolbar)
    }

    fun getCountryCode() : String {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var prefixCode: String = tm.networkCountryIso.toUpperCase()
        val  phoneUtil= PhoneNumberUtil.createInstance(this)
        try {
            var countryCode = phoneUtil.getCountryCodeForRegion(prefixCode)
            return "+"+countryCode
        } catch (e: NumberParseException) {
            System.err.println("NumberParseException was thrown: " + e.toString())
        }
        return ""
    }
}
