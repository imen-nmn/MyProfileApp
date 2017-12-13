package com.imennmn.myprofileapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.extras.AlertDialogClickListener

/**
 * Created by imen_nmn on 15/11/17.
 */

open class BaseActivity : AppCompatActivity() {

    /**
     * Method to replace fragment
     *
     * @param fragment
     * @param withBackStack
     */
    protected fun replaceFragment(fragment: Fragment, containerId: Int, withBackStack: Boolean) {
        var backStateName: String? = fragment::class.simpleName
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment, backStateName)
        if (withBackStack)
            fragmentTransaction.addToBackStack(backStateName)
        fragmentTransaction.commit()
    }

    /**
     * Method to show a message
     * @param messageToShow
     */
    public fun showMessage(messageToShow: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.app_name)
        alertDialogBuilder.setMessage(messageToShow).setCancelable(false)
                .setPositiveButton(android.R.string.ok) { dialog, id -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    public fun showAlertDialogWithDecision(messageToShow: String,
                                    positiveMsg: String,
                                    negativeMsg: String,
                                    callBack: AlertDialogClickListener?) {
        hideSoftKeyboard(currentFocus)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.app_name)
        alertDialogBuilder.setMessage(messageToShow).setCancelable(false)
                .setPositiveButton(positiveMsg) { dialog, id ->
                     callBack!!.positiveButtonClicked()

                }
                .setNegativeButton(negativeMsg) { dialog, id ->
                    callBack!!.negativeButtonClicked()
                    dialog.cancel()
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    /**
     * Method to hide keyboard
     * @param aView
     */
    public fun hideSoftKeyboard(aView: View?): Boolean {
        return try {
            val inputMethodManager = aView!!.context.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(aView.windowToken, 0)
            //            aView.setFocusable(false);
            true
        } catch (ex: Exception) {
            false
        }

    }
}
