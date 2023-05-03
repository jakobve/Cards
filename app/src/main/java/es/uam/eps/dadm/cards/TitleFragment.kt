package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private val titleViewModel by lazy {
        ViewModelProvider(this)[TitleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        val userLoggedIn = titleViewModel.getUserLoggedIn()
        val userId = titleViewModel.getUserId()

        binding.titleFragmentRelativeLayout.setOnClickListener { view ->

            if (userLoggedIn && userId != "") {
                view.findNavController().navigate(R.id.action_titleFragment_to_deckListFragment)
            } else {
                view.findNavController().navigate(R.id.action_titleFragment_to_loginFragment)
            }
        }
        titleViewModel.downloadDecks()
        titleViewModel.downloadCards()


        return binding.root
    }
}
