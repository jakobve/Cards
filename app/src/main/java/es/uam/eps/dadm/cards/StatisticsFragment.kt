package es.uam.eps.dadm.cards

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding
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
        binding = DataBindingUtil.inflate<FragmentStatisticsBinding>(
            inflater,
            R.layout.fragment_statistics,
            container,
            false
        )

        // Create the PieChart
        val pieChart: PieChart = binding.statisticsChart


        viewModel.decksWithCards.observe(viewLifecycleOwner) {
            decksWithCards = it
            cards = decksWithCards.flatMap { it.cards }
            decks = decksWithCards.map { it.deck }

            val avgCardQuality = cards.sumOf { it.quality }.div(cards.size)

            binding.generalStatisticsTotalNumberOfDecksNumber.setText(decks.size)

            binding.generalStatisticsTotalNumberOfCardsNumber.setText(cards.size)

            binding.userStatisticsAvgCardQualityNumber.text = avgCardQuality.toString()

            // Difficult cards
            cards.filter { it.quality == 0 }.size.let {
                PieEntry(it.toFloat(), 0) }?.let { entries.add(it)
                }

            // Doubt cards
            cards.filter { it.quality == 3 }.size.let {
                PieEntry(it.toFloat(), 0) }?.let { entries.add(it)
                }

            // Good cards
            cards.filter { it.quality == 5 }.size.let {
                PieEntry(it.toFloat(), 0) }?.let { entries.add(it)
                }

            // Repetitions
            binding.userStatisticsNumberOfReviewsNumber.setText(cards.count { it.answered })

            // Due this week
            val nDueCardsWeek = cards?.filter {
                LocalDateTime.parse(it.nextPracticeDate).isBefore(LocalDateTime.now().plusWeeks(1))
            }
            binding.upcomingThisWeekNumber.setText(nDueCardsWeek?.size ?: 0)

            // Due more than one week
            var nDueCardsMoreWeek = cards?.filter {
                LocalDateTime.parse(it.nextPracticeDate).isAfter(LocalDateTime.now().plusWeeks(1))
            }
            binding.upcomingWeeksNumber.setText(nDueCardsMoreWeek?.size ?: 0)
        }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.decksWithCards.observe(viewLifecycleOwner) {
            var message = String()
            it.forEach {
                message += "The deck named ${it.deck.name} has ${it.cards.size} cards\n" }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        Timber.i("On Resume statistics fragment")
        //viewModel.updateLiveData()
        super.onStart()
    }


}