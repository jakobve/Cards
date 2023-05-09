package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.dadm.cards.Model.Card
import es.uam.eps.dadm.cards.database.CardDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor
import kotlin.properties.Delegates

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