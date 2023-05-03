package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding


class DeckListFragment : Fragment() {

    private lateinit var adapter: DeckAdapter

    private val deckListViewModel by lazy {
        ViewModelProvider(this)[DeckListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentDeckListBinding>(
            inflater,
            R.layout.fragment_deck_list,
            container,
            false
        )

        this.adapter = DeckAdapter()
        // val decks = deckListViewModel.decks.value?.map { it.deck }
        adapter.data = emptyList()
        binding.deckListRecyclerView.adapter = adapter

        deckListViewModel.decksWithCards.observe(viewLifecycleOwner) { it ->
            adapter.data = it.map { it.deck }
            adapter.notifyDataSetChanged()
        }
        
        binding.newDeckFab.setOnClickListener { view ->
            /*
            val deck = decks?.maxOf { id }?.plus(1)?.let { Deck(it.toLong(), "") }
            if (deck != null) {
                deckListViewModel.addDeck(deck)
            }
             */
           view.findNavController()
               .navigate(
                   DeckListFragmentDirections.actionDeckListFragmentToDeckEditFragment(-1)
               )
            print("Button clicked")
        }
        return binding.root
    }

    override fun onStop() {
        deckListViewModel.uploadDecks()
        deckListViewModel.uploadCards()
        super.onStop()
    }

}