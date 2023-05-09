package es.uam.eps.dadm.cards

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsForecastBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class StatisticsForecastFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsForecastBinding

    private lateinit var decks: List<Deck>
    private lateinit var cards: List<Card>

    private val viewModel: StatisticsViewModel by activityViewModels()

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_statistics_forecast,
            container,
            false
        )

        //Create BarChart
        barChart = binding.barChart

        val xAxisLabels = mutableListOf("Today", "Tomorrow", "Day after Tomorrow")
        // Customize the appearance of the chart
        barChart.apply {
            data = barData
            xAxis.apply {
                setDrawLabels(true)
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setLabelCount(3, true)
                position = XAxis.XAxisPosition.BOTTOM
                textSize = 15f
                granularity = 1f
                textColor = Color.BLACK
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase): String {
                        return xAxisLabels[value.toInt()]
                    }
                }
            }
            axisLeft.apply {
                setDrawLabels(true)
                setDrawGridLines(true)
                setDrawAxisLine(true)
                setDrawZeroLine(true)
                zeroLineWidth = 1f
                granularity = 1f
                axisMinimum = 0f
                textSize = 12f

            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            setDrawValueAboveBar(false)
            description.isEnabled = true
            description.text = "Card forecast"
            legend.isEnabled = false
            setTouchEnabled(false)
            setScaleEnabled(false)
            setFitBars(true)
        }

        lifecycleScope.launch{
            viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
                decks = it.map { it.deck }
                cards = it.flatMap { it.cards }

                // Due this week
                val nDueCardsWeek = cards.filter {
                    LocalDateTime.parse(it.nextPracticeDate).isBefore(LocalDateTime.now().plusWeeks(1))
                }
                binding.upcomingThisWeekNumber.text = nDueCardsWeek.size.toString()

                // Due more than one week
                val nDueCardsMoreWeek = cards.filter {
                    LocalDateTime.parse(it.nextPracticeDate).isAfter(LocalDateTime.now().plusWeeks(1))
                }
                binding.upcomingWeeksNumber.text = nDueCardsMoreWeek.size.toString()

                updateBarChart(cards)
            }
        }

        return binding.root
    }

    private fun updateBarChart(cards: List<Card>) {

        // Due today
        val nDueCardsToday = cards.filter {
            LocalDateTime.parse(it.nextPracticeDate).isBefore(LocalDateTime.now())
        }

        // Due tomorrow
        val nDueCardsTomorrow = cards.filter {
            LocalDateTime.parse(it.nextPracticeDate).isBefore(LocalDateTime.now().plusDays(1))
        }

        // Due day after tomorrow
        val nDueCardsDayAfterTomorrow = cards.filter {
            LocalDateTime.parse(it.nextPracticeDate).isAfter(LocalDateTime.now().plusDays(2))
        }

        val entryDueToday = BarEntry(0f, nDueCardsToday.size.toFloat(), "Today")
        val entryDueTomorrow = BarEntry(1f, nDueCardsTomorrow.size.toFloat(), "Tomorrow")
        val entryDueDayAfterTomorrow = BarEntry(2f, nDueCardsDayAfterTomorrow.size.toFloat(), "Day after tomorrow")

        barChart.xAxis.setDrawLabels(true)

        // Create entries
        val entries: MutableList<BarEntry> = mutableListOf()
        entries.add(entryDueToday)
        entries.add(entryDueTomorrow)
        entries.add(entryDueDayAfterTomorrow)

        // Create entries
        val barDataSet = BarDataSet(entries, "Data Set Label")
        barDataSet.setValueTextColors(mutableListOf(Color.WHITE))
        barDataSet.valueTextSize = 12f
        barDataSet.setDrawValues(true)

        // Set the colors for the bars
        barDataSet.colors = listOf(Color.RED, Color.BLUE, Color.GREEN)

        // Create a BarData object and add the dataset to it
        val barData = BarData(barDataSet)

        barChart.data = barData
        barChart.invalidate()
    }
}