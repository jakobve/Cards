package es.uam.eps.dadm.cards.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity
import es.uam.eps.dadm.cards.model.Card
import timber.log.Timber

class CardListViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var deckId = MutableLiveData<Long>()

    var cards: LiveData<List<Card>> = deckId.switchMap {
        CardsApplication.getCardsOfDeck(context, it)
    }

   fun loadDeckId(id: Long) {
       deckId.value = id
       Timber.i("Load deckId")
   }

    fun logOut() {
        SettingsActivity.setLoggedIn(context, false)
        SettingsActivity.setUserID(context, "")
        auth.signOut()
    }
}