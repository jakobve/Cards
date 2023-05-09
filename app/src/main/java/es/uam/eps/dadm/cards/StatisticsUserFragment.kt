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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import es.uam.eps.dadm.cards.Model.Card
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsUserBinding
import kotlinx.coroutines.launch


class StatisticsUserFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsUserBinding

    private lateinit var decks: List<Deck>
    private lateinit var cards: List<Card>

    private val viewModel: StatisticsViewModel by activityViewModels()

    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_statistics_user,
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
        }

        lifecycleScope.launch{
            viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
                decks = it.map { it.deck }
                cards = it.flatMap { it.cards }

                // Number of reviews today
                binding.userStatisticsNumberOfReviewsNumber.text = viewModel.getRepetitionsToday().toString()

                // Average quality of cards
                val avgCardQuality = cards.sumOf { it.quality }.div(cards.size)
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

                updatePieChart(cards)
            }
        }
        return binding.root
    }

    private fun updatePieChart(cards: List<Card>) {

        val entryGoodCards = PieEntry(cards.filter { it.quality == 5 }.size.toFloat(), "Good")
        val entryDoubtCards = PieEntry(cards.filter { it.quality == 3 }.size.toFloat(), "Doubt")
        val entryDifficultCards = PieEntry(cards.filter { it.quality == 0 }.size.toFloat(), "Difficult")

        val entries: MutableList<PieEntry> = mutableListOf()

        entries.add(entryGoodCards)
        entries.add(entryDoubtCards)
        entries.add(entryDifficultCards)

        val dataSet = PieDataSet(entries, "Card performance")
        dataSet.setValueTextColors(mutableListOf(Color.WHITE))
        val data = PieData(dataSet)
        pieChart.data = data
        dataSet.colors = listOf(
            Color.parseColor("#99CC00"), // green
            Color.parseColor("#33B5E5"), // blue
            Color.parseColor("#FF4444") // orange
        )

        dataSet.valueTextSize = 12f

        dataSet.sliceSpace = 2f
        dataSet.selectionShift = 5f
        dataSet.setDrawValues(true)

        pieChart.invalidate()
    }
}