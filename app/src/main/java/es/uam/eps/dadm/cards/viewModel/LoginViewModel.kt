package es.uam.eps.dadm.cards.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import es.uam.eps.dadm.cards.state.AuthenticationState
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authenticationState.value = AuthenticationState.AUTHENTICATED
                    SettingsActivity.setLoggedIn(context, true)
                    SettingsActivity.setUserID(context, email)
                } else {
                    _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                }
            }
    }
}
