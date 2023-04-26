package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    view.findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToDeckListFragment()
                    )
                }
                AuthenticationState.INVALID_AUTHENTICATION -> {
                    Toast.makeText(
                        requireContext(),
                        "Invalid credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Authentication Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                viewModel.signUp(email, password, username)
            } else {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToLogin.setOnClickListener {
            it.findNavController()
                .navigate(
                    SignUpFragmentDirections
                        .actionSignUpFragmentToLoginFragment()
                )
        }
    }

}
