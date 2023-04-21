package es.uam.eps.dadm.cards

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
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentCardEditBinding
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.Executors


class CardEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var binding: FragmentCardEditBinding
    private lateinit var card: Card
    lateinit var question: String
    lateinit var answer: String

    private val viewModel by lazy {
        ViewModelProvider(this).get(CardEditViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_edit,
            container,
            false
        )
        val args = CardEditFragmentArgs.fromBundle(requireArguments())

        if(args.cardId != "0") {
            viewModel.loadCardId(args.cardId)
            Timber.i(args.cardId)

            viewModel.card.observe(viewLifecycleOwner) {
                card = it
                question = it.question
                answer = it.answer
                binding.card = it
                Timber.i(it.question)
            }
        } else {
            card = Card("", "", id = "0", deckId = args.deckId)
            question = card.question
            answer = card.answer
            binding.card = card
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Timber.i("Start")
        // TODO: Check if its ok to access the viewmodel here
        if(viewModel.card.value?.question == "" || viewModel.card.value?.answer == "") {
            binding.editCardDeleteButton.isGone = true
        }

        val questionTextWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                card.question = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        val answerTextWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                card.answer = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        binding.editCardEditQuestionText.addTextChangedListener(questionTextWatcher)
        binding.editCardEditAnswerText.addTextChangedListener(answerTextWatcher)

        binding.editCardAcceptButton.setOnClickListener {

            if(card.question.trim() != "" && card.answer.trim() != "" && card.id != "0") {
                // TODO: Check if this is right??
                executor.execute {
                    viewModel.updateCard(card)
                }
            } else if (card.question.trim() != "" && card.answer.trim() != "" && card.id == "0") {
                card.id = UUID.randomUUID().toString()
                viewModel.addCard(card)
            }

            it.findNavController()
                .navigate(CardEditFragmentDirections
                    .actionCardEditFragmentToCardListFragment(
                        card.deckId
                    )
                )
        }

        binding.editCardCancelButton.setOnClickListener {
            card.question = question
            card.answer = answer

            it.findNavController()
                .navigate(CardEditFragmentDirections
                    .actionCardEditFragmentToCardListFragment(
                        card.deckId
                    )
                )
        }

        binding.editCardDeleteButton.setOnClickListener {

            viewModel.removeCard(card)

            it.findNavController()
                .navigate(CardEditFragmentDirections
                    .actionCardEditFragmentToCardListFragment(
                        card.deckId
                    )
                )
        }

    }

}