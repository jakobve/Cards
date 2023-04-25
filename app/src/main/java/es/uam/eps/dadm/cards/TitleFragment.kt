package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
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

        val user = FirebaseAuth.getInstance().currentUser

        binding.titleFragmentRelativeLayout.setOnClickListener { view ->

            if (user != null) {
                view.findNavController().navigate(R.id.action_titleFragment_to_deckListFragment)
            } else {
                view.findNavController().navigate(R.id.action_titleFragment_to_loginFragment)
            }
        }
        return binding.root
    }
}
