package com.imennmn.myprofileapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import com.imennmn.myprofileapp.dummy.DummyContent

class MainActivity : AppCompatActivity(), ProfileFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(ProfileFragment.newInstance(10), R.id.frame, false)

    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Method to replace fragment
     *
     * @param fragment
     * @param withBackStack
     */
    open fun replaceFragment(fragment: Fragment, containerId: Int, withBackStack: Boolean) {
        fragment.setEnterTransition(Fade(Fade.IN));
        fragment.setExitTransition(Fade(Fade.OUT));
        var backStateName: String? = fragment::class.simpleName
        var fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) {
            var fragmentTransaction = getSupportFragmentManager().beginTransaction()
            fragmentTransaction.replace(containerId, fragment, backStateName)
            if (withBackStack)
                fragmentTransaction.addToBackStack(backStateName)
            fragmentTransaction.commitAllowingStateLoss()

        }

    }


}
