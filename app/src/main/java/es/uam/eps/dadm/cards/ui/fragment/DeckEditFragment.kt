package es.uam.eps.dadm.cards.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.R
import es.uam.eps.dadm.cards.model.Deck
import es.uam.eps.dadm.cards.databinding.FragmentDeckEditBinding
import es.uam.eps.dadm.cards.model.DeckWithCards
import es.uam.eps.dadm.cards.viewModel.DeckEditViewModel
import timber.log.Timber

class DeckEditFragment : Fragment() {

    private val deckEditViewModel by lazy {
        ViewModelProvider(this)[DeckEditViewModel::class.java]
    }

    private lateinit var binding: FragmentDeckEditBinding
    private lateinit var deckWithCards: DeckWithCards
    lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = DataBindingUtil.inflate(
           inflater,
           R.layout.fragment_deck_edit,
           container,
           false
       )
        val args = DeckEditFragmentArgs.fromBundle(requireArguments())

        if (args.deckId != -1L) {
            deckEditViewModel.loadDeckId(args.deckId)

            Timber.i(args.deckId.toString())

            deckEditViewModel.deckWithCards.observe(viewLifecycleOwner) {
                deckWithCards = it
                binding.deck = deckWithCards.deck
                name = deckWithCards.deck.name
            }
        } else {
            deckWithCards  = DeckWithCards(
                Deck(
                    args.deckId,
                    "",
                    deckEditViewModel.getUserId()!!
                ),
                mutableListOf()
            )
            binding.deck = deckWithCards.deck
            name = deckWithCards.deck.name
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()


        // In case a new deck is to be added do not show the delete button
        if(deckEditViewModel.deckWithCards.value?.deck?.name == "") {
            binding.editDeckDeleteButton.isGone = true
        }

        val nameTextWatcher = object: TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                deckWithCards.deck.name = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        binding.editDeckEditName.addTextChangedListener(nameTextWatcher)

        // TODO check whether to use DeckWithCards or Deck
        binding.editDeckAcceptButton.setOnClickListener {
            Timber.i("Click accept button")
            if(deckWithCards.deck.name.trim() != "" && deckWithCards.deck.deckId != -1L) {
                Timber.i("Update existing deck")
                deckEditViewModel.updateDeck(deckWithCards.deck)
            } else if (deckWithCards.deck.name.trim() != "" && deckWithCards.deck.deckId == -1L) {
                Timber.i("Add new deck")
                deckEditViewModel.addDeck(deckWithCards)
            }

            it.findNavController()
                .navigate(
                    DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment()
                )
        }

        binding.editDeckCancelButton.setOnClickListener {
            Timber.i("Click accept button")
            deckWithCards.deck.name = name
            it.findNavController()
                .navigate(
                    DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment()
                )
        }

        // TODO: Check whether to use DeckWithCards or just Deck
        binding.editDeckDeleteButton.setOnClickListener {

            deckEditViewModel.removeDeck(deckWithCards.deck.deckId)

            it.findNavController()
                .navigate(
                    DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment()
                )
        }

        /*
        binding.generateTestDeckButton.setOnClickListener {
            CardsApplication.decks.remove(deck)
            CardsApplication.generateTestDeck()

            it.findNavController()
                .navigate(DeckEditFragmentDirections
                    .actionDeckEditFragmentToDeckListFragment()
                )
        }

         */
    }
}