package com.material.tecgurus.usuariosfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.material.tecgurus.R

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
class PanelUsuariosFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return PanelUsuariosFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel_usuarios, container, false)
    }

    //acceder a la informacion de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
