package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.cards.databinding.FragmentStudyBinding
import timber.log.Timber
import kotlin.properties.Delegates

class StudyFragment : Fragment() {

    private val viewModel: StudyViewModel by lazy {
     ViewModelProvider(this)[StudyViewModel::class.java]
    }

    private lateinit var card: Card
    private var nDueCards by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentStudyBinding>(
            inflater,
            R.layout.fragment_study,
            container,
            false
        )

        val listener = View.OnClickListener {v ->
            val quality = when (v?.id) {
                binding.easyQualityButton.id-> 5
                binding.doubtQualityButton.id -> 3
                binding.difficultQualityButton.id -> 0
                else -> throw Exception("Invalid quality")
            }
            viewModel.update(quality)

            if(card == null) {
                Timber.i("Card is null")
                Toast.makeText(requireContext(), "No more cards to review", Toast.LENGTH_SHORT).show()
            }
            binding.invalidateAll()
        }

        // Retrieve arguments needed
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        val deckId = args.deckId

        viewModel.loadDeckId(deckId)

        viewModel.dueCard.observe(viewLifecycleOwner) {
            if (it != null) {
                card = it
            }
            binding.card = card
            binding.invalidateAll()
        }

        viewModel.nDueCards.observe(viewLifecycleOwner) {
            binding.dueCardsInfoChip.text = it.toString()
            binding.invalidateAll()
        }

        binding.studyFragmentContainer.setOnClickListener {
            card.answered = true
            binding.invalidateAll()
        }

        binding.apply {
            easyQualityButton.setOnClickListener(listener)
            doubtQualityButton.setOnClickListener(listener)
            difficultQualityButton.setOnClickListener(listener)
        }

        return binding.root
    }

    override fun onStart() {
        // Retrieve arguments needed
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        val deckId = args.deckId

        viewModel.loadDeckId(deckId)

        super.onStart()
    }
}
