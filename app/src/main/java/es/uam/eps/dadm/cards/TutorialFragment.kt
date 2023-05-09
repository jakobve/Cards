package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.uam.eps.dadm.cards.databinding.FragmentTutorialDecksBinding

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // Get the ViewPager and set the adapter
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val adapter = TutorialPagerAdapter(supportFragmentManager)

        // Add the fragments and titles to the adapter
        adapter.addFragment(TutorialDecksFragment(), "Decks")
        adapter.addFragment(TutorialCardsFragment(), "Cards")
        adapter.addFragment(TutorialStatisticsFragment(), "Statistics")

        // Set the adapter to the ViewPager
        viewPager.adapter = adapter

        // Get the TabLayout and set it up with the ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}