package com.imennmn.myprofileapp.ui

import android.os.Bundle
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.controllers.FirebaseRequestManager

class MainActivity : BaseActivity(), ProfileFragment.OnListFragmentInteractionListener {

    val firebaseManager   = FirebaseRequestManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(ProfileFragment(), R.id.frame, false)
//        firebaseManager.initFirebase(this)
//        firebaseManager.validatePhoneNumber("+21695600411")
    }



    override fun openAddPhoneNumberView(){
        AddPhoneNumberActivity.openNewActivity(this)
    }



}
