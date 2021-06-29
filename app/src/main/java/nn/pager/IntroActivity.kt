package nn.pager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()

        Handler().postDelayed({

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()

        }, 2000)
    }
}