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
import com.github.mikephil.charting.charts.PieChart
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
    private var entries: MutableList<PieEntry> = mutableListOf()

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
        val pieChart: PieChart = binding.statisticsChart
        lifecycleScope.launch {
            viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
                decksWithCards = it
                Timber.i("Initialize statistics fragment")
                decks = decksWithCards.map { it.deck }
                cards = decksWithCards.flatMap { it.cards }

                Timber.i(decks.size.toString())

                val avgCardQuality = cards.sumOf { it.quality }.div(cards.size)

                binding.generalStatisticsTotalNumberOfDecksNumber.text = decks.size.toString()

                binding.generalStatisticsTotalNumberOfCardsNumber.text = cards.size.toString()

                binding.userStatisticsAvgCardQualityNumber.text = avgCardQuality.toString()

                // Good cards
                val nGoodCards = cards.filter { it.quality == 5 }.size
                PieEntry(nGoodCards.toFloat(), 0).let { entries.add(it) }
                binding.userStatisticsGoodCardsNumber.text = nGoodCards.toString()

                // Doubt cards
                val nDoubtCards = cards.filter { it.quality == 3 }.size
                PieEntry(nDoubtCards.toFloat(), 0).let { entries.add(it) }
                binding.userStatisticsDoubtCardsNumber.text = nDoubtCards.toString()

                // Difficult cards
                val nDifficultCards = cards.filter { it.quality == 0 }.size
                PieEntry(nDifficultCards.toFloat(), 0).let { entries.add(it) }
                binding.userStatisticsBadCardsNumber.text = nDifficultCards.toString()

                // Repetitions
                binding.userStatisticsNumberOfReviewsNumber.text = cards.count { it.answered }.toString()

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


                //entries.add(PieEntry(viewModel.nDoubtCards.toFloat(), 1))
                //entries.add(PieEntry(viewModel.nBadCards.toFloat(), 2))

                val dataSet = PieDataSet(entries, "Card Performance")
                dataSet.colors = listOf(
                    Color.parseColor("#99CC00"), // green
                    Color.parseColor("#33B5E5"), // blue
                    Color.parseColor("#FF4444") // orange
                )

                dataSet.sliceSpace = 2f
                dataSet.selectionShift = 5f
                dataSet.setDrawValues(true)

                val data = PieData(dataSet)
                pieChart.data = data

                pieChart.apply {
                    isDrawHoleEnabled = true
                    setHoleColor(Color.TRANSPARENT)
                    holeRadius = 50f
                    transparentCircleRadius = 55f

                    invalidate()
                }
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

}