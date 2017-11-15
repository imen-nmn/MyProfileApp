package com.imennmn.myprofileapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.controllers.ContentRecyclerViewAdapter
import com.imennmn.myprofileapp.controllers.TakePhotoController
import com.imennmn.myprofileapp.extras.AlertDialogClickListener

class ProfileFragment : Fragment() {
    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private var mListener: OnListFragmentInteractionListener? = null
    private var profileImg: ImageView? = null
    private val requestCameraPermission = 1
    private val requestTakePhoto = 0x38
    private val takePhotoController = TakePhotoController()

//    private val cameraPermissionListener :


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.profile_fragment, container, false)
        val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        profileImg = view.findViewById(R.id.profile_img)
        profileImg!!.setOnClickListener { openCamera() }
        toolbar.title = ""
        toolbar.subtitle = ""
        mListener!!.setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val items = resources.getStringArray(R.array.profile_menu)
        recyclerView.adapter = ContentRecyclerViewAdapter(items.asList(), mListener)

        return view
    }

    private fun openCamera() {
        if (checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
            return
        }
        takePhotoController.takePicture(this, requestTakePhoto)
    }

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val cameraPermissionListener = object : AlertDialogClickListener {
                override fun positiveButtonClicked() {
                    requestPermissions(cameraPermissions,
                            requestCameraPermission)
                }

                override fun negativeButtonClicked() {

                }
            }
            mListener!!.showAlertDialogWithDecision(getString(R.string.request_permission),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel), cameraPermissionListener)
        } else {
            requestPermissions(cameraPermissions, requestCameraPermission)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == requestCameraPermission) {
            if (checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                mListener!!.showMessage(getString(R.string.request_permission))
            } else {
                openCamera()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestTakePhoto) {
                val photoBitmap = takePhotoController.generatePhoto()
                profileImg!!.setImageBitmap(photoBitmap)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: String)

        fun setSupportActionBar(toolbar: Toolbar?)
        fun showMessage(messageToShow: String)
        fun showAlertDialogWithDecision(messageToShow: String,
                                        positiveMsg: String,
                                        negativeMsg: String,
                                        callBack: AlertDialogClickListener?)
    }

}
