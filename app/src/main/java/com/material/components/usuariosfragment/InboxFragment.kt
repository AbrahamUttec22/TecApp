package com.material.components.usuariosfragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.material.components.R
import kotlinx.android.synthetic.main.fragment_inbox.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InboxFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InboxFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InboxFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return InboxFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.textTitle.text=getString(R.string.inbox)
    }
}
