package nn.pager.events

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nn.pager.App
import nn.pager.R
import nn.pager.login.Login
import nn.pager.login.LoginPref.Companion.IS_LOGIN
import nn.pager.login.LoginPref.Companion.KEY_PASSWORD
import nn.pager.login.LoginPref.Companion.KEY_USERNAME


class EventFragment : Fragment() {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }

    private lateinit var refresh : FloatingActionButton

    private val eventVM : EventVM by viewModels {
        EventVM.EventVMFactory((requireActivity().application as App).repository)
    }

//    val getContent =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                eventVM.recrawl(username, password)
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val session : SharedPreferences = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)!!
        var loggedIn = session.getBoolean(IS_LOGIN, false)

        Log.d("Status EventFrag", loggedIn.toString())

        if (!loggedIn) {
            val i = Intent(activity, Login::class.java)
            startActivity(i)
        }

        val view = inflater.inflate(R.layout.fragment_event, container,false)               // LOGIN CHECK HERE
        val rV = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = EventAdapter()
        rV.adapter = adapter
        rV.layoutManager = LinearLayoutManager(activity)

        eventVM.allEvents.observe(viewLifecycleOwner) {events ->
            events.let { adapter.differ.submitList(it) }
        }
        val btn = view.findViewById<FloatingActionButton>(R.id.fab_main)
        refresh = view.findViewById<FloatingActionButton>(R.id.fab_refresh)
        val loginBtn = view.findViewById<FloatingActionButton>(R.id.fab_login)
        var clicked = false



        //Dialog

        val builder : AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder!!.setMessage("This will remove all current events in the database.")
            .setTitle("Switch account?")
        builder.apply {
            setPositiveButton("Understood") { dialog, id ->
                eventVM.deleteAll()
                loggedIn = false
                val i = Intent(activity, Login::class.java)
                startActivity(i)
            }
            setNegativeButton("Cancel") {dialog , id ->
            }
        }
        val dialog : AlertDialog = builder.create()
            //


// MENU BUTTONS
        btn.setOnClickListener {
            if (!clicked) {
                refresh.visibility = View.VISIBLE
                loginBtn.visibility = View.VISIBLE
                loginBtn.startAnimation(fromBottom)
                refresh.startAnimation(fromBottom)
                btn.startAnimation(rotateOpen)
                clicked = !clicked

            }
            else {
                refresh.visibility = View.INVISIBLE
                loginBtn.visibility = View.INVISIBLE
                loginBtn.startAnimation(toBottom)
                refresh.startAnimation(toBottom)
                btn.startAnimation(rotateClose)
                clicked = !clicked
            }
        }

        //get saved events
        if (loggedIn) {
            eventVM.getSaved().observe(viewLifecycleOwner) {
                val events = it ?: emptyList()
                if (events.isEmpty()) {
                                     ///  SMALL ARROWs INSTEAD
                }
                //CHECK 4 EMPTY LIST
                adapter.differ.submitList(events)

            }
        }
        //login opt
        loginBtn.setOnClickListener {
            if (loggedIn) {
                dialog.show()
            }
            else {
                val i = Intent(activity, Login::class.java)
                startActivity(i)

            }
        }
        //refresh opt
        refresh.setOnClickListener {
            if (loggedIn) {
                val username = session.getString(KEY_USERNAME, "")
                val password = session.getString(KEY_PASSWORD, "")
                if (username != null && password != null) {
                    eventVM.recrawl(username, password)
                }
            }
            else {
                Toast.makeText(activity, "Not logged in!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}