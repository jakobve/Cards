package es.uam.eps.dadm.cards.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentLoginBinding
import es.uam.eps.dadm.cards.state.AuthenticationState
import es.uam.eps.dadm.cards.viewModel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false)

        viewModel.authenticationState.observe(viewLifecycleOwner) { result ->
            if (result == AuthenticationState.AUTHENTICATED) {
                // Navigate to the main screen after successful login
                view?.findNavController()?.navigate(
                    LoginFragmentDirections.actionLoginFragmentToDeckListFragment()
                )
            } else if (result == AuthenticationState.INVALID_AUTHENTICATION) {
                // Show an error message to the user
                Toast.makeText(
                    requireContext(),
                    AuthenticationState.INVALID_AUTHENTICATION.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        binding.goToSignup.setOnClickListener {
            it.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do nothing to disable the back button
                }
            })
    }
}


