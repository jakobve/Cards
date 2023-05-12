package es.uam.eps.dadm.cards.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity
import es.uam.eps.dadm.cards.state.AuthenticationState

@SuppressLint("StaticFieldLeak")
class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val auth = Firebase.auth

    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    fun signUp(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                _authenticationState.value = AuthenticationState.AUTHENTICATED
                                SettingsActivity.setLoggedIn(context, true)
                                SettingsActivity.setUserID(context, email)
                            } else {
                                _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                            }
                        }
                } else {
                    _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                }
            }
    }
}
