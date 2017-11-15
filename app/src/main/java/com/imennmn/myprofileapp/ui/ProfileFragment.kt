package com.imennmn.myprofileapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.controllers.ContentRecyclerViewAdapter
import com.imennmn.myprofileapp.controllers.TakePhotoController
import com.imennmn.myprofileapp.controllers.UploadPhotoController
import com.imennmn.myprofileapp.extras.AlertDialogClickListener
import java.lang.Exception

class ProfileFragment : Fragment(), OnFailureListener,
        OnSuccessListener<UploadTask.TaskSnapshot> {
    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private var mListener: OnListFragmentInteractionListener? = null
    private var profileImg: ImageView? = null
    private var progressBarProfileImg: ProgressBar? = null
    private val requestCameraPermission = 1
    private val requestTakePhoto = 0x38
    private val takePhotoController = TakePhotoController()
    private val uploadPhotoController = UploadPhotoController()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.profile_fragment, container, false)
        val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        profileImg = view.findViewById(R.id.profile_img)
        progressBarProfileImg = view.findViewById(R.id.progressBar)
        progressBarProfileImg!!.visibility = View.GONE
        profileImg!!.setOnClickListener { openCamera() }
        toolbar.title = ""
        toolbar.subtitle = ""
        mListener!!.setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val items = resources.getStringArray(R.array.profile_menu)
        recyclerView.adapter = ContentRecyclerViewAdapter(items.asList(), mListener)
        uploadPhotoController.init(context)
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
        if ((resultCode == Activity.RESULT_OK) && (requestCode == requestTakePhoto)) {
            val photoBitmap = takePhotoController.generatePhoto()
            progressBarProfileImg!!.visibility = View.VISIBLE
            uploadPhotoController.uploadPhoto(photoBitmap, this, this)
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

    /**************/
    override fun onFailure(e: Exception) {
        mListener!!.showMessage("Failed to load profile picture")
        progressBarProfileImg!!.visibility = View.GONE
    }

    override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
        var downloadUrl = taskSnapshot!!.downloadUrl
        Glide.with(this)
                .load(downloadUrl)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.avatar)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        progressBarProfileImg!!.visibility = View.GONE
                        if (bitmap != null) {
                            profileImg!!.setImageBitmap(bitmap)
                        }
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        progressBarProfileImg!!.visibility = View.GONE
                    }
                })
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
        fun openAddPhoneNumberView()
        fun setSupportActionBar(toolbar: Toolbar?)
        fun showMessage(messageToShow: String)
        fun showAlertDialogWithDecision(messageToShow: String,
                                        positiveMsg: String,
                                        negativeMsg: String,
                                        callBack: AlertDialogClickListener?)
    }

}
