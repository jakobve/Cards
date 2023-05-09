package es.uam.eps.dadm.cards.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import es.uam.eps.dadm.cards.R
import es.uam.eps.dadm.cards.model.Card
import es.uam.eps.dadm.cards.model.Deck
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsGeneralBinding
import es.uam.eps.dadm.cards.viewModel.StatisticsViewModel
import kotlinx.coroutines.launch


class StatisticsGeneralFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsGeneralBinding
    private lateinit var decks: List<Deck>
    private lateinit var cards: List<Card>

    private val viewModel: StatisticsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_statistics_general,
            container,
            false
        )

        lifecycleScope.launch{
            viewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
                decks = it.map { it.deck }
                cards = it.flatMap { it.cards }

                // Number of decks
                binding.generalStatisticsTotalNumberOfDecksNumber.text = decks.size.toString()

                // Number of cards
                binding.generalStatisticsTotalNumberOfCardsNumber.text = cards.size.toString()
            }
        }
        return binding.root
    }
}