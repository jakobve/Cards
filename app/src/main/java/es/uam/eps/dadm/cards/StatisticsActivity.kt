package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class StatisticsActivity : AppCompatActivity() {

    /*
    private val viewModel: StatisticsViewModel by lazy {
        ViewModelProvider(this)[StatisticsViewModel::class.java]
    }

     */

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // Get the ViewPager and set the adapter
        val viewPager = findViewById<ViewPager>(R.id.view_pager_tutorial)
        val adapter = TutorialPagerAdapter(supportFragmentManager)

        // Add the fragments and titles to the adapter
        adapter.addFragment(StatisticsGeneralFragment(), "General Stats")
        adapter.addFragment(StatisticsUserFragment(), "User Stats")
        adapter.addFragment(StatisticsForecastFragment(), "Forecast")

        // Set the adapter to the ViewPager
        viewPager.adapter = adapter

        // Get the TabLayout and set it up with the ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_tutorial)
        tabLayout.setupWithViewPager(viewPager)
    }

}