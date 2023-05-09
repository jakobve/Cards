package es.uam.eps.dadm.cards

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime

class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by lazy {
        ViewModelProvider(this)[StatisticsViewModel::class.java]
    }

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var decksWithCards: List<DeckWithCards>
    private lateinit var decks: List<Deck>
    private lateinit var cards: List<Card>
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_statistics,
            container,
            false
        )

        // Create the PieChart
        pieChart = binding.pieChart

        pieChart.apply {
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            holeRadius = 50f
            transparentCircleRadius = 55f
            invalidate()
        }

        //Create BarChart
        barChart = binding.barChart


        // Observer the livedata
        lifecycleScope.launch {
            viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
                decksWithCards = it
                Timber.i("Initialize statistics fragment")
                decks = decksWithCards.map { it.deck }
                cards = decksWithCards.flatMap { it.cards }

                val avgCardQuality = cards.sumOf { it.quality }.div(cards.size)

                // Number of decks
                binding.generalStatisticsTotalNumberOfDecksNumber.text = decks.size.toString()

                // Number of cards
                binding.generalStatisticsTotalNumberOfCardsNumber.text = cards.size.toString()

                // Repetitions
                binding.userStatisticsNumberOfReviewsNumber.text = cards.count { it.answered }.toString()

                // Number of reviews today
                binding.userStatisticsNumberOfReviewsNumber.text = cards.filter { it.answered }.size.toString()

                // Average quality of cards
                binding.userStatisticsAvgCardQualityNumber.text = avgCardQuality.toString()

                // Good cards
                val nGoodCards = cards.filter { it.quality == 5 }.size
                binding.userStatisticsGoodCardsNumber.text = nGoodCards.toString()

                // Doubt cards
                val nDoubtCards = cards.filter { it.quality == 3 }.size
                binding.userStatisticsDoubtCardsNumber.text = nDoubtCards.toString()

                // Difficult cards
                val nDifficultCards = cards.filter { it.quality == 0 }.size
                binding.userStatisticsBadCardsNumber.text = nDifficultCards.toString()

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

                updatePieChart(cards)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
            var message = String()
            it.forEach {
                message += "The deck named ${it.deck.name} has ${it.cards.size} cards\n" }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updatePieChart(cards: List<Card>) {

        val entryGoodCards = PieEntry(cards.filter { it.quality == 5 }.size.toFloat(), "Good")
        val entryDoubtCards = PieEntry(cards.filter { it.quality == 3 }.size.toFloat(), "Doubt")
        val entryDifficultCards = PieEntry(cards.filter { it.quality == 0 }.size.toFloat(), "Difficult")

        var entries: MutableList<PieEntry> = mutableListOf()

        entries.add(entryGoodCards)
        entries.add(entryDoubtCards)
        entries.add(entryDifficultCards)

        val dataSet = PieDataSet(entries, "Card performance")
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()

        dataSet.colors = listOf(
            Color.parseColor("#99CC00"), // green
            Color.parseColor("#33B5E5"), // blue
            Color.parseColor("#FF4444") // orange
        )

        dataSet.sliceSpace = 2f
        dataSet.selectionShift = 5f
        dataSet.setDrawValues(true)
    }

    private fun updateBarChart() {

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

        val entryDueToday = BarEntry(0.toFloat(), nDueCardsToday.size.toFloat())
        val entryDueTomorrow = BarEntry(1.toFloat(), nDueCardsTomorrow.size.toFloat())
        val entryDueDayAfterTomorrow = BarEntry(2.toFloat(), nDueCardsDayAfterTomorrow.size.toFloat())

        var entries: MutableList<BarEntry> = mutableListOf()
        entries.add(entryDueToday)
        entries.add(entryDueTomorrow)
        entries.add(entryDueDayAfterTomorrow)
    }

}