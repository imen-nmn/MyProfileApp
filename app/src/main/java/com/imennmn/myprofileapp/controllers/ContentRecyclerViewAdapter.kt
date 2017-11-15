package com.imennmn.myprofileapp.controllers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.imennmn.myprofileapp.R
import com.imennmn.myprofileapp.ui.ProfileFragment.OnListFragmentInteractionListener


class ContentRecyclerViewAdapter(private val mValues: List<String>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
//        holder.mIdView.text = mValues[position]
        holder.mContentView.text = mValues[position]

        holder.mView.setOnClickListener {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
//                    mListener!!.onListFragmentInteraction(holder.mItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIconView: ImageView = mView.findViewById<View>(R.id.icon) as ImageView
        val mContentView: TextView = mView.findViewById<View>(R.id.content) as TextView
        var mItem: String? = null

        override fun toString(): String {
            return """${super.toString()} '${mContentView.text}'"""
        }
    }
}
