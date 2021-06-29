package nn.pager.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import nn.pager.R
import nn.pager.login.Login
import nn.pager.login.LoginPref
import nn.pager.login.LoginPref.Companion.PREF_NAME


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container,false)

        val btnAbout = view.findViewById<Button>(R.id.about)

        val session : SharedPreferences = activity?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)!!
        val loggedIn = session.getBoolean(LoginPref.IS_LOGIN, false)

        btnAbout.setOnClickListener {
            val k = Intent(activity, About::class.java)
            startActivity(k)
        }
        return view
    }


}