package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.cards.database.CardDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor
import kotlin.properties.Delegates

class CardListViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var deckId = MutableLiveData<Long>()

    var cards: LiveData<List<Card>> = deckId.switchMap {
        CardsApplication.getCardsOfDeck(context, it)
    }

   fun loadDeckId(id: Long) {
       deckId.value = id
       Timber.i("Load deckId")
   }
}