package es.uam.eps.dadm.cards.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.uam.eps.dadm.cards.R
import es.uam.eps.dadm.cards.ui.fragment.StatisticsForecastFragment
import es.uam.eps.dadm.cards.ui.fragment.StatisticsGeneralFragment
import es.uam.eps.dadm.cards.ui.fragment.StatisticsUserFragment
import es.uam.eps.dadm.cards.ui.adapter.TutorialPagerAdapter
import es.uam.eps.dadm.cards.viewModel.StatisticsViewModel

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