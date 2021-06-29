package nn.pager.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nn.pager.MainActivity
import nn.pager.R

class Login : AppCompatActivity() {
    private lateinit var etUsername : EditText
    private lateinit var etPassword : EditText

    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLogin : Button = findViewById(R.id.buttonLogin)

        etUsername = findViewById(R.id.textFieldUsername)
        etPassword = findViewById(R.id.textFieldPassword)


        session = LoginPref(this)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                session.LogoutUser()
                session.createLoginSession(username, password)

                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)  //register
                finish()
            }
            else {
                Toast.makeText(this, "Fill all the forms!", Toast.LENGTH_LONG).show()
            }
        }

    }
}