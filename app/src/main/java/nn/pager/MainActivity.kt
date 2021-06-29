package nn.pager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import nn.pager.events.EventFragment
import nn.pager.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewpager : ViewPager2 = findViewById(R.id.viewpager)

        val fragments : ArrayList<Fragment> = arrayListOf(
            EventFragment(),
            SettingsFragment(),
            //move to webview by button?
        )

        val adapter = ViewPagerAdapter(fragments, this)
        viewpager.adapter = adapter
    }//main?
    //scroll view needed

}