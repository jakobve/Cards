package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding

class CardListFragment : Fragment() {
    private lateinit var adapter: CardAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CardListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentCardListBinding>(
            inflater,
            R.layout.fragment_card_list,
            container,
            false
        )

        // Retrieve arguments needed
        val args = CardListFragmentArgs.fromBundle(requireArguments())

        // Add the arguments to the navigation to fragment_card_list
        this.adapter = CardAdapter()
        val deckId = args.deckId

        adapter.data = emptyList()
        binding.cardListRecyclerView.adapter = adapter

        viewModel.loadDeckId(args.deckId)

        viewModel.cards.observe(viewLifecycleOwner) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

        // onClickListener to review cards
        // TODO: Normally it should receive an argument but does not accept it
        binding.reviewCards.setOnClickListener {
            it.findNavController()
                .navigate(
                    CardListFragmentDirections
                        .actionCardListFragmentToStudyFragment()
                )
        }

        // onClickListener to add a new card
        binding.newCardFab.setOnClickListener {

            // TODO: Check if this is right
            it.findNavController()
                .navigate(
                    CardListFragmentDirections
                    .actionCardListFragmentToCardEditFragment(0.toString(), deckId)
                )
        }

        binding.radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                binding.radioEasiness.id -> {
                    adapter.data = viewModel.cards.value!!.sortedByDescending { it.easiness }
                    adapter.notifyDataSetChanged()
                }
                binding.radioRepetitions.id -> {
                    adapter.data = viewModel.cards.value!!.sortedByDescending { it.repetitions }
                    adapter.notifyDataSetChanged()
                }
            }
        }
        return binding.root
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_list, menu)
    }
     */

    // TODO
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
            R.id.log_out -> {
                viewModel.logOut()
                view?.findNavController()?.navigate(R.id.loginFragment)

            }
        }
        return true
    }

}