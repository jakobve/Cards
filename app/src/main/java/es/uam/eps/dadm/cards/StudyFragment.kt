package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import es.uam.eps.dadm.cards.databinding.FragmentStudyBinding
import kotlinx.coroutines.launch
import timber.log.Timber

class StudyFragment : Fragment() {

    private val viewModel: StudyViewModel by lazy {
     ViewModelProvider(this)[StudyViewModel::class.java]
    }

    private lateinit var binding: FragmentStudyBinding

    private var card: Card? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate<FragmentStudyBinding>(
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

            binding.boardView.clearBoard()
            binding.invalidateAll()
        }

        // Retrieve arguments needed
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        val deckId = args.deckId

        viewModel.loadDeckId(deckId)

        viewModel.dueCard.observe(viewLifecycleOwner) {

            card = it
            binding.card = card

            binding.invalidateAll()
        }

        viewModel.nDueCards.observe(viewLifecycleOwner) {
            binding.dueCardsInfoChip.text = "$it cards remaining"
            binding.invalidateAll()
        }

        binding.answerButton.setOnClickListener {
            card?.answered = true
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

        if(viewModel.getBoardSetting()) {
            Timber.i("Board activated")
            binding.boardView.isGone = false
            binding.separatorViewBoard1.isGone = false
            binding.separatorViewBoard2.isGone = false
        } else {
            binding.boardView.isGone = true
            binding.separatorViewBoard1.isGone = true
            binding.separatorViewBoard2.isGone = true
        }

        viewModel.loadDeckId(deckId)

        super.onStart()
    }
}
