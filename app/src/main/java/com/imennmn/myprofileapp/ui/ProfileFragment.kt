package com.imennmn.myprofileapp.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.controllers.ContentRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class ProfileFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 4
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.profile, container, false)

        val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        val recyclerView : RecyclerView = view.findViewById<View>(R.id.list) as RecyclerView

        toolbar.title = ""
        toolbar.subtitle = ""
        mListener!!.setSupportActionBar(toolbar)
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);

        val context = view.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        val items = resources.getStringArray(R.array.profile_menu)
        recyclerView.adapter = ContentRecyclerViewAdapter(items.asList(), mListener)

        return view
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
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
