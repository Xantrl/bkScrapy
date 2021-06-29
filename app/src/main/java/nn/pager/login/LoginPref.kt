package nn.pager.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class LoginPref {
    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var _context : Context

    var PRIVATEM_MODE : Int  = 0

    constructor(_context : Context) {
        this._context = _context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATEM_MODE)
        editor = pref.edit()
    }
    companion object {
        val PREF_NAME =  "userSession"
        val IS_LOGIN = "isLoggedIn"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"

    }
    fun createLoginSession (user : String, pass : String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, user)
        editor.putString(KEY_PASSWORD, pass)
        editor.commit()

    }
    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var i : Intent = Intent(_context, Login::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            _context.startActivity(i)
        }
    }
    fun getUserDetails(): HashMap<String, String> {
        var user : Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_USERNAME, pref.getString(KEY_USERNAME, null)!!)
        (user as HashMap).put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null)!!)
        return user
    }
    fun LogoutUser() {
        editor.clear()
        editor.commit()
   //     var i : Intent = Intent(_context, Login::class.java)
   //     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
   //     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
   //     _context.startActivity(i)
    }
    fun isLoggedIn() : Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
    fun getUsername() : String? {
        return pref.getString(KEY_USERNAME, "")
    }

}